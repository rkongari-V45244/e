package com.aflac.core.services;

import java.util.ArrayList;
import java.util.List;

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
import com.aflac.core.experience.api.model.ApiMasterAppData;
import com.aflac.core.experience.api.model.MasterAppReviewTableApi;
import com.aflac.core.experience.api.model.MasterAppReviewTableDataApi;
import com.aflac.core.experience.api.model.SaveCasebuildResponse;
import com.aflac.core.osgi.service.MasterAppService;
import com.aflac.core.util.AflacAppsHelper;

@Component(property = { Constants.SERVICE_DESCRIPTION + "=Custom component to update status of pdf signature.",
		Constants.SERVICE_VENDOR + "=Adobe Systems",
		"process.label" + "=Custom component to update status" })
public class PDFStatusUpdateWorkflowStep implements WorkflowProcess {

	private static final Logger log = LoggerFactory.getLogger(PDFStatusUpdateWorkflowStep.class);
		
	@Reference
	private transient MasterAppService masterAppService;
	
	@Reference
	AflacAppsHelper aflacAppsHelper;
	
	public PDFStatusUpdateWorkflowStep() {}

	public PDFStatusUpdateWorkflowStep(MasterAppService masterAppService, AflacAppsHelper aflacAppsHelper) {
		this.masterAppService = masterAppService;
		this.aflacAppsHelper = aflacAppsHelper;
	}




	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap metaDataMap) throws WorkflowException {
		log.info("------- PDF Status update method called...");
		// Read the value from the dialogue box
		String masterAppIdVariableName = metaDataMap.get("masterAppIdVariable", String.class);
		String masterAppVersionVariableName = metaDataMap.get("masterAppVersionVariable", String.class);
		String statusVariableName  = metaDataMap.get("statusVariableName", String.class);
		String formIdVariableName  = metaDataMap.get("formIdVariableName", String.class);
		WorkflowData data = item.getWorkflowData();
		MetaDataMap metaData = data.getMetaDataMap();
		
		String masterAppId = metaData.get(masterAppIdVariableName,String.class);
		String version = metaData.get(masterAppVersionVariableName,String.class);
		String status = metaData.get(statusVariableName,String.class);
		String[] formIds = metaData.get(formIdVariableName, String[].class);
		log.info("---- Status to be updated: " + status);
		if(masterAppId!=null && version != null && !masterAppId.isEmpty() && !version.isEmpty()) {
			//saving status update
			aflacAppsHelper.updateMasterAppTableData(masterAppId,version,status,formIds);
//			ApiMasterAppData apiMasterAppData = new ApiMasterAppData();
//			apiMasterAppData.setMasterAppEntityId(masterAppId);
//			apiMasterAppData.setVersion(Integer.parseInt(version));
//			apiMasterAppData.setForStatusUpdate(true);
//			List<MasterAppReviewTableDataApi> masterAppReviewTableData = new ArrayList<>();
//			for(int i=0; i<formIds.length; i++) {
//				MasterAppReviewTableDataApi tmp = new MasterAppReviewTableDataApi();
//				tmp.setFormId(formIds[i].replace(".pdf", ""));
//				tmp.setSignStatus(status);
//				masterAppReviewTableData.add(tmp);
//			}
//			MasterAppReviewTableApi table = new MasterAppReviewTableApi();
//			table.setMasterAppReviewTableData(masterAppReviewTableData);
//			apiMasterAppData.setMasterAppReviewTable(table);
//			//apiMasterAppData.setMasterappReceivedDatetime(LocalDateTime.now().toString());
//			//apiMasterAppData.setMasterappSentDatetime(LocalDateTime.now().toString());
//			
//			SaveCasebuildResponse saveMasterAppResponse = masterAppService.saveMasterAppData(apiMasterAppData, null, "");
//			log.info("====> Status Update Response: " + saveMasterAppResponse.getMessage());
		}
	}
}
