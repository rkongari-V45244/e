package com.aflac.core.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.adobe.granite.workflow.exec.Route;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
import com.aflac.apiworkflow.models.AgreementCreationInfo;
import com.aflac.apiworkflow.models.DocumentCreationInfo;
import com.aflac.apiworkflow.models.Recipient;
import com.aflac.apiworkflow.models.TransientId;
import com.aflac.core.config.AdobeSignConfig;
import com.aflac.core.osgi.service.MasterAppService;
import com.aflac.core.util.DocumentStatus;
import com.aflac.core.util.DocumentUtil;
import com.aflac.xml.masterApp.models.MasterApp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Component(property = { Constants.SERVICE_DESCRIPTION + "=Custom component to create an agreement.",
		Constants.SERVICE_VENDOR + "=Adobe Systems",
		"process.label" + "=Custom component to create an agreement" })
public class CreateAgreementWorkflowStep implements WorkflowProcess {

	private static final Logger log = LoggerFactory.getLogger(CreateAgreementWorkflowStep.class);
	
	@Reference
	private transient AdobeSignConfig adobeSignConfig;
	@Reference
	private transient MasterAppService masterAppService;
		
	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap metaDataMap) throws WorkflowException {
		log.info("----Inside Create Agreement------");
		String docListVariable = metaDataMap.get("docListVariableName", String.class);
		String formIdsVarName = metaDataMap.get("formIdsVarName", String.class);
		String agreementIdVariable = metaDataMap.get("agreementIdVarName", String.class);
		String formData = (String) item.getWorkflowData().getMetaDataMap().get("formXmlData");

		log.info("----Following variables are received docListVariable {}, formIdsVarName {}, agreementIdVariable {}------" + docListVariable, formIdsVarName, agreementIdVariable);
		String role = metaDataMap.get("role", String.class);
		String signatureFlow = metaDataMap.get("signatureFlow", String.class);
		List<String> transientIds = new ArrayList<>();

		WorkflowData data = item.getWorkflowData();
		MetaDataMap metaData = data.getMetaDataMap();
		
		Document[] docs = metaData.get(docListVariable, Document[].class);
		String[] formIds = metaData.get(formIdsVarName, String[].class);
		String name = metaData.get("strContractTitle", String.class);
		String firstSigner = metaData.get("firstSignerEmailId", String.class);
		String secondSigner = metaData.get("secondSignerEmailId", String.class);
		List<String> signers = new ArrayList<>();	
		if(firstSigner != null && !firstSigner.isEmpty())
			signers.add(firstSigner);
		if(secondSigner != null && !secondSigner.isEmpty())
			signers.add(secondSigner);
		HttpPost httpPost = new HttpPost(adobeSignConfig.getAdobeSignApiUrl() + "/transientDocuments");
		log.info("----adobeSignConfig.getAdobeSignApiUrl() {} ------" + adobeSignConfig.getAdobeSignApiUrl());
		httpPost.addHeader("Authorization", "Bearer " + adobeSignConfig.getAccessToken());
		CloseableHttpClient httpclient;
		
		for(int i=0; i<docs.length; i++) {
			InputStream is = writeDocument(docs[i], formIds[i]);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addBinaryBody("File", is, ContentType.APPLICATION_OCTET_STREAM, formIds[i]);
			builder.addTextBody("Mime-Type", "application/pdf");
			HttpEntity entity = builder.build();
			httpPost.setEntity(entity);
			
			CloseableHttpResponse outputResponse;
			httpclient = HttpClients.createDefault();
			try {
				log.info("----httpPost {}------" + httpPost);
				outputResponse = httpclient.execute(httpPost);
				log.info("----http outputResponse {}------" + outputResponse);
				int statusCode = outputResponse.getStatusLine().getStatusCode();
				log.info("---- Transient API HTTP code statusCode {} ------" + statusCode);
				HttpEntity resEntity = outputResponse.getEntity();
				String result = EntityUtils.toString(resEntity);
				log.info("---- result {} ------" + result);
				if(statusCode == 201) {
					JsonReader jsonReader = new JsonReader(new StringReader(result));
					JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
					String transientId = jsonObject.get("transientDocumentId").getAsString();
					log.info("Transient Document Id received: " + transientId);
					transientIds.add(transientId);
				} else {
					log.info("Transient API Error: " + result);
				}
				is.close();
			} catch (IOException e) {
				log.error("Error in Transient Document Api: ", e);
				session.terminateWorkflow(item.getWorkflow());
			} finally {
				try {
					httpclient.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		if(transientIds != null && !transientIds.isEmpty()) {
			httpclient = HttpClients.createDefault();
			try {
				String agreementId = createAgreement(transientIds,signers,name,role,signatureFlow,formData);

				log.info("----- AgreementId is created: " + agreementId);
				if(agreementId != null && !agreementId.isEmpty()) {
					
					saveDocumentIdsWorkflowStep saveIds = new saveDocumentIdsWorkflowStep();
					String masterAppId = metaData.get("strMasterAppId",String.class);
					String version = metaData.get("strMasterAppVersion",String.class);
					String[] masterAppIds = metaData.get("listOfFormIds", String[].class);
					String status = DocumentStatus.OUT_FOR_SIGNATURE.getValue();
					String res = saveIds.updateDocIdsAndStatus(masterAppId, version, masterAppIds, null, agreementId, status, masterAppService);
					log.info("----- AgreementId Update status: " + res);
					
					metaData.put(agreementIdVariable, agreementId);
					metaData.put("signatureStatus",status);
					
				}
			} catch (IOException e) {
				log.error("Error while creating Agreement: ", e);
				session.terminateWorkflow(item.getWorkflow());
			} finally {
				try {
					httpclient.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	private String createAgreement(List<String> transientId, List<String> signers, String name, String role, String signatureFlow, String formData) throws IOException {
		List<Recipient> recipients = new ArrayList<>();
		signers.forEach(s -> {recipients.add(new Recipient(role, s));});
		MasterApp masterApp = null;
		
		//getting masterApp Model instance from converted form XML
		if(formData!=null){
			masterApp = getMasterAppData(formData);
		}
		String agreementModel = getAgreementCreationInfo(transientId, name, recipients, signatureFlow,masterApp);
		String agreementId = "";
		HttpPost httpPost = new HttpPost(adobeSignConfig.getAdobeSignApiUrl() + "/agreements");
		httpPost.addHeader("Authorization", "Bearer " + adobeSignConfig.getAccessToken());
		StringEntity stringEntity = new StringEntity(agreementModel);
		httpPost.setEntity(stringEntity);
		httpPost.addHeader("Content-Type", "application/json");
		log.info("Agreement Request: "+agreementModel);
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try(final CloseableHttpResponse outputResponse = httpclient.execute(httpPost);)	{
			log.info("Create Agreement Api Status Code:" + outputResponse.getStatusLine().getStatusCode());
			HttpEntity entity = outputResponse.getEntity();
			String result = EntityUtils.toString(entity);
			if (outputResponse.getStatusLine().getStatusCode() == 201 || outputResponse.getStatusLine().getStatusCode() == 200) {
				JsonReader jsonReader = new JsonReader(new StringReader(result));
				JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
				agreementId = jsonObject.get("agreementId").getAsString();
				log.info("Agreement Id :" + agreementId);
			} else {
				log.info("Create Agreement API Response: " + result);
			}
			outputResponse.close();
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		} finally {
			httpclient.close();
		}
		return agreementId;
	}
	
	private MasterApp getMasterAppData(String formData) throws IOException {
		MasterApp masterApp =null;
		try {
			org.w3c.dom.Document doc = DocumentUtil.convertToDocument(formData);
			String masterAppXml = DocumentUtil.getNode("/afData/afBoundData/masterApp", doc);
						
			InputStream stream = new ByteArrayInputStream(masterAppXml.getBytes(StandardCharsets.UTF_8));
			JAXBContext context = JAXBContext.newInstance(MasterApp.class);
			Unmarshaller um = context.createUnmarshaller();

			masterApp = (MasterApp) um.unmarshal(stream);
		}catch (Exception e) {
			throw new IOException(e.getMessage());
		}
		return masterApp;
	}
	
	private String getAgreementCreationInfo(List<String> transientIds, String name, List<Recipient> recipients, String signatureFlow, MasterApp masterApp) {
		String minimalModel = "";
		AgreementCreationInfo agreementModel = new AgreementCreationInfo();
		DocumentCreationInfo docInfo = new DocumentCreationInfo();
		List<TransientId> fileInfos = new ArrayList<>();
		transientIds.forEach(id -> fileInfos.add(new TransientId(id)));
		docInfo.setFileInfos(fileInfos);
		docInfo.setName(name);
		docInfo.setRecipients(recipients);
		docInfo.setSignatureType("ESIGN");
		
		if(masterApp!=null) {
			docInfo.setMessage(masterApp.getSignatureMessage()!=null?masterApp.getSignatureMessage():null);
			docInfo.setDaysUntilSigningDeadline(masterApp.getSigningDeadline()!=null?masterApp.getSigningDeadline():null);
			docInfo.setReminderFrequency(masterApp.getReminderFrequency()!=null?masterApp.getReminderFrequency():null);
			docInfo.setCcs(masterApp.getCcs()!=null?Stream.of(masterApp.getCcs().split(";")).collect(Collectors.toList()):null);
			docInfo.setSignatureFlow(masterApp.getSignatureFlow()!=null?masterApp.getSignatureFlow():null);
		}
		agreementModel.setDocumentCreationInfo(docInfo);
		
		ObjectMapper obj = new ObjectMapper();
		try {
			minimalModel = obj.writeValueAsString(agreementModel);
		} catch (JsonProcessingException e) {
			log.error("Error while creating Agreement Model: ", e);
		}
		return minimalModel;
	}
	
	private InputStream writeDocument(Document documentToWrite, String key) {
		InputStream is = null;
		
		if (documentToWrite != null) {
			try {
				File tmpFile = File.createTempFile("tempPdf", ".pdf");
				documentToWrite.copyToFile(tmpFile);
				is = new FileInputStream(tmpFile);
				tmpFile.deleteOnExit();
				return is;
			} catch (IOException e) {
				log.error("Error in copyFile: ", e);
			}
		}
		return is;
	}
}
