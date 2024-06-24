package com.aflac.core.osgi.service.impl;

import static org.mockito.Mockito.when;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.model.WorkflowModel;
import com.aflac.core.services.GetAttachmentDocsWorkflowStep;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
@ExtendWith(MockitoExtension.class)
public class GetAttachmentDocsWorkflowStepTest {

	private static GetAttachmentDocsWorkflowStep getAttachmentDocsWorkflowStep;
	
	@Mock
	private WorkflowSession workflowSession;

	@Mock
	private Workflow workflow;

	@Mock
	private WorkflowData workflowData;

	@Mock
	private WorkItem workItem;
	
	@Mock
	private MetaDataMap metaDataMap;
	
	@Mock
	private WorkflowModel workflowModel;
	
	@Mock
	private CloseableHttpClient closeableHttpClient;
	
	@Mock
	private CloseableHttpResponse closeableHttpResponse;
	
	@Mock
	private ResourceResolver resolver;
	
	@Mock
	private Resource resource;
	
	
	@BeforeAll
	public static void setup() {
		try {
	        getAttachmentDocsWorkflowStep = new GetAttachmentDocsWorkflowStep();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void GetAttachmentDocsWorkflowStep() {
		try {
			when(metaDataMap.get("att1VarName", String.class)).thenReturn("att1VarName");
			when(metaDataMap.get("att2VarName", String.class)).thenReturn("att2VarName");
			when(metaDataMap.get("selectedAttVarName", String.class)).thenReturn("selectedAttVarName");
			when(metaDataMap.get("attPdfsVarName", String.class)).thenReturn("attPdfsVarName");
			when(workItem.getWorkflowData()).thenReturn(workflowData);
			when(workflowData.getMetaDataMap()).thenReturn(metaDataMap);
			when(metaDataMap.get("att1VarName",String.class)).thenReturn("att1VarName");
			when(metaDataMap.get("att2VarName",String.class)).thenReturn("att2VarName");
			when(metaDataMap.get("selectedAttVarName",String.class)).thenReturn("selectedAttVarName");
			when(metaDataMap.get("attPdfsVarName",String.class)).thenReturn("attPdfsVarName");
			when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(resolver);
			when(resolver.getResource("/content/dam/aflacapps/master-app-attchments/att1VarName")).thenReturn(resource);
			when(resolver.getResource("/content/dam/aflacapps/master-app-attchments/att2VarName")).thenReturn(resource);
			getAttachmentDocsWorkflowStep.execute(workItem, workflowSession, metaDataMap);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
