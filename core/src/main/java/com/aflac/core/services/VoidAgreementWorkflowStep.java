package com.aflac.core.services;

import java.io.IOException;
import java.io.StringReader;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
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
import com.adobe.granite.workflow.model.WorkflowModel;
import com.aflac.apiworkflow.models.VoidAgreementRequest;
import com.aflac.core.config.AdobeSignConfig;
import com.aflac.core.util.DocumentStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Component(property = { Constants.SERVICE_DESCRIPTION + "=Custom component to void an agreement.",
		Constants.SERVICE_VENDOR + "=Adobe Systems",
		"process.label" + "=Custom component to void an agreement" })
public class VoidAgreementWorkflowStep implements WorkflowProcess {

	private static final Logger log = LoggerFactory.getLogger(VoidAgreementWorkflowStep.class);
	private transient CloseableHttpClient httpclient;
	
	@Reference
	AdobeSignConfig adobeSignConfig;
	
	public VoidAgreementWorkflowStep() {
		this.httpclient = HttpClients.createDefault();
	}

	public VoidAgreementWorkflowStep(AdobeSignConfig adobeSignConfig ,CloseableHttpClient httpclient) {
		this.adobeSignConfig = adobeSignConfig;
		this.httpclient = httpclient;
	}

	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap metaDataMap) throws WorkflowException {
		String voidStatus=null;
		try {
			log.info("----Inside VoidAgreementWorkflowStep----");
			String agreementIdValue = (String) item.getWorkflowData().getMetaDataMap().get("agreementId");
			
			log.info("Calling Void Agreement Api for Agreement Id: "+agreementIdValue);
			voidStatus = voidAgreement(agreementIdValue,voidStatus);
			
			log.info("Status got from API:"+voidStatus);
			
			if(voidStatus!=null && (voidStatus.equalsIgnoreCase(DocumentStatus.CANCELLED.getValue())||voidStatus.equalsIgnoreCase(DocumentStatus.ALREADY_SIGNED.getValue())))
				voidStatus = DocumentStatus.VOIDED.getValue();
			
			//getting attribute name of voidStatus from metaDataMap.
			WorkflowModel wfModel = session.getModel("/var/workflow/models/Void-Agreement-Workflow");
			String status = wfModel.getVariableTemplates().get("voidStatus").getName();
			
			//updating status value got from api.
			WorkflowData data = item.getWorkflowData();
			MetaDataMap metaData = data.getMetaDataMap();
			metaData.put(status, voidStatus);
			
		}
		catch (Exception e) {
			log.error("Error in VoidAgreementWorkflowStep: ", e);
		}
		
		
	}
	
	private String voidAgreement(String agreementIdValue, String status) throws IOException {
		HttpPut httpPut = new HttpPut(adobeSignConfig.getAdobeSignApiUrl() + "/agreements/"+agreementIdValue+"/status");
		httpPut.addHeader("Authorization", "Bearer " + adobeSignConfig.getAccessToken());
		ObjectMapper obj = new ObjectMapper();
		VoidAgreementRequest voidAgreementRequest = new VoidAgreementRequest();
		voidAgreementRequest.setValue("CANCEL");
		voidAgreementRequest.setComment("");
		voidAgreementRequest.setNotifySigner(true);
		StringEntity stringEntity = new StringEntity(obj.writeValueAsString(voidAgreementRequest));
		httpPut.setEntity(stringEntity);
		httpPut.addHeader("Content-Type", "application/json");
		
		CloseableHttpClient httpclient1 = HttpClients.createDefault();
		try {
			CloseableHttpResponse outputResponse = httpclient1.execute(httpPut);
			log.info("Void Agreement Api Status Code:" + outputResponse.getStatusLine().getStatusCode());
			HttpEntity entity = outputResponse.getEntity();
			String result = EntityUtils.toString(entity);
			if (outputResponse.getStatusLine().getStatusCode() == 201 || outputResponse.getStatusLine().getStatusCode() == 200) {
				JsonReader jsonReader = new JsonReader(new StringReader(result));
				JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
				status = jsonObject.get("result").getAsString();
				log.info("Agreement Status :" + status);
			} else {
				log.info("Void Agreement API Response: " + result);
			}
			outputResponse.close();
		} catch (IOException e) {
			log.error("Void Agreement Api Exception: ", e);
		} finally {
			httpclient1.close();
		}
		return status;
	}
	
	
}
