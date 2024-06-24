package com.aflac.core.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import com.aflac.apiworkflow.models.CustomFileNetResponse;
import com.aflac.core.experience.api.model.ApiMasterAppData;
import com.aflac.core.experience.api.model.MasterAppReviewTableApi;
import com.aflac.core.experience.api.model.MasterAppReviewTableDataApi;
import com.aflac.core.experience.api.model.SaveCasebuildResponse;
import com.aflac.core.osgi.service.MasterAppService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(property = { Constants.SERVICE_DESCRIPTION + "=Custom component to save filenet Id, agreement Id to dynamo db.",
		Constants.SERVICE_VENDOR + "=Adobe Systems",
		"process.label" + "=Custom component to save filenet Id, agreement Id to dynamo db" })
public class saveDocumentIdsWorkflowStep implements WorkflowProcess {

	private static final Logger log = LoggerFactory.getLogger(saveDocumentIdsWorkflowStep.class);
	
	@Reference
	private transient MasterAppService masterAppService;
	
	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap metaDataMap) throws WorkflowException {
		log.info("------- SaveDocumentIdsWorkflowStep method called-----");
		// Read the value from the dialogue box
		String masterAppIdVariableName = metaDataMap.get("masterAppIdVariable", String.class);
		String masterAppVersionVariableName = metaDataMap.get("masterAppVersionVariable", String.class);
		String formIdVariableName  = metaDataMap.get("formIdVariableName", String.class);
		String filenetIdVariableName  = metaDataMap.get("filenetIdVariable", String.class);
		String agreementIdVariableName  = metaDataMap.get("agreementIdVarName", String.class);
		String saveStatusVariableName = metaDataMap.get("saveStatusVar", String.class);
		
		WorkflowData data = item.getWorkflowData();
		MetaDataMap metaData = data.getMetaDataMap();
		
		String masterAppId = metaData.get(masterAppIdVariableName,String.class);
		String version = metaData.get(masterAppVersionVariableName,String.class);
		String[] formIds = metaData.get(formIdVariableName, String[].class);
		String filenetDocIdJson = metaData.get(filenetIdVariableName,String.class);
		String agreementId = metaData.get(agreementIdVariableName,String.class);
		
		String res = updateDocIdsAndStatus(masterAppId, version, formIds, filenetDocIdJson, agreementId, null, masterAppService);
		metaData.put(saveStatusVariableName, res);
	}
	
	public String updateDocIdsAndStatus(String masterAppId, String version, String[] formIds, 
			String filenetDocIdJson, String agreementId, String signStatus, MasterAppService masterAppSer) {
		ObjectMapper Obj = new ObjectMapper();
		
		if(masterAppId!=null && version != null && !masterAppId.isEmpty() && !version.isEmpty()) {
			//saving status update
			ApiMasterAppData apiMasterAppData = new ApiMasterAppData();
			apiMasterAppData.setMasterAppEntityId(masterAppId);
			apiMasterAppData.setVersion(Integer.parseInt(version));
			apiMasterAppData.setForStatusUpdate(true);
			CustomFileNetResponse res;
			try {
				List<String> filenetIds = new ArrayList<>();
				if(filenetDocIdJson != null && !filenetDocIdJson.isEmpty()) {
					res = Obj.readValue(filenetDocIdJson, CustomFileNetResponse.class);
					filenetIds = res.getFilenetIds().stream().filter(d -> Arrays.asList(formIds).contains(d.getTitle()))
								.map(d -> d.getDocumentId()).collect(Collectors.toList());
				}
				List<MasterAppReviewTableDataApi> masterAppReviewTableData = new ArrayList<>();
				for(int i=0; i<formIds.length; i++) {
					MasterAppReviewTableDataApi tmp = new MasterAppReviewTableDataApi();
					tmp.setFormId(formIds[i].replace(".pdf", ""));
					if(filenetDocIdJson != null && !filenetDocIdJson.isEmpty())
						tmp.setFilenetDocumentId(filenetIds.get(i));
					if(agreementId != null && !agreementId.isEmpty())
						tmp.setAgreementId(agreementId);
					if(signStatus != null && !signStatus.isEmpty())
						tmp.setSignStatus(signStatus);
					masterAppReviewTableData.add(tmp);
				}
				MasterAppReviewTableApi table = new MasterAppReviewTableApi();
				table.setMasterAppReviewTableData(masterAppReviewTableData);
				apiMasterAppData.setMasterAppReviewTable(table);
				
				SaveCasebuildResponse saveMasterAppResponse = masterAppSer.saveMasterAppData(apiMasterAppData, null, "");
				
				log.info("====> Filenet Id or Agreement Id save status Response: " + saveMasterAppResponse.getMessage());
				return saveMasterAppResponse.getMessage();
			} catch (JsonProcessingException e) {
				log.error("Error: ", e);
			}
		}
		return "";
	}
}
