//package com.aflac.core.osgi.service.impl;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Map;
//import javax.jcr.Session;
//import static org.mockito.ArgumentMatchers.any;
//import org.apache.http.HttpEntity;
//import org.apache.http.StatusLine;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.sling.api.SlingHttpServletRequest;
//import org.apache.sling.api.SlingHttpServletResponse;
//import org.apache.sling.api.resource.Resource;
//import org.apache.sling.api.resource.ResourceResolver;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//
//import com.adobe.granite.workflow.WorkflowException;
//import com.adobe.granite.workflow.WorkflowSession;
//import com.adobe.granite.workflow.exec.WorkItem;
//import com.adobe.granite.workflow.exec.Workflow;
//import com.adobe.granite.workflow.exec.WorkflowData;
//import com.adobe.granite.workflow.model.VariableTemplate;
//import com.adobe.granite.workflow.model.WorkflowModel;
//import com.adobe.granite.workflow.payload.PayloadInfo;
//import com.aflac.core.config.AdobeSignConfig;
//import com.aflac.core.services.VoidAgreementWorkflowStep;
//import com.aflac.core.servlets.VoidAgreementServlet;
//import io.wcm.testing.mock.aem.junit5.AemContext;
//import io.wcm.testing.mock.aem.junit5.AemContextExtension;
//import com.adobe.granite.workflow.metadata.MetaDataMap;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(AemContextExtension.class)
//@ExtendWith(MockitoExtension.class)
//public class VoidAgreementServletTest {
//	
//	private static AemContext aemContext;
//	private static VoidAgreementServlet voidAgreementServlet;
//	
//	@Mock
//	private WorkflowSession workflowSession;
//
//	@Mock
//	private Workflow workflow;
//
//	@Mock
//	private WorkflowData workflowData;
//
//	@Mock
//	private WorkItem workItem;
//	
//	@Mock
//	private MetaDataMap metaDataMap;
//	
//	@Mock
//	private WorkflowModel workflowModel;
//	
//	@Mock
//	private AdobeSignConfig adobeSignConfig;
//	
//	@Mock
//	private CloseableHttpClient httpClient;
//	
//	@Mock
//	private CloseableHttpClient closeableHttpClient;
//	
//	@Mock
//	private CloseableHttpResponse closeableHttpResponse;
//
//	@BeforeAll
//	public static void setup() {
//		voidAgreementServlet = new VoidAgreementServlet();
//	}
//	
//	@Test
//	public void voidAgreementServlet() {
//		String response ="{\"metadata\":{\"status\":\"success\",\"code\":\"OK\",\"descriptions\":[{\"context\":\"success\",\"type\":\"info\",\"code\":\"200 OK\",\"short-description\":\"generic.success.message\",\"long-description\":\"Request completed successfully.\"}]},\"data\":{\"master-app-entity-id\":\"GP-2#2024-03-07\",\"version\":2,\"situs-state\":\"AL\",\"group-type\":\"Employer Group\",\"census-enrollement\":null,\"eligible-series\":\"9100,\",\"forms-ids\":null,\"account-eligibility\":{\"group-number-or-gp-id\":\"GP-2\",\"effective-date\":\"2024-03-07\",\"organization-name\":\"sasda\",\"city-of-hq\":\"assa\",\"subsidiaries-affiliates\":\"sasda1\",\"cert-holders\":\"Employee\",\"hour-per-week-full-time\":12,\"hour-per-week-part-time\":null,\"enroll-employee-count\":25,\"eligible-employee-count\":12,\"other-requirement\":\"sdsad\",\"purpose-of-application\":null,\"classes-added-or-removed\":null,\"policy-numbers\":null,\"applicant\":null,\"applicant-other\":null,\"sic-code\":null,\"address\":null,\"number-or-street\":null,\"city\":\"assa\",\"state\":null,\"zip\":null,\"physical-address\":null,\"physical-number-or-street\":null,\"physical-city\":null,\"physical-state\":null,\"physical-zip\":null,\"authorized-person-name\":null,\"authorized-person-title\":null,\"contact-name\":null,\"contact-title\":null,\"contact-phone\":null,\"contact-email\":null,\"erisa-plan\":null,\"plan-number\":null,\"sad-to-be-covered\":null,\"sad-names\":null,\"first-of-the-month\":\"Yes\",\"full-time-or-part-time\":\"No\",\"full-time-eligible-coverage-duration\":\"12\",\"part-time-eligible-coverage-duration\":null,\"full-time-eligible-coverage-month\":\"Days\",\"part-time-eligible-coverage-month\":null},\"product\":[{\"product-name\":\"TL9100\",\"benefit-type\":null,\"application-reason\":\"New Policy\",\"eligible-employee\":null,\"eligible-employee-except\":null,\"plan\":\"1\",\"optional-features\":null,\"optional-features-other\":null,\"effective-date\":\"2024-03-07\",\"rates\":\"12\",\"employee-premium\":\"100\",\"employer-premium\":\"0\",\"is-replacing-group-policy\":false,\"series\":\"9100\",\"eligible-spouse\":null,\"eligible-employee-other\":null,\"eligible-employee-class\":null,\"product-class\":null,\"plan-features\":null,\"plan-features-other\":null,\"plan-level-premium\":\"5-Year Term\",\"product-sequence\":6,\"optional-features-text\":\"ssdsaas\",\"benefit-period\":null,\"elimination-period\":null,\"income-replacement\":null,\"carrier-policy-number\":null,\"group-policy-state\":\"AL\",\"optional-term-life-coverages\":null,\"ciac\":null,\"proposed-effective-date\":null,\"on-date\":\"2024-03-07\",\"minimum-no-of-employees\":null,\"percentile-of-eligible-employees\":null,\"premium-paid\":null,\"product-contribution\":\"100% Voluntary\",\"premium-payment-mode\":null,\"employee-cost-of-insurance\":null,\"actively-at-work\":{\"active-at-work-data\":[{\"class-name\":\"ssc\",\"actively-at-work-hours\":12}]},\"waiting-period-eligibility\":{\"waiting-period-eligibility-data\":[{\"class-name\":\"ccz\",\"waiting-period\":\"after Active Employment\",\"number-Of-days\":\"30\"}]},\"existing-policy-number\":null,\"other-application-reason\":null,\"hi-enroll-employee-count\":null,\"plan-year-per-insured\":null,\"premium-due\":null,\"pre-existing-condition-limitation\":null,\"preceeding-the-CED-months\":0,\"following-the-CED-months\":0}],\"for-status-update\":false,\"master-app-review-table\":{\"master-app-review-table-data\":[{\"master-app-entity-id-with-version\":null,\"signature-status\":\"Doc Not Generated\",\"filenet-document-id\":null,\"products\":\"TL9100\",\"form-id\":\"C03204\",\"api-form-id\":\"C03204AL\",\"signature-checkbox\":\"-100\",\"agreement-id\":null,\"masterapp-sent-datetime\":null,\"masterapp-received-datetime\":null}]},\"first-signer-email-id\":null,\"second-signer-email-id\":null,\"data-status\":\"Data Saved # Thu, 07 Mar 2024 10:37:45 GMT\"}}";
//		SlingHttpServletRequest slingHttpServletRequest=mock(SlingHttpServletRequest.class);
//    	SlingHttpServletResponse slingHttpServletResponse = mock(SlingHttpServletResponse.class);
//    	Map<String, VariableTemplate> map = new HashMap<String, VariableTemplate>();
//    	VariableTemplate masterAppId = mock(VariableTemplate.class);
//    	VariableTemplate coverageEffectiveDate = mock(VariableTemplate.class);
//    	VariableTemplate formId = mock(VariableTemplate.class);
//    	VariableTemplate agreementId = mock(VariableTemplate.class);
//    	VariableTemplate masterAppVersion = mock(VariableTemplate.class);
//    	map.put("masterAppId", masterAppId);
//    	map.put("coverageEffectiveDate", coverageEffectiveDate);
//    	map.put("formId", formId);
//    	map.put("agreementId", agreementId);
//    	map.put("masterAppVersion", masterAppVersion);
//		try {
//			when(slingHttpServletRequest.getParameter("masterAppId")).thenReturn("1234");
//			when(slingHttpServletRequest.getParameter("coverageEffectiveDate")).thenReturn("2024-01-31");
//			when(slingHttpServletRequest.getParameter("formId")).thenReturn("C02204");
//			when(slingHttpServletRequest.getParameter("agreementId")).thenReturn("vsvsdfsf");
//			when(slingHttpServletRequest.getParameter("masterAppVersion")).thenReturn("1");
//			
//			ResourceResolver resolver = mock(ResourceResolver.class);
//			Session session = mock(Session.class);
//			when(slingHttpServletRequest.getResourceResolver()).thenReturn(resolver);
//		    when(resolver.adaptTo(Session.class)).thenReturn(session);
//		    WorkflowSession workflowSession = mock(WorkflowSession.class);
//		    when(resolver.adaptTo(WorkflowSession.class)).thenReturn(workflowSession);
//		    WorkflowModel wfModel = mock(WorkflowModel.class);
//		    when(workflowSession.getModel("/var/workflow/models/Void-Agreement-Workflow")).thenReturn(wfModel);
//		    Resource resourse = mock(Resource.class);
//		    when(slingHttpServletRequest.getResource()).thenReturn(resourse);
//		    when(resourse.getPath()).thenReturn("Test/bin");
//		    when(wfModel.getId()).thenReturn("1");
//		    
//		    when(wfModel.getVariableTemplates()).thenReturn(map);
//		    
//		    when(map.get("masterAppId").getName()).thenReturn("masterAppId");
//		    when(map.get("coverageEffectiveDate").getName()).thenReturn("coverageEffectiveDate");
//		    when(map.get("formId").getName()).thenReturn("formId");
//		    when(map.get("agreementId").getName()).thenReturn("agreementId");
//		    when(map.get("masterAppVersion").getName()).thenReturn("masterAppVersion");
//		    WorkflowData wfData = mock(WorkflowData.class);
//		    MetaDataMap metaDataMap = mock(MetaDataMap.class);
//		    when(wfData.getMetaDataMap()).thenReturn(metaDataMap);
//		    when(workflowSession.newWorkflowData(PayloadInfo.PAYLOAD_TYPE.JCR_PATH.name(),resourse.getPath().split("/bin")[0] + wfModel.getId())).thenReturn(wfData);
//			voidAgreementServlet.doGet(slingHttpServletRequest, slingHttpServletResponse);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void voidAgreementWorkflowStep() throws WorkflowException {
//		try {
//			String response = "{\"result\":\"CANCELLED\"}";
//			setUpClient(closeableHttpClient, closeableHttpResponse, response);
//			StatusLine status = mock(StatusLine.class);
//	        when(closeableHttpResponse.getStatusLine()).thenReturn(status);
//	        when(status.getStatusCode()).thenReturn(200);
//			Map<String, VariableTemplate> map = new HashMap<String, VariableTemplate>();
//			VariableTemplate voidStatus = mock(VariableTemplate.class);
//			map.put("voidStatus", voidStatus);
//			VoidAgreementWorkflowStep voidAgreementWorkflowStep = new VoidAgreementWorkflowStep(adobeSignConfig,httpClient);
//			when(workItem.getWorkflowData()).thenReturn(workflowData);
//			when(workflowData.getMetaDataMap()).thenReturn(metaDataMap);
//			when(metaDataMap.get("agreementId")).thenReturn("123");
//			when(adobeSignConfig.getAdobeSignApiUrl()).thenReturn("/abc");
//			when(workflowSession.getModel("/var/workflow/models/Void-Agreement-Workflow")).thenReturn(workflowModel);
//			when(workflowModel.getVariableTemplates()).thenReturn(map);
//			when(map.get("voidStatus").getName()).thenReturn("CANCELLED");
//			voidAgreementWorkflowStep.execute(workItem, workflowSession, metaDataMap);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//	
//	private void setUpClient(CloseableHttpClient closeableHttpClient, CloseableHttpResponse closeableHttpResponse,String response) throws NoSuchFieldException,IOException{
//        InputStream is = new ByteArrayInputStream(response.getBytes());
//        HttpEntity httpEntity = mock(HttpEntity.class); 
//        when(httpEntity.getContent()).thenReturn(is);
//        when(closeableHttpResponse.getEntity()).thenReturn(httpEntity);
//        when(httpClient.execute(any())).thenReturn(closeableHttpResponse);
//   }
//}
