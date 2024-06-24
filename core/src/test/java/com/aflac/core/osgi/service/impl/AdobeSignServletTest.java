//package com.aflac.core.osgi.service.impl;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import java.io.PrintWriter;
//import java.util.HashMap;
//import java.util.Map;
//import javax.jcr.Session;
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
//import org.mockito.junit.jupiter.MockitoExtension;
//import com.adobe.granite.workflow.WorkflowSession;
//import com.adobe.granite.workflow.exec.WorkItem;
//import com.adobe.granite.workflow.exec.Workflow;
//import com.adobe.granite.workflow.exec.WorkflowData;
//import com.adobe.granite.workflow.metadata.MetaDataMap;
//import com.adobe.granite.workflow.model.VariableTemplate;
//import com.adobe.granite.workflow.model.WorkflowModel;
//import com.adobe.granite.workflow.payload.PayloadInfo;
//import com.aflac.core.config.AdobeSignConfig;
//import com.aflac.core.servlets.AdobeSignWorkflowServlet;
//import io.wcm.testing.mock.aem.junit5.AemContextExtension;
//
//@ExtendWith(AemContextExtension.class)
//@ExtendWith(MockitoExtension.class)
//public class AdobeSignServletTest {
//
//	private static AdobeSignWorkflowServlet adobeSignWorkflowServlet;
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
//	@Mock
//	private PrintWriter printWriter;
//
//	@BeforeAll
//	public static void setup() {
//		adobeSignWorkflowServlet = new AdobeSignWorkflowServlet();
//	}
//	
//	@Test
//	public void adobeSignServlet() {
//		SlingHttpServletRequest slingHttpServletRequest=mock(SlingHttpServletRequest.class);
//    	SlingHttpServletResponse slingHttpServletResponse = mock(SlingHttpServletResponse.class);
//    	Map<String, VariableTemplate> map = new HashMap<String, VariableTemplate>();
//    	VariableTemplate formXmlData = mock(VariableTemplate.class);
//    	map.put("formXmlData", formXmlData);
//		try {
//			when(slingHttpServletRequest.getParameter("groupNumber")).thenReturn("1234");
//			when(slingHttpServletRequest.getParameter("coverageEffectiveDate")).thenReturn("2024-01-31");
//			when(slingHttpServletRequest.getParameter("formData")).thenReturn("<afData><afUnboundData><data><GroupNoRadioButton>GP-ID</GroupNoRadioButton><masterAppGroupNo>GP-123</masterAppGroupNo><coverageEffDateMasterApp>2023-12-15</coverageEffDateMasterApp><masterAppFormCase>Add</masterAppFormCase><employeeType>full-time</employeeType><exceptionRadio>No</exceptionRadio></data></afUnboundData><afBoundData><masterApp xmlns:xfa=\\\"http://www.xfa.org/schema/xfa-data/1.0/\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance/\\\"><MetaData><title/><masterAppId/></MetaData><masterAppEntityId/><version/><situsState>AL</situsState><groupType>Employer Group</groupType><censusEnrollment/><eligible-series>7700</eligible-series><formIds/><AccountAndEligiblity><groupNumberOrGpId>GP-123</groupNumberOrGpId><effectiveDate>2023-12-15</effectiveDate><organizationName>Test</organizationName><certHolders>Employee</certHolders><hourPerWeekFullTime>34</hourPerWeekFullTime><hourPerWeekpartTime>45</hourPerWeekpartTime><eligibleCoverageMonth>56-months</eligibleCoverageMonth><cityOfHq>NY</cityOfHq><subsidiariesAffiliates>subs</subsidiariesAffiliates><enrollEmployeeCount>25</enrollEmployeeCount><eligibleEmployeeCount>100</eligibleEmployeeCount><otherRequirement>others</otherRequirement><erisaPlan>No</erisaPlan><churchPlan>No</churchPlan></AccountAndEligiblity><Product><productName>TL9100</productName><productSequence>6</productSequence><series>9100</series><planLevelPremium>5-Year Term</planLevelPremium><applicationReason>New Policy</applicationReason><ActivelyAtWork><ActiveAtWorkData><className>ssc</className><activelyAtWorkHours>12</activelyAtWorkHours></ActiveAtWorkData></ActivelyAtWork><WaitingPeriodEligibility><WaitingPeriodEligibilityData><className>ccz</className><waitingPeriod>after Active Employment</waitingPeriod><numberOfDays>30</numberOfDays></WaitingPeriodEligibilityData></WaitingPeriodEligibility><optionalFeaturesText>ssdsaas</optionalFeaturesText><plan>1</plan><effectiveDate>2024-03-07</effectiveDate><rates>12</rates><productContribution>100% Voluntary</productContribution><employeePremium>100</employeePremium><employerPremium>0</employerPremium><groupPolicyState>AL</groupPolicyState><onDate>2024-03-07</onDate></Product><MasterAppReviewTable><MasterAppReviewTableData><formId>C03204</formId><apiFormId>C03204AL</apiFormId><product>TL9100</product><signStatus>Doc Not Generated</signStatus><signatureCheckbox>0</signatureCheckbox><masterappSentDatetime/><masterappReceivedDatetime/><filenetDocumentId/><agreementId/></MasterAppReviewTableData></MasterAppReviewTable></masterApp></afBoundData><afSubmissionInfo><lastFocusItem>guide[0].guide1[0].guideRootPanel[0].masterAppOuterPanel[0].MasterAppDetails[0].reviewSection[0]</lastFocusItem><stateOverrides/><signers/></afSubmissionInfo></afData>");
//			when(slingHttpServletResponse.getWriter()).thenReturn(printWriter);
//			ResourceResolver resolver = mock(ResourceResolver.class);
//			Session session = mock(Session.class);
//			when(slingHttpServletRequest.getResourceResolver()).thenReturn(resolver);
//		    when(resolver.adaptTo(Session.class)).thenReturn(session);
//		    WorkflowSession workflowSession = mock(WorkflowSession.class);
//		    when(resolver.adaptTo(WorkflowSession.class)).thenReturn(workflowSession);
//		    WorkflowModel wfModel = mock(WorkflowModel.class);
//		    when(workflowSession.getModel("/var/workflow/models/masterapp-signature-wf1")).thenReturn(wfModel);
//		    Resource resourse = mock(Resource.class);
//		    when(slingHttpServletRequest.getResource()).thenReturn(resourse);
//		    when(resourse.getPath()).thenReturn("Test/bin");
//		    when(wfModel.getId()).thenReturn("1");
//		    when(wfModel.getVariableTemplates()).thenReturn(map);
//		    when(map.get("formXmlData").getName()).thenReturn("formXmlData");
//		    WorkflowData wfData = mock(WorkflowData.class);
//		    MetaDataMap metaDataMap = mock(MetaDataMap.class);
//		    when(wfData.getMetaDataMap()).thenReturn(metaDataMap);
//		    when(workflowSession.newWorkflowData(PayloadInfo.PAYLOAD_TYPE.JCR_PATH.name(),resourse.getPath().split("/bin")[0] + wfModel.getId())).thenReturn(wfData);
//		    adobeSignWorkflowServlet.doPost(slingHttpServletRequest, slingHttpServletResponse);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
//}
