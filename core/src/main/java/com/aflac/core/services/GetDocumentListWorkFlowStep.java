package com.aflac.core.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.aflac.apiworkflow.models.AgreementDocumentId;
import com.aflac.core.config.AdobeSignConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(property = { Constants.SERVICE_DESCRIPTION + "=Custom component to retrieve documents of an agreement.",
		Constants.SERVICE_VENDOR + "=Adobe Systems",
		"process.label" + "=Custom component to retrieve documents of an agreement" })
public class GetDocumentListWorkFlowStep implements WorkflowProcess {

	private static final Logger log = LoggerFactory.getLogger(GetDocumentListWorkFlowStep.class);
	
	@Reference
	AdobeSignConfig adobeSignConfig;
	
	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap metaDataMap) throws WorkflowException {
		log.info("--- ENTERING : GetDocumentListWorkFlowStep method ---");
		ObjectMapper Obj = new ObjectMapper();
		String agreementIdVariable = metaDataMap.get("agreementIdVarName", String.class);
		String formIdVariableName  = metaDataMap.get("formIdVariableName", String.class); 
		String docIdListVariable = metaDataMap.get("docIdListVariableName", String.class);
		String docListVariable = metaDataMap.get("docListVariableName", String.class);
		WorkflowData data = item.getWorkflowData();
		MetaDataMap metaData = data.getMetaDataMap();
		
		String agreementId = metaData.get(agreementIdVariable,String.class);
		String[] formIds = metaData.get(formIdVariableName, String[].class);
		metaData.put(formIdVariableName, formIds);
		String docIds = metaData.get(docIdListVariable,String.class);
		Document[] docList = new Document[formIds.length];
		log.info("=====> Document ID Json: " + docIds);
		CloseableHttpClient httpclient = HttpClients.createDefault();		
		try {
			AgreementDocumentId agreementdocJson;
			AgreementDocumentId[] docIdsArray = {};
			List<AgreementDocumentId> docIdList = new ArrayList<>();
			if(docIds.startsWith("[")) {
				docIdsArray = Obj.readValue(docIds, AgreementDocumentId[].class);
				docIdList = Arrays.asList(docIdsArray);
			}
			else {
				agreementdocJson = Obj.readValue(docIds, AgreementDocumentId.class);
				docIdList.add(agreementdocJson);
			}
			Set<String> formIdSet = new HashSet<String>(Arrays.asList(formIds));
			List<AgreementDocumentId> filteredDocIdList =  docIdList.stream().filter(d -> formIdSet.contains(d.getName())).collect(Collectors.toList());
			docIdList.stream().filter(d -> formIdSet.contains(d.getName()));
			for(int i=0; i < filteredDocIdList.size(); i++) {
				String docId = filteredDocIdList.get(i).getDocumentId();		
				HttpGet httpGet = new HttpGet(adobeSignConfig.getAdobeSignApiUrl() + "/agreements/" + agreementId + "/documents/" + docId);
				httpGet.addHeader("Authorization", "Bearer " + adobeSignConfig.getAccessToken());
				CloseableHttpResponse outputResponse = httpclient.execute(httpGet);
				int statusCode = outputResponse.getStatusLine().getStatusCode();
				log.info("Document API HTTP code: " + statusCode);
				
				if (statusCode == 200) {
					HttpEntity entity = outputResponse.getEntity();
					InputStream docStream = entity.getContent();
					byte[] bytes = IOUtils.toByteArray(docStream);
					InputStream copyIs = new ByteArrayInputStream(bytes);
					Document Agreementdoc = new Document(copyIs);
					docList[i] = Agreementdoc;
				}
			}
			log.info("--- SETTING : Agreement Document List ---");
			metaData.put(docListVariable, docList);
		} catch (IOException e) {
			log.error("Error in getting agreement Doc : " + e);
			session.terminateWorkflow(item.getWorkflow());
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				log.error("Error :", e);
			}
		}
		log.info("--- EXITING : GetDocumentListWorkFlowStep method ---");
	}
}
