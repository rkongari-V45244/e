package com.aflac.core.services;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component(property = { Constants.SERVICE_DESCRIPTION + "=Custom component WaitForSignatureCompletion.",
		Constants.SERVICE_VENDOR + "=Adobe Systems",
		"process.label" + "=Custom component WaitForSignatureCompletion" })
public class WaitForSignatureCompletion implements WorkflowProcess {
	
	private static final Logger log = LoggerFactory.getLogger(WaitForSignatureCompletion.class);

	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap metaDataMap) throws WorkflowException {
		log.info("=============== WaitForSignatureCompletion........");

	}
	

}
