package com.aflac.core.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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

@Component(property = { Constants.SERVICE_DESCRIPTION + "=Custom component to retrieve audit trail document.",
		Constants.SERVICE_VENDOR + "=Adobe Systems",
		"process.label" + "=Custom component to retrieve audit trail document" })
public class RetrieveAuditTrailDocWorkflowStep implements WorkflowProcess {

	private static final Logger log = LoggerFactory.getLogger(RetrieveAuditTrailDocWorkflowStep.class);
	
	@Reference
	AdobeSignConfig adobeSignConfig;
	
	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap metaDataMap) throws WorkflowException {
		log.info("Retrieve Audit Trail Document method...");
		
		String agreementIdVariable = metaDataMap.get("agreementIdVarName", String.class);
		WorkflowData data = item.getWorkflowData();
		MetaDataMap metaData = data.getMetaDataMap();
				
		String agreementId = metaData.get(agreementIdVariable,String.class);
		
		HttpGet httpGet = new HttpGet(adobeSignConfig.getAdobeSignApiUrl() + "/agreements/" + agreementId + "/auditTrail");
		httpGet.addHeader("Authorization", "Bearer " + adobeSignConfig.getAccessToken());
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			CloseableHttpResponse outputResponse = httpclient.execute(httpGet);
			int statusCode = outputResponse.getStatusLine().getStatusCode();
			log.info("Audit Trail API HTTP code: " + statusCode);
			log.info(outputResponse.getStatusLine().getReasonPhrase());
			
			if (statusCode == 200) {
				InputStream generatedPDF = outputResponse.getEntity().getContent();					
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IOUtils.copy(generatedPDF, baos);
				InputStream is = new ByteArrayInputStream(baos.toByteArray());
				
				// save audit trail document to JCR at payload path
				String auditTrailDoc = "AuditTrailDoc.pdf";
				String payloadPath = data.getPayload().toString();
				Node payloadNode = session.adaptTo(Session.class).getNode(payloadPath);
				ValueFactory valueFactory = session.adaptTo(Session.class).getValueFactory();
				Node transformedXmlNode = payloadNode.addNode(auditTrailDoc, "nt:file");
				Node resNode = transformedXmlNode.addNode("jcr:content", "nt:resource");

			    resNode.setProperty("jcr:mimeType", "application/pdf");
			    resNode.setProperty("jcr:encoding", "utf-8");
			    Binary value = valueFactory.createBinary(is);
			    resNode.setProperty("jcr:data", value);
			    Calendar lastModified = Calendar.getInstance();
			    lastModified.setTimeInMillis(lastModified.getTimeInMillis());
			    resNode.setProperty("jcr:lastModified", lastModified);
			}
		} catch (IOException | RepositoryException  e) {
			log.error("Error in getting Audit Trail Doc: " + e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
