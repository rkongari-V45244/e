package com.aflac.core.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
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
import com.aflac.apiworkflow.models.CustomFileNetResponse;
import com.aflac.apiworkflow.models.FileNetWFModel;
import com.aflac.core.config.FileNetApiConfig;
import com.aflac.core.osgi.service.FileNetService;
import com.aflac.core.util.AflacAppsHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Component(property = { Constants.SERVICE_DESCRIPTION + "=Custom component to wrtie generated pdf file to FileNet.",
		Constants.SERVICE_VENDOR + "=Adobe Systems", "process.label" + "=Custom component to wrtie file to FileNet. " })
public class FileNetWorkflowProcess implements WorkflowProcess {

	private static final Logger log = LoggerFactory.getLogger(FileNetWorkflowProcess.class);
	
	private static String objStore = "ACC";
	private ObjectMapper Obj;
	@Reference
	private FileNetApiConfig fileNetConfig;
	@Reference
	private FileNetService fileNet;
	
	@Reference
	AflacAppsHelper aflacAppsHelper;

	private InputStream writeDocument(Document documentToWrite, String key) {
		log.info(" Writing Document  " + key);
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

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
			throws WorkflowException {
		log.info("FileNet component...");
		String filenetAction = metaDataMap.get("filenetAction", String.class);
		log.info("====> filenetAction: " + filenetAction);
		this.Obj = new ObjectMapper();
		String masterAppId = (String) workItem.getWorkflowData().getMetaDataMap().get("strMasterAppId");
		String masterAppVersion = (String) workItem.getWorkflowData().getMetaDataMap().get("strMasterAppVersion");
		WorkflowData data = workItem.getWorkflowData();
		MetaDataMap metaData = data.getMetaDataMap();
		
		try {
			if(filenetAction.equalsIgnoreCase("save") || filenetAction.equalsIgnoreCase("write")) {
				saveFileToFilenet(workItem, workflowSession, metaDataMap);
			} else if(filenetAction.equalsIgnoreCase("read")) {
				readFileFromFilenet(workItem, metaDataMap);
			}
		} catch(IOException e) {
			String[] formIds = null;
			if(filenetAction.equalsIgnoreCase("save") || filenetAction.equalsIgnoreCase("write")) {
				String formIdVariableName  = metaDataMap.get("masterAppFormIdsVarName", String.class);
				formIds = metaData.get(formIdVariableName, String[].class);
			}
			else if(filenetAction.equalsIgnoreCase("read")) {
				String formIdVariableName  = metaDataMap.get("masterAppFormIdsVarName", String.class);
				String[] formIdsName = metaData.get(formIdVariableName, String[].class);
				if(formIdsName!=null && formIdsName.length>0) {
					List<String> list = new ArrayList<>();
					for(String formId : formIdsName) {
						list.add(formId.split(".pdf")[0]);
					}
					formIds = (String[]) list.toArray(new String[0]);
				}
				
			}
			log.error("Error in FileNet Service: ", e);
			workflowSession.terminateWorkflow(workItem.getWorkflow());
			String status = "Workflow Failed!";		
			aflacAppsHelper.updateMasterAppTableData(masterAppId, masterAppVersion, status, formIds);
		}
		
	}
	
	private void readFileFromFilenet(WorkItem workItem, MetaDataMap metaDataMap) throws IOException {
		// Read the names of the variable names passed from dialogue box
		log.info("Read from FileNet Server...");
		String fileNetDoc = metaDataMap.get("fileNetDocVarName", String.class);
		String formIdsVariable = metaDataMap.get("formIdVarName", String.class);
		String fileNetDocIDName = metaDataMap.get("fileNetDocIDName", String.class);

		MetaDataMap metaMap = workItem.getWorkflowData().getMetaDataMap();
		String filenetDocIdJson = metaMap.get(fileNetDocIDName, String.class);
		CustomFileNetResponse res = Obj.readValue(filenetDocIdJson, CustomFileNetResponse.class);
		Document[] docs = new Document[res.getFilenetIds().size()];
		String[] formIds = new String[res.getFilenetIds().size()];
		int i = 0;
		try {
			for (FileNetWFModel fn : res.getFilenetIds()) {
				String docId = fn.getDocumentId();
				if (!docId.isEmpty()) {
					log.info("====> docId: " + docId);
					docId = docId.replace("{", "").replace("}", "");
					log.info("DocID in aem server: " + docId);

					fileNet.getFileFromFileNetWorkFlow(docId, objStore, null);
					InputStream fileStream = fileNet.getPdfStream();
					if (fileStream != null) {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						IOUtils.copy(fileStream, baos);
						InputStream is = new ByteArrayInputStream(baos.toByteArray());

						Document doc = new Document(is);
						docs[i] = doc;
						formIds[i] = fn.getTitle();
						i++;
					} else {
						log.info("Error in Reading File from fileNet...");
					}
				} else {
					log.info("FileNet document Id is blank for FormID: " + fn.getTitle());
				}
			}
			metaMap.put(fileNetDoc, docs);
			metaMap.put(formIdsVariable, formIds);
		} catch (Exception e) {
			log.error("Error in readFileFromFilenet: ", e);
			throw new IOException("Error in readFileFromFilenet");
		}
	}
	
	private void saveFileToFilenet(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException, IOException {
		// Read the names of the variable names passed from dialogue box
		log.info("Save File to FileNet Server");
		String masterAppPdfsVarname = metaDataMap.get("masterAppPdfsVarname", String.class);
		String attPdfsVarname = metaDataMap.get("attPdfsVarname", String.class);
		String masterAppFormIdsVarName = metaDataMap.get("masterAppFormIdsVarName", String.class);
		String selectedAttVarName = metaDataMap.get("selectedAttVarName", String.class);
		String fileNetDocIDName = metaDataMap.get("fileNetDocIDName", String.class);
		String fileNetDocSaveStatus = metaDataMap.get("fileNetDocSaveStatus", String.class);

		MetaDataMap metaMap = workItem.getWorkflowData().getMetaDataMap();
		// Read the WF variable based on name
		Document[] masterAppPdfs = metaMap.get(masterAppPdfsVarname, Document[].class);
		Document[] attPdfs = {};
		if(!selectedAttVarName.equalsIgnoreCase("NA"))
			attPdfs = metaMap.get(attPdfsVarname, Document[].class);
		String[] masterAppFormIds = metaMap.get(masterAppFormIdsVarName, String[].class);
		String[] attachmentNames = {};
		if(!selectedAttVarName.equalsIgnoreCase("NA"))
			attachmentNames = metaMap.get(selectedAttVarName, String[].class);
		
		int allDocsLength = masterAppPdfs.length + attPdfs.length;
		Document[] allDocs = new Document[allDocsLength];
		System.arraycopy(masterAppPdfs, 0, allDocs, 0, masterAppPdfs.length);
		System.arraycopy(attPdfs, 0, allDocs, masterAppPdfs.length, attPdfs.length);
		log.info("All Document length: " + allDocsLength);
		
		int allTitlesLength = masterAppFormIds.length + attachmentNames.length;
		String[] allTitles = new String[allTitlesLength];
		for(int i=0; i<masterAppFormIds.length; i++) {
			if(!masterAppFormIds[i].contains(".pdf"))
				masterAppFormIds[i] = masterAppFormIds[i] + ".pdf";
		}
		System.arraycopy(masterAppFormIds, 0, allTitles, 0, masterAppFormIds.length);
		System.arraycopy(attachmentNames, 0, allTitles, masterAppFormIds.length, attachmentNames.length);
		
		List<FileNetWFModel> fileNetDocIds = new ArrayList<>();
		List<String> metaDatas = new ArrayList<>();
		List<InputStream> isPDFs = new ArrayList<>();
		
		for(int i=0; i<allDocs.length; i++) {
			Document documentToWrite = allDocs[i];
			String docTitle = allTitles[i];
			InputStream is = null;
			if (documentToWrite != null) {
				is = writeDocument(documentToWrite, docTitle);

				String metaData = "{\r\n"
						+ "    \"Document\": {\r\n"
						+ "        \"ObjectStore\": \"" + fileNetConfig.getObjectStore() + "\",\r\n"
						+ "        \"DocumentClass\": \"" + fileNetConfig.getDocClass() + "\",\r\n"
						+ "        \"Title\": \"" + docTitle + "\",\r\n"
						+ "        \"RetrievalName\": \"" + docTitle + "\",\r\n"
						+ "        \"ContentType\": \"application/pdf\",\r\n"
						+ "        \"Properties\": {\r\n"
						+ "            \"Property\": [\r\n"
						+ "                {\r\n"
						+ "                    \"name\": \"createDate360\",\r\n"
						+ "                    \"value\": \"" + LocalDateTime.now() + "\",\r\n"
						+ "                    \"datatype\": \"date\"\r\n"
						+ "                }\r\n"
						+ "            ]\r\n"
						+ "        }\r\n"
						+ "    }\r\n"
						+ "}";
				metaDatas.add(metaData);
				isPDFs.add(is);
			}
		}
		
		try {
			metaMap.put(fileNetDocIDName, "myDocID");
			metaMap.put(fileNetDocSaveStatus, "fail");
			// Store the metadata of the file sent for saving on to filenet
			String fileNetRes = fileNet.bullUploadOnFileNet(metaDatas, isPDFs);

			if(fileNetRes.isEmpty() || fileNetRes.contains("Bad") || fileNetRes.contains("errorCode") || fileNetRes.contains("faultcode")) {
				log.info("fileNet Bulk Upload Error =====> " + fileNetRes);
				// FileNet Response Status :Error
				metaMap.put("FileNetResponseStatus", "Status : Error - "+ fileNetRes);
				workflowSession.terminateWorkflow(workItem.getWorkflow());
				
				String masterAppId = (String) workItem.getWorkflowData().getMetaDataMap().get("strMasterAppId");
				String masterAppVersion = (String) workItem.getWorkflowData().getMetaDataMap().get("strMasterAppVersion");
				String formIdVariableName  = metaDataMap.get("masterAppFormIdsVarName", String.class);
				
				WorkflowData data = workItem.getWorkflowData();
				MetaDataMap metaData = data.getMetaDataMap();
				
				String status = "Workflow Failed!";
				String[] formIds = metaData.get(formIdVariableName, String[].class);
							
				aflacAppsHelper.updateMasterAppTableData(masterAppId, masterAppVersion, status, formIds);
				
			} else {
				JsonReader jsonReader = new JsonReader(new StringReader(fileNetRes));
				JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
				String document = jsonObject.get("Document").toString();
				if(allDocsLength == 1) {
					FileNetWFModel fm = Obj.readValue(document, FileNetWFModel.class);
					fileNetDocIds.add(fm);
				} else {
					FileNetWFModel[] docIds = Obj.readValue(document, FileNetWFModel[].class);
					fileNetDocIds = Arrays.asList(docIds);
				}
								
				// FileNet Response Status : Success
				metaMap.put("FileNetResponseStatus", "Status : Success");
				metaMap.put(fileNetDocSaveStatus, "success");
			}
		} catch (IOException e) {
			log.error("Error in Filenet workflow component: ", e);
			workflowSession.terminateWorkflow(workItem.getWorkflow());
			throw new IOException("Error in bullUploadOnFileNet");
		}
		CustomFileNetResponse res = new CustomFileNetResponse();
		res.setFilenetIds(fileNetDocIds);
		String json = Obj.writeValueAsString(res);
		metaMap.put(fileNetDocIDName, json);
	}
}
