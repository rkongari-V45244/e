package com.aflac.core.osgi.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
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
import com.aflac.core.config.AdobePDFApiIntegrationConfig;
import com.aflac.core.config.ExperienceApiConfig;
import com.aflac.core.config.FileNetApiConfig;
import com.aflac.core.config.impl.AdobePDFApiIntegrationConfigImpl;
import com.aflac.core.config.impl.ExperienceApiConfigImpl;
import com.aflac.core.config.impl.FileNetApiConfigImpl;
import com.aflac.core.osgi.service.AuditService;
import com.aflac.core.osgi.service.CaseBuilderService;
import com.aflac.core.osgi.service.ComplianceService;
import com.aflac.core.osgi.service.FileNetService;
import com.aflac.core.osgi.service.GeneratePDFService;
import com.aflac.core.osgi.service.MasterAppService;
import com.aflac.core.services.PDFStatusUpdateWorkflowStep;
import com.aflac.core.util.AflacAppsHelper;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
@ExtendWith(MockitoExtension.class)
public class PDFStatusUpdateWorkflowStepTest {

	private static PDFStatusUpdateWorkflowStep pdfStatusUpdateWorkflowStep;
	private static AemContext aemContext;
	private static ExperienceApiConfig apiUrlService;
	private static GeneratePDFService pdfService;
	private static MasterAppService masterAppService;
	private static CaseBuilderService caseService;
	private static ComplianceService complianceService;
	private static FileNetApiConfig fileNetConfig;
	private static FileNetService fileNet;
	private static AdobePDFApiIntegrationConfig getCredentials;
	private static AuditService auditService;
	private static AflacAppsHelper aflacAppsHelper;
	private static CloseableHttpClient httpClient;
	
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
	
	
	@BeforeAll
	public static void setup() {
		try {
			aemContext = new AemContext();
			httpClient = mock(CloseableHttpClient.class);
			apiUrlService = aemContext.registerInjectActivateService(new ExperienceApiConfigImpl());
			auditService = aemContext.registerInjectActivateService(new AuditServiceImpl());
			caseService = aemContext.registerInjectActivateService(new CaseBuilderServiceImpl(httpClient,apiUrlService, auditService));
			complianceService = aemContext.registerInjectActivateService(new ComplianceServiceImpl(httpClient,apiUrlService));
			fileNetConfig = aemContext.registerInjectActivateService(new FileNetApiConfigImpl());
			fileNet = aemContext.registerInjectActivateService(new FileNetServiceImpl(apiUrlService, fileNetConfig));
			getCredentials = aemContext.registerInjectActivateService(new AdobePDFApiIntegrationConfigImpl());
			pdfService = aemContext.registerInjectActivateService(new GeneratePDFServiceImpl(httpClient, getCredentials, caseService, complianceService, fileNetConfig, fileNet));
	        masterAppService = aemContext.registerInjectActivateService(new MasterAppServiceImpl(httpClient,apiUrlService, pdfService));
	        aflacAppsHelper = aemContext.registerInjectActivateService(new AflacAppsHelper());
			pdfStatusUpdateWorkflowStep = new PDFStatusUpdateWorkflowStep(masterAppService,aflacAppsHelper);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void pdfStatusUpdate() {
		try {
			String response ="{\"metadata\":{\"status\":\"success\",\"code\":\"CREATED\",\"descriptions\":[{\"context\":\"success\",\"type\":\"info\",\"code\":\"201 CREATED\",\"short-description\":\"generic.success.message\",\"long-description\":\"Request completed successfully.\"}]},\"data\":{\"status\":true,\"validation-status\":\"Success\"}}";
			setUpClient(closeableHttpClient, closeableHttpResponse, response);
			StatusLine status = mock(StatusLine.class);
	        when(closeableHttpResponse.getStatusLine()).thenReturn(status);
	        when(status.getStatusCode()).thenReturn(200);
			when(metaDataMap.get("masterAppIdVariable", String.class)).thenReturn("123");
			when(metaDataMap.get("masterAppVersionVariable", String.class)).thenReturn("1");
			when(metaDataMap.get("statusVariableName", String.class)).thenReturn("SIGNED");
			when(metaDataMap.get("formIdVariableName", String.class)).thenReturn("C02204");
			when(workItem.getWorkflowData()).thenReturn(workflowData);
			when(workflowData.getMetaDataMap()).thenReturn(metaDataMap);
			when(metaDataMap.get("1",String.class)).thenReturn("1");
			when(metaDataMap.get("123",String.class)).thenReturn("123");
			when(metaDataMap.get("SIGNED",String.class)).thenReturn("SIGNED");
			when(metaDataMap.get("C02204",String[].class)).thenReturn(new String[] {"C02204"});
			
			pdfStatusUpdateWorkflowStep.execute(workItem, workflowSession, metaDataMap);
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
