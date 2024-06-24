package com.aflac.core.services;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
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
import com.aflac.core.config.AdobePDFApiIntegrationConfig;
import com.day.cq.dam.api.Asset;

@Component(property = { Constants.SERVICE_DESCRIPTION + "=Custom component to MASTERAPP - Get Attachments PDFs.",
		Constants.SERVICE_VENDOR + "=Adobe Systems",
		"process.label" + "=Custom component to MASTERAPP - Get Attachments PDFs" })
public class GetAttachmentDocsWorkflowStep implements WorkflowProcess {
	
	private static final Logger log = LoggerFactory.getLogger(GetAttachmentDocsWorkflowStep.class);
	
	@Reference
	private transient AdobePDFApiIntegrationConfig getCredentials;
		
	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap metaDataMap) throws WorkflowException {
		log.info("Get Attachments method...");
		String attLocation = "/content/dam/aflacapps/master-app-attchments/";
		String att1VarName = metaDataMap.get("att1VarName", String.class);
		String att2VarName = metaDataMap.get("att2VarName", String.class);
		String selectedAttVarName = metaDataMap.get("selectedAttVarName", String.class);
		String attPdfsVarName = metaDataMap.get("attPdfsVarName", String.class);
		
		WorkflowData data = item.getWorkflowData();
		MetaDataMap metaData = data.getMetaDataMap();
				
		String att1 = metaData.get(att1VarName,String.class);
		String att2 = metaData.get(att2VarName,String.class);
		List<String> attList = new ArrayList<>();
		if(att1 != null && !att1.isEmpty())
			attList.add(att1);
		if(att2 != null && !att2.isEmpty())
			attList.add(att2);
		Document[] docs = new Document[attList.size()];
		log.info("docs size: " + attList.size());
		for(int i=0; i < attList.size(); i++) {
			ResourceResolver resolver = session.adaptTo(ResourceResolver.class);		
			String att1Path = attLocation + attList.get(i);
			Resource resourseAtt = resolver.getResource(att1Path);
			log.info("Attachment ==>" + resourseAtt);
			Asset asset = resourseAtt.adaptTo(Asset.class);
			if(asset != null) {
				Resource original = asset.getOriginal();
				InputStream attIs = original.adaptTo(InputStream.class);
				Document doc = new Document(attIs);
				docs[i] = doc;
			}
			
		}
				
		metaData.put(attPdfsVarName, docs);
		String[] attNames = {att1, att2};
		metaData.put(selectedAttVarName, attNames);
	}	
}
