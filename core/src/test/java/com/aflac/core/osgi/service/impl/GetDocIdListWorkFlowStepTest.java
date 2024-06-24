package com.aflac.core.osgi.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
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
import com.aflac.core.config.AdobeSignConfig;
import com.aflac.core.services.GetDocIdListWorkFlowStep;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
@ExtendWith(MockitoExtension.class)
public class GetDocIdListWorkFlowStepTest {

	private static GetDocIdListWorkFlowStep getDocIdListWorkFlowStep;
	
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
	private AdobeSignConfig adobeSignConfig;
	
	@Mock
	private CloseableHttpClient httpClient;
	
	
	@Test
	public void GetDocIdListWorkFlowStep() {
		try {
			String response = "{\"documents\":\"TestDocument\"}";
			setUpClient(closeableHttpClient, closeableHttpResponse, response);
			StatusLine status = mock(StatusLine.class);
	        when(closeableHttpResponse.getStatusLine()).thenReturn(status);
	        when(status.getStatusCode()).thenReturn(200);
			getDocIdListWorkFlowStep = new GetDocIdListWorkFlowStep(httpClient,adobeSignConfig);
			when(metaDataMap.get("agreementIdVarName", String.class)).thenReturn("123");
			when(metaDataMap.get("docIdListVariableName", String.class)).thenReturn("docIdListVariableName");
			when(workItem.getWorkflowData()).thenReturn(workflowData);
			when(workflowData.getMetaDataMap()).thenReturn(metaDataMap);
			when(metaDataMap.get("123",String.class)).thenReturn("123");
			getDocIdListWorkFlowStep.execute(workItem, workflowSession, metaDataMap);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void setUpClient(CloseableHttpClient closeableHttpClient, CloseableHttpResponse closeableHttpResponse,String response) throws NoSuchFieldException,IOException{
        InputStream is = new ByteArrayInputStream(response.getBytes());
        HttpEntity httpEntity = mock(HttpEntity.class); 
        when(httpEntity.getContent()).thenReturn(is);
        when(closeableHttpResponse.getEntity()).thenReturn(httpEntity);
        when(httpClient.execute(any())).thenReturn(closeableHttpResponse);
   }
}
