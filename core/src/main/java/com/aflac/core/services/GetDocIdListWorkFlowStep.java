package com.aflac.core.services;

import java.io.IOException;
import java.io.StringReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.aflac.core.config.AdobeSignConfig;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Component(property = { Constants.SERVICE_DESCRIPTION + "=Custom component to retrieve document Ids of an agreement.",
		Constants.SERVICE_VENDOR + "=Adobe Systems",
		"process.label" + "=Custom component to retrieve document Ids of an agreement" })
public class GetDocIdListWorkFlowStep implements WorkflowProcess {

	private static final Logger log = LoggerFactory.getLogger(GetDocIdListWorkFlowStep.class);
	private transient CloseableHttpClient httpclient;
	
	@Reference
	AdobeSignConfig adobeSignConfig;
	
	public GetDocIdListWorkFlowStep() {
		this.httpclient = HttpClients.createDefault();
	}

	public GetDocIdListWorkFlowStep(CloseableHttpClient httpclient, AdobeSignConfig adobeSignConfig) {
		this.httpclient = httpclient;
		this.adobeSignConfig = adobeSignConfig;
	}

	
	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap metaDataMap) throws WorkflowException {
		log.info("Retrieve Document Id as per agreement method...");
		
		String agreementIdVariable = metaDataMap.get("agreementIdVarName", String.class);
		String docIdListVariable = metaDataMap.get("docIdListVariableName", String.class);
		WorkflowData data = item.getWorkflowData();
		MetaDataMap metaData = data.getMetaDataMap();
				
		String agreementId = metaData.get(agreementIdVariable,String.class);
		
		HttpGet httpGet = new HttpGet(adobeSignConfig.getAdobeSignApiUrl() +"/agreements/"+ agreementId +"/documents");
		httpGet.addHeader("Authorization", "Bearer " + adobeSignConfig.getAccessToken());
		
		//CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			CloseableHttpResponse outputResponse = httpclient.execute(httpGet);
			int statusCode = outputResponse.getStatusLine().getStatusCode();
			log.info("Get Document ID API HTTP code: " + statusCode);
			HttpEntity entity = outputResponse.getEntity();
			String result = EntityUtils.toString(entity);
			if (statusCode == 200) {
				JsonReader jsonReader = new JsonReader(new StringReader(result));
				JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
				String documents = jsonObject.get("documents").toString();
				log.info("====> document Jason: " + documents);
				metaData.put(docIdListVariable, documents);
			} else {
				log.info("GetDocumentID API Error Response: " + result);
			}
		} catch (IOException e) {
			log.error("Error in getting Doc Ids: " + e);
		}finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
