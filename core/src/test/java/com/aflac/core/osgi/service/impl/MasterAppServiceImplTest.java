package com.aflac.core.osgi.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.ServletOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.eclipse.jetty.util.ssl.SslContextFactory.Client;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

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
import com.aflac.core.servlets.MasterAppServlet;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
public class MasterAppServiceImplTest {
	
	private static AemContext aemContext;
	private static ExperienceApiConfig apiUrlService;
	private static GeneratePDFService pdfService;
	private static CloseableHttpClient httpClient;
	private static CloseableHttpClient closeableHttpClient; 
	private static CloseableHttpResponse closeableHttpResponse;
	private static MasterAppService masterAppService;
	private static MasterAppServlet masterAppServlet;
	private static PrintWriter printWriter;
	private static CaseBuilderService caseService;
	private static ComplianceService complianceService;
	private static FileNetApiConfig fileNetConfig;
	private static FileNetService fileNet;
	private static AdobePDFApiIntegrationConfig getCredentials;
	private static AuditService auditService;
	
	
	@BeforeAll
	public static void setup() {
		aemContext = new AemContext();
		httpClient = mock(CloseableHttpClient.class);
		apiUrlService = aemContext.registerInjectActivateService(new ExperienceApiConfigImpl());
		auditService = aemContext.registerInjectActivateService(new AuditServiceImpl());
		caseService = aemContext.registerInjectActivateService(new CaseBuilderServiceImpl(httpClient,apiUrlService, auditService));
		complianceService = aemContext.registerInjectActivateService(new ComplianceServiceImpl(httpClient,apiUrlService));
		fileNetConfig = aemContext.registerInjectActivateService(new FileNetApiConfigImpl());
		fileNet = aemContext.registerInjectActivateService(new FileNetServiceImpl(apiUrlService, fileNetConfig));
		getCredentials = aemContext.registerInjectActivateService(new AdobePDFApiIntegrationConfigImpl());
		printWriter = mock(PrintWriter.class);
		pdfService = aemContext.registerInjectActivateService(new GeneratePDFServiceImpl(httpClient, getCredentials, caseService, complianceService, fileNetConfig, fileNet));
		closeableHttpClient = mock(CloseableHttpClient.class);
        closeableHttpResponse = mock(CloseableHttpResponse.class);
        masterAppService = aemContext.registerInjectActivateService(new MasterAppServiceImpl(httpClient,apiUrlService, pdfService));
        masterAppServlet = new MasterAppServlet(masterAppService);
	}
	
	@Test
	public void fetchMasterApp() {
		String response ="{\"metadata\":{\"status\":\"success\",\"code\":\"OK\",\"descriptions\":[{\"context\":\"success\",\"type\":\"info\",\"code\":\"200 OK\",\"short-description\":\"generic.success.message\",\"long-description\":\"Request completed successfully.\"}]},\"data\":{\"master-app-entity-id\":\"GP-2#2024-03-07\",\"version\":2,\"situs-state\":\"AL\",\"group-type\":\"Employer Group\",\"census-enrollement\":null,\"eligible-series\":\"9100,\",\"forms-ids\":null,\"account-eligibility\":{\"group-number-or-gp-id\":\"GP-2\",\"effective-date\":\"2024-03-07\",\"organization-name\":\"sasda\",\"city-of-hq\":\"assa\",\"subsidiaries-affiliates\":\"sasda1\",\"cert-holders\":\"Employee\",\"hour-per-week-full-time\":12,\"hour-per-week-part-time\":null,\"enroll-employee-count\":25,\"eligible-employee-count\":12,\"other-requirement\":\"sdsad\",\"purpose-of-application\":null,\"classes-added-or-removed\":null,\"policy-numbers\":null,\"applicant\":null,\"applicant-other\":null,\"sic-code\":null,\"address\":null,\"number-or-street\":null,\"city\":\"assa\",\"state\":null,\"zip\":null,\"physical-address\":null,\"physical-number-or-street\":null,\"physical-city\":null,\"physical-state\":null,\"physical-zip\":null,\"authorized-person-name\":null,\"authorized-person-title\":null,\"contact-name\":null,\"contact-title\":null,\"contact-phone\":null,\"contact-email\":null,\"erisa-plan\":null,\"plan-number\":null,\"sad-to-be-covered\":null,\"sad-names\":null,\"first-of-the-month\":\"Yes\",\"full-time-or-part-time\":\"No\",\"full-time-eligible-coverage-duration\":\"12\",\"part-time-eligible-coverage-duration\":null,\"full-time-eligible-coverage-month\":\"Days\",\"part-time-eligible-coverage-month\":null},\"product\":[{\"product-name\":\"TL9100\",\"benefit-type\":null,\"application-reason\":\"New Policy\",\"eligible-employee\":null,\"eligible-employee-except\":null,\"plan\":\"1\",\"optional-features\":null,\"optional-features-other\":null,\"effective-date\":\"2024-03-07\",\"rates\":\"12\",\"employee-premium\":\"100\",\"employer-premium\":\"0\",\"is-replacing-group-policy\":false,\"series\":\"9100\",\"eligible-spouse\":null,\"eligible-employee-other\":null,\"eligible-employee-class\":null,\"product-class\":null,\"plan-features\":null,\"plan-features-other\":null,\"plan-level-premium\":\"5-Year Term\",\"product-sequence\":6,\"optional-features-text\":\"ssdsaas\",\"benefit-period\":null,\"elimination-period\":null,\"income-replacement\":null,\"carrier-policy-number\":null,\"group-policy-state\":\"AL\",\"optional-term-life-coverages\":null,\"ciac\":null,\"proposed-effective-date\":null,\"on-date\":\"2024-03-07\",\"minimum-no-of-employees\":null,\"percentile-of-eligible-employees\":null,\"premium-paid\":null,\"product-contribution\":\"100% Voluntary\",\"premium-payment-mode\":null,\"employee-cost-of-insurance\":null,\"actively-at-work\":{\"active-at-work-data\":[{\"class-name\":\"ssc\",\"actively-at-work-hours\":12}]},\"waiting-period-eligibility\":{\"waiting-period-eligibility-data\":[{\"class-name\":\"ccz\",\"waiting-period\":\"after Active Employment\",\"number-Of-days\":\"30\"}]},\"existing-policy-number\":null,\"other-application-reason\":null,\"hi-enroll-employee-count\":null,\"plan-year-per-insured\":null,\"premium-due\":null,\"pre-existing-condition-limitation\":null,\"preceeding-the-CED-months\":0,\"following-the-CED-months\":0}],\"for-status-update\":false,\"master-app-review-table\":{\"master-app-review-table-data\":[{\"master-app-entity-id-with-version\":null,\"signature-status\":\"Doc Not Generated\",\"filenet-document-id\":null,\"products\":\"TL9100\",\"form-id\":\"C03204\",\"api-form-id\":\"C03204AL\",\"signature-checkbox\":\"-100\",\"agreement-id\":null,\"masterapp-sent-datetime\":null,\"masterapp-received-datetime\":null}]},\"first-signer-email-id\":null,\"second-signer-email-id\":null,\"data-status\":\"Data Saved # Thu, 07 Mar 2024 10:37:45 GMT\"}}";
		SlingHttpServletRequest slingHttpServletRequest=mock(SlingHttpServletRequest.class);
    	SlingHttpServletResponse slingHttpServletResponse = mock(SlingHttpServletResponse.class);
		try {
			when(slingHttpServletRequest.getParameter("mode")).thenReturn("fetch");
			when(slingHttpServletRequest.getParameter("groupNumber")).thenReturn("GP-1");
			when(slingHttpServletRequest.getParameter("effectiveDate")).thenReturn("2024-01-31");
			when(slingHttpServletResponse.getWriter()).thenReturn(printWriter);
			setUpClient(closeableHttpClient, closeableHttpResponse,response);
			masterAppServlet.doGet(slingHttpServletRequest, slingHttpServletResponse);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateMasterApp() {
		String response ="{\"metadata\":{\"status\":\"success\",\"code\":\"OK\",\"descriptions\":[{\"context\":\"success\",\"type\":\"info\",\"code\":\"200 OK\",\"short-description\":\"generic.success.message\",\"long-description\":\"Request completed successfully.\"}]},\"data\":{\"master-app-entity-id\":\"GP-1#2024-01-31\",\"version\":26,\"situs-state\":\"IN\",\"group-type\":\"Employer Group\",\"census-enrollement\":null,\"eligible-series\":\"7700,2800,8500,1100,5000,9100,93000,9800,\",\"forms-ids\":null,\"account-eligibility\":{\"group-number-or-gp-id\":\"GP-1\",\"effective-date\":\"2024-01-31\",\"organization-name\":\"aflac\",\"city-of-hq\":\"NYC\",\"subsidiaries-affiliates\":\"asda\",\"cert-holders\":\"Employee\",\"hour-per-week-full-time\":12,\"hour-per-week-part-time\":15,\"enroll-employee-count\":25,\"eligible-employee-count\":15,\"other-requirement\":\"other\",\"purpose-of-application\":null,\"classes-added-or-removed\":null,\"policy-numbers\":null,\"applicant\":null,\"applicant-other\":null,\"sic-code\":null,\"address\":null,\"number-or-street\":null,\"city\":\"NYC\",\"state\":null,\"zip\":null,\"physical-address\":null,\"physical-number-or-street\":null,\"physical-city\":null,\"physical-state\":null,\"physical-zip\":null,\"authorized-person-name\":null,\"authorized-person-title\":null,\"contact-name\":null,\"contact-title\":null,\"contact-phone\":null,\"contact-email\":null,\"erisa-plan\":null,\"plan-number\":null,\"sad-to-be-covered\":null,\"sad-names\":null,\"first-of-the-month\":\"Yes\",\"full-time-or-part-time\":\"Yes\",\"full-time-eligible-coverage-duration\":\"12\",\"part-time-eligible-coverage-duration\":\"12\",\"full-time-eligible-coverage-month\":\"Days\",\"part-time-eligible-coverage-month\":\"Days\"},\"product\":[{\"product-name\":\"AC7700\",\"benefit-type\":\"24 Hours\",\"application-reason\":\"New Policy\",\"eligible-employee\":\"Regular full-time Employee Between Age 18 and 69\",\"eligible-employee-except\":\"except\",\"plan\":\"1\",\"optional-features\":[\"Initial Accident Treatment Category: High\",\"Hospitalization Category: Mid\",\"After Care Category: Low\",\"Total Disability Rider\",\"Gunshot Wound Rider: $5000\"],\"optional-features-other\":null,\"effective-date\":\"2024-01-31\",\"rates\":\"1\",\"employee-premium\":\"100\",\"employer-premium\":\"0\",\"is-replacing-group-policy\":false,\"series\":\"7700\",\"eligible-spouse\":\"Spouses of Eligible Employee Between Age 18 And 64\",\"eligible-employee-other\":\"other\",\"eligible-employee-class\":\"P\",\"product-class\":null,\"plan-features\":null,\"plan-features-other\":null,\"plan-level-premium\":null,\"product-sequence\":1,\"optional-features-text\":null,\"benefit-period\":null,\"elimination-period\":null,\"income-replacement\":null,\"carrier-policy-number\":null,\"group-policy-state\":\"IN\",\"optional-term-life-coverages\":null,\"ciac\":null,\"proposed-effective-date\":null,\"on-date\":\"2024-01-31\",\"minimum-no-of-employees\":null,\"percentile-of-eligible-employees\":null,\"premium-paid\":null,\"product-contribution\":\"100% Voluntary\",\"premium-payment-mode\":null,\"employee-cost-of-insurance\":null,\"actively-at-work\":null,\"waiting-period-eligibility\":null,\"existing-policy-number\":null,\"other-application-reason\":null,\"hi-enroll-employee-count\":null,\"plan-year-per-insured\":null,\"premium-due\":null},{\"product-name\":\"CI2800\",\"benefit-type\":null,\"application-reason\":\"Change to Existing Policy Number\",\"eligible-employee\":\"Regular full-time Employee Between Age 18 and 69\",\"eligible-employee-except\":null,\"plan\":null,\"optional-features\":[\"Non-Invasive Cancer Benefit: Yes\",\"Skin Cancer Benefit: Yes\",\"With Health Screening Benefit:Yes\"],\"optional-features-other\":null,\"effective-date\":\"2024-01-31\",\"rates\":\"2\",\"employee-premium\":\"0\",\"employer-premium\":\"100\",\"is-replacing-group-policy\":false,\"series\":\"2800\",\"eligible-spouse\":\"Spouses of Eligible Employee Between Age 18 And 69\",\"eligible-employee-other\":\"qwe\",\"eligible-employee-class\":null,\"product-class\":null,\"plan-features\":null,\"plan-features-other\":null,\"plan-level-premium\":null,\"product-sequence\":2,\"optional-features-text\":null,\"benefit-period\":null,\"elimination-period\":null,\"income-replacement\":null,\"carrier-policy-number\":null,\"group-policy-state\":\"IN\",\"optional-term-life-coverages\":null,\"ciac\":null,\"proposed-effective-date\":null,\"on-date\":\"2024-01-31\",\"minimum-no-of-employees\":null,\"percentile-of-eligible-employees\":null,\"premium-paid\":null,\"product-contribution\":\"100% Employer-Paid\",\"premium-payment-mode\":null,\"employee-cost-of-insurance\":null,\"actively-at-work\":null,\"waiting-period-eligibility\":null,\"existing-policy-number\":\"123\",\"other-application-reason\":null,\"hi-enroll-employee-count\":null,\"plan-year-per-insured\":null,\"premium-due\":null},{\"product-name\":\"HI8500\",\"benefit-type\":null,\"application-reason\":\"New Policy\",\"eligible-employee\":\"Regular part-time Employee Between Age 18 and 64\",\"eligible-employee-except\":null,\"plan\":\"2\",\"optional-features\":[\"Non-Occupational\",\"Hospitalization Category: Mid\"],\"optional-features-other\":null,\"effective-date\":\"2024-01-31\",\"rates\":\"3\",\"employee-premium\":\"100\",\"employer-premium\":\"0\",\"is-replacing-group-policy\":false,\"series\":\"8500\",\"eligible-spouse\":\"Spouses of Eligible Employee Between Age 18 And 64\",\"eligible-employee-other\":\"ssdadsa\",\"eligible-employee-class\":null,\"product-class\":null,\"plan-features\":null,\"plan-features-other\":null,\"plan-level-premium\":null,\"product-sequence\":3,\"optional-features-text\":null,\"benefit-period\":null,\"elimination-period\":null,\"income-replacement\":null,\"carrier-policy-number\":null,\"group-policy-state\":\"IN\",\"optional-term-life-coverages\":null,\"ciac\":null,\"proposed-effective-date\":null,\"on-date\":\"2024-01-31\",\"minimum-no-of-employees\":null,\"percentile-of-eligible-employees\":null,\"premium-paid\":null,\"product-contribution\":\"100% Voluntary\",\"premium-payment-mode\":null,\"employee-cost-of-insurance\":null,\"actively-at-work\":null,\"waiting-period-eligibility\":null,\"existing-policy-number\":null,\"other-application-reason\":null,\"hi-enroll-employee-count\":25,\"plan-year-per-insured\":\"sadsa\",\"premium-due\":null},{\"product-name\":\"D1100\",\"benefit-type\":null,\"application-reason\":\"New Policy\",\"eligible-employee\":\"Regular part-time Employee Between Age 18 and 69\",\"eligible-employee-except\":null,\"plan\":\"3\",\"optional-features\":[\"Orthodontic Benefit Rider\"],\"optional-features-other\":null,\"effective-date\":\"2024-01-31\",\"rates\":\"4\",\"employee-premium\":\"0\",\"employer-premium\":\"100\",\"is-replacing-group-policy\":false,\"series\":\"1100\",\"eligible-spouse\":\"Spouses of Eligible Employee Between Age 18 and 69\",\"eligible-employee-other\":\"asd\",\"eligible-employee-class\":null,\"product-class\":null,\"plan-features\":null,\"plan-features-other\":null,\"plan-level-premium\":null,\"product-sequence\":4,\"optional-features-text\":null,\"benefit-period\":null,\"elimination-period\":null,\"income-replacement\":null,\"carrier-policy-number\":null,\"group-policy-state\":\"IN\",\"optional-term-life-coverages\":null,\"ciac\":null,\"proposed-effective-date\":null,\"on-date\":\"2024-01-31\",\"minimum-no-of-employees\":null,\"percentile-of-eligible-employees\":null,\"premium-paid\":null,\"product-contribution\":\"100% Employer-Paid\",\"premium-payment-mode\":null,\"employee-cost-of-insurance\":null,\"actively-at-work\":null,\"waiting-period-eligibility\":null,\"existing-policy-number\":null,\"other-application-reason\":null,\"hi-enroll-employee-count\":null,\"plan-year-per-insured\":null,\"premium-due\":null},{\"product-name\":\"DI5000\",\"benefit-type\":null,\"application-reason\":\"New Policy\",\"eligible-employee\":\"Regular part-time Employee Between Age 18 and 69\",\"eligible-employee-except\":null,\"plan\":null,\"optional-features\":[\"Non-Occupational\",\"Pre-Existing Condition Benefit\"],\"optional-features-other\":null,\"effective-date\":\"2024-01-31\",\"rates\":\"5\",\"employee-premium\":\"0\",\"employer-premium\":\"100\",\"is-replacing-group-policy\":false,\"series\":\"5000\",\"eligible-spouse\":null,\"eligible-employee-other\":\"assasda\",\"eligible-employee-class\":null,\"product-class\":\"Class A\",\"plan-features\":null,\"plan-features-other\":null,\"plan-level-premium\":null,\"product-sequence\":5,\"optional-features-text\":null,\"benefit-period\":\"6-months\",\"elimination-period\":\"14/14\",\"income-replacement\":\"60%\",\"carrier-policy-number\":null,\"group-policy-state\":\"IN\",\"optional-term-life-coverages\":null,\"ciac\":null,\"proposed-effective-date\":null,\"on-date\":\"2024-01-31\",\"minimum-no-of-employees\":null,\"percentile-of-eligible-employees\":null,\"premium-paid\":null,\"product-contribution\":\"100% Employer-Paid\",\"premium-payment-mode\":null,\"employee-cost-of-insurance\":null,\"actively-at-work\":null,\"waiting-period-eligibility\":null,\"existing-policy-number\":null,\"other-application-reason\":null,\"hi-enroll-employee-count\":null,\"plan-year-per-insured\":null,\"premium-due\":null},{\"product-name\":\"TL9100\",\"benefit-type\":null,\"application-reason\":\"New Policy\",\"eligible-employee\":null,\"eligible-employee-except\":null,\"plan\":\"3\",\"optional-features\":null,\"optional-features-other\":null,\"effective-date\":\"2024-01-31\",\"rates\":\"6\",\"employee-premium\":\"21\",\"employer-premium\":\"45\",\"is-replacing-group-policy\":false,\"series\":\"9100\",\"eligible-spouse\":null,\"eligible-employee-other\":null,\"eligible-employee-class\":null,\"product-class\":null,\"plan-features\":null,\"plan-features-other\":null,\"plan-level-premium\":\"20-Year Term\",\"product-sequence\":6,\"optional-features-text\":\"optional\",\"benefit-period\":null,\"elimination-period\":null,\"income-replacement\":null,\"carrier-policy-number\":null,\"group-policy-state\":\"IN\",\"optional-term-life-coverages\":null,\"ciac\":null,\"proposed-effective-date\":null,\"on-date\":\"2024-01-31\",\"minimum-no-of-employees\":null,\"percentile-of-eligible-employees\":null,\"premium-paid\":null,\"product-contribution\":\"Other\",\"premium-payment-mode\":null,\"employee-cost-of-insurance\":null,\"actively-at-work\":{\"active-at-work-data\":[{\"class-name\":\"Class\",\"actively-at-work-hours\":12},{\"class-name\":\"B\",\"actively-at-work-hours\":34}]},\"waiting-period-eligibility\":{\"waiting-period-eligibility-data\":[{\"class-name\":\"Other\",\"waiting-period\":\"after Active Employment\",\"number-Of-days\":\"30\"},{\"class-name\":\"B\",\"waiting-period\":\"on Date of Employment\",\"number-Of-days\":null}]},\"existing-policy-number\":null,\"other-application-reason\":null,\"hi-enroll-employee-count\":null,\"plan-year-per-insured\":null,\"premium-due\":null},{\"product-name\":\"TL93000\",\"benefit-type\":null,\"application-reason\":null,\"eligible-employee\":\"Regular full-time Employee Between Age 18 and 70\",\"eligible-employee-except\":null,\"plan\":null,\"optional-features\":null,\"optional-features-other\":null,\"effective-date\":\"2024-01-31\",\"rates\":null,\"employee-premium\":\"100\",\"employer-premium\":\"0\",\"is-replacing-group-policy\":false,\"series\":\"93000\",\"eligible-spouse\":\"Spouses of Eligible Employee Between Age 18 And 70\",\"eligible-employee-other\":\"sasas\",\"eligible-employee-class\":null,\"product-class\":null,\"plan-features\":null,\"plan-features-other\":null,\"plan-level-premium\":null,\"product-sequence\":6,\"optional-features-text\":null,\"benefit-period\":null,\"elimination-period\":null,\"income-replacement\":null,\"carrier-policy-number\":null,\"group-policy-state\":\"CA\",\"optional-term-life-coverages\":\"Spouse Term Life\",\"ciac\":\"Yes\",\"proposed-effective-date\":\"2024-01-27\",\"on-date\":\"2024-01-31\",\"minimum-no-of-employees\":\"25\",\"percentile-of-eligible-employees\":\"12%\",\"premium-paid\":null,\"product-contribution\":\"100% Voluntary\",\"premium-payment-mode\":null,\"employee-cost-of-insurance\":null,\"actively-at-work\":null,\"waiting-period-eligibility\":null,\"existing-policy-number\":null,\"other-application-reason\":null,\"hi-enroll-employee-count\":null,\"plan-year-per-insured\":null,\"premium-due\":null},{\"product-name\":\"WL9800\",\"benefit-type\":null,\"application-reason\":\"New Policy\",\"eligible-employee\":\"Regular part-time Employee Between Age 18 and 70\",\"eligible-employee-except\":null,\"plan\":null,\"optional-features\":null,\"optional-features-other\":null,\"effective-date\":\"2024-01-31\",\"rates\":\"7\",\"employee-premium\":\"0\",\"employer-premium\":\"100\",\"is-replacing-group-policy\":false,\"series\":\"9800\",\"eligible-spouse\":\"Spouses of Eligible Employee Between Age 18 And 70\",\"eligible-employee-other\":\"qwe\",\"eligible-employee-class\":null,\"product-class\":null,\"plan-features\":[\"Children Term Rider\"],\"plan-features-other\":null,\"plan-level-premium\":null,\"product-sequence\":7,\"optional-features-text\":\"Options\",\"benefit-period\":null,\"elimination-period\":null,\"income-replacement\":null,\"carrier-policy-number\":null,\"group-policy-state\":\"IN\",\"optional-term-life-coverages\":null,\"ciac\":null,\"proposed-effective-date\":null,\"on-date\":\"2024-01-31\",\"minimum-no-of-employees\":null,\"percentile-of-eligible-employees\":null,\"premium-paid\":null,\"product-contribution\":\"100% Employer-Paid\",\"premium-payment-mode\":null,\"employee-cost-of-insurance\":null,\"actively-at-work\":null,\"waiting-period-eligibility\":null,\"existing-policy-number\":null,\"other-application-reason\":null,\"hi-enroll-employee-count\":null,\"plan-year-per-insured\":null,\"premium-due\":null}],\"for-status-update\":false,\"signature-status\":\"Data Saved; No App Sent for Signature\",\"filenet-document-id\":null,\"masterapp-sent-datetime\":null,\"masterapp-received-datetime\":null}}";
		SlingHttpServletRequest slingHttpServletRequest=mock(SlingHttpServletRequest.class);
    	SlingHttpServletResponse slingHttpServletResponse = mock(SlingHttpServletResponse.class);
		try {
			when(slingHttpServletRequest.getParameter("mode")).thenReturn("update");
			when(slingHttpServletRequest.getParameter("groupNumber")).thenReturn("GP-1");
			when(slingHttpServletRequest.getParameter("effectiveDate")).thenReturn("2024-01-31");
			when(slingHttpServletRequest.getParameter("addedProducts")).thenReturn("AC7800");
			when(slingHttpServletRequest.getParameter("deletedProducts")).thenReturn("AC7700");
			when(slingHttpServletResponse.getWriter()).thenReturn(printWriter);
			setUpClient(closeableHttpClient, closeableHttpResponse,response);
			masterAppServlet.doGet(slingHttpServletRequest, slingHttpServletResponse);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddMasterAppProducts() {
		String response ="";
		SlingHttpServletRequest slingHttpServletRequest=mock(SlingHttpServletRequest.class);
    	SlingHttpServletResponse slingHttpServletResponse = mock(SlingHttpServletResponse.class);
		try {
			when(slingHttpServletRequest.getParameter("mode")).thenReturn("addProducts");
			when(slingHttpServletRequest.getParameter("addedProducts")).thenReturn("7700,2800,8500,1100,5000,9100,9800");
			when(slingHttpServletResponse.getWriter()).thenReturn(printWriter);
			setUpClient(closeableHttpClient, closeableHttpResponse,response);
			masterAppServlet.doGet(slingHttpServletRequest, slingHttpServletResponse);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMasterAppFetchSeries() {
		String response ="{\"metadata\":{\"status\":\"success\",\"code\":\"OK\",\"descriptions\":[{\"context\":\"success\",\"type\":\"info\",\"code\":\"200 OK\",\"short-description\":\"generic.success.message\",\"long-description\":\"Request completed successfully.\"}]},\"data\":[{\"situs-state\":\"AL\",\"form-id\":\"C03204AL\",\"product-series\":[\"7700\",\"7800\",\"70000\",\"2800\",\"20000\",\"21000\",\"1100\",\"5000\",\"50000\",\"8500\",\"80000\",\"81000\",\"9800\",\"60000\",\"9100\",\"22000\"],\"census-enrollment\":\"N/A\"},{\"situs-state\":\"AL\",\"form-id\":\"ICC22 93201\",\"product-series\":[\"93000\"],\"census-enrollment\":\"N/A\"}]}";
		SlingHttpServletRequest slingHttpServletRequest=mock(SlingHttpServletRequest.class);
    	SlingHttpServletResponse slingHttpServletResponse = mock(SlingHttpServletResponse.class);
		try {
			when(slingHttpServletRequest.getParameter("mode")).thenReturn("fetchSeries");
			when(slingHttpServletRequest.getParameter("situs")).thenReturn("CA");
			when(slingHttpServletRequest.getParameter("groupType")).thenReturn("Union");
			when(slingHttpServletRequest.getParameter("census")).thenReturn("N/A");
			when(slingHttpServletResponse.getWriter()).thenReturn(printWriter);
			setUpClient(closeableHttpClient, closeableHttpResponse,response);
			masterAppServlet.doGet(slingHttpServletRequest, slingHttpServletResponse);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMasterAppOptionalFeatures() {
		String response ="{\"metadata\":{\"status\":\"success\",\"code\":\"OK\",\"descriptions\":[{\"context\":\"success\",\"type\":\"info\",\"code\":\"200 OK\",\"short-description\":\"generic.success.message\",\"long-description\":\"Request completed successfully.\"}]},\"data\":[{\"situs-state\":\"NH\",\"form-id\":\"C21201.1NH\",\"product-series\":[\"21000\"],\"census-enrollment\":\"N/A\"},{\"situs-state\":\"NH\",\"form-id\":\"C22201NH\",\"product-series\":[\"22000\"],\"census-enrollment\":\"N/A\"},{\"situs-state\":\"NH\",\"form-id\":\"C50201NH\",\"product-series\":[\"50000\"],\"census-enrollment\":\"N/A\"},{\"situs-state\":\"NH\",\"form-id\":\"C60201NH\",\"product-series\":[\"60000\"],\"census-enrollment\":\"N/A\"},{\"situs-state\":\"NH\",\"form-id\":\"C70201.2NH \",\"product-series\":[\"70000\"],\"census-enrollment\":\"N/A\"},{\"situs-state\":\"NH\",\"form-id\":\"C80201.2NH\",\"product-series\":[\"80000\"],\"census-enrollment\":\"N/A\"},{\"situs-state\":\"NH\",\"form-id\":\"CAI0010NH\",\"product-series\":[\"5000\"],\"census-enrollment\":\"N/A\"},{\"situs-state\":\"NH\",\"form-id\":\"CAI9110\",\"product-series\":[\"9100\"],\"census-enrollment\":\"N/A\"},{\"situs-state\":\"NH\",\"form-id\":\"ICC22 93201\",\"product-series\":[\"93000\"],\"census-enrollment\":\"N/A\"},{\"situs-state\":\"NH\",\"form-id\":\"WL9800-MA\",\"product-series\":[\"9800\"],\"census-enrollment\":\"N/A\"}]}";
		SlingHttpServletRequest slingHttpServletRequest=mock(SlingHttpServletRequest.class);
    	SlingHttpServletResponse slingHttpServletResponse = mock(SlingHttpServletResponse.class);
		try {
			when(slingHttpServletRequest.getParameter("mode")).thenReturn("getOptionalFeatures");
			when(slingHttpServletResponse.getWriter()).thenReturn(printWriter);
			setUpClient(closeableHttpClient, closeableHttpResponse,response);
			masterAppServlet.doGet(slingHttpServletRequest, slingHttpServletResponse);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSaveMasterApp() {
		String startDateTime = "2023-07-07 15:40:19 Asia/Calcutta";
		String response ="{\"metadata\":{\"status\":\"success\",\"code\":\"CREATED\",\"descriptions\":[{\"context\":\"success\",\"type\":\"info\",\"code\":\"201 CREATED\",\"short-description\":\"generic.success.message\",\"long-description\":\"Request completed successfully.\"}]},\"data\":{\"status\":true,\"validation-status\":\"Success\"}}";
		String formData = "<afData><afUnboundData><data><GroupNoRadioButton>GP-ID</GroupNoRadioButton><masterAppGroupNo>GP-123</masterAppGroupNo><coverageEffDateMasterApp>2023-12-15</coverageEffDateMasterApp><masterAppFormCase>Add</masterAppFormCase><employeeType>full-time</employeeType><exceptionRadio>No</exceptionRadio></data></afUnboundData><afBoundData><masterApp xmlns:xfa=\"http://www.xfa.org/schema/xfa-data/1.0/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance/\"><MetaData><title/><masterAppId/></MetaData><masterAppEntityId/><version/><situsState>AL</situsState><groupType>Employer Group</groupType><censusEnrollment/><eligible-series>7700</eligible-series><formIds/><AccountAndEligiblity><groupNumberOrGpId>GP-123</groupNumberOrGpId><effectiveDate>2023-12-15</effectiveDate><organizationName>Test</organizationName><certHolders>Employee</certHolders><hourPerWeekFullTime>34</hourPerWeekFullTime><hourPerWeekpartTime>45</hourPerWeekpartTime><eligibleCoverageMonth>56-months</eligibleCoverageMonth><cityOfHq>NY</cityOfHq><subsidiariesAffiliates>subs</subsidiariesAffiliates><enrollEmployeeCount>25</enrollEmployeeCount><eligibleEmployeeCount>100</eligibleEmployeeCount><otherRequirement>others</otherRequirement><erisaPlan>No</erisaPlan><churchPlan>No</churchPlan></AccountAndEligiblity><Product><productName>TL9100</productName><productSequence>6</productSequence><series>9100</series><planLevelPremium>5-Year Term</planLevelPremium><applicationReason>New Policy</applicationReason><ActivelyAtWork><ActiveAtWorkData><className>ssc</className><activelyAtWorkHours>12</activelyAtWorkHours></ActiveAtWorkData></ActivelyAtWork><WaitingPeriodEligibility><WaitingPeriodEligibilityData><className>ccz</className><waitingPeriod>after Active Employment</waitingPeriod><numberOfDays>30</numberOfDays></WaitingPeriodEligibilityData></WaitingPeriodEligibility><optionalFeaturesText>ssdsaas</optionalFeaturesText><plan>1</plan><effectiveDate>2024-03-07</effectiveDate><rates>12</rates><productContribution>100% Voluntary</productContribution><employeePremium>100</employeePremium><employerPremium>0</employerPremium><groupPolicyState>AL</groupPolicyState><onDate>2024-03-07</onDate></Product><MasterAppReviewTable><MasterAppReviewTableData><formId>C03204</formId><apiFormId>C03204AL</apiFormId><product>TL9100</product><signStatus>Doc Not Generated</signStatus><signatureCheckbox>0</signatureCheckbox><masterappSentDatetime/><masterappReceivedDatetime/><filenetDocumentId/><agreementId/></MasterAppReviewTableData></MasterAppReviewTable></masterApp></afBoundData><afSubmissionInfo><lastFocusItem>guide[0].guide1[0].guideRootPanel[0].masterAppOuterPanel[0].MasterAppDetails[0].reviewSection[0]</lastFocusItem><stateOverrides/><signers/></afSubmissionInfo></afData>";
		SlingHttpServletRequest slingHttpServletRequest=mock(SlingHttpServletRequest.class);
    	SlingHttpServletResponse slingHttpServletResponse = mock(SlingHttpServletResponse.class);
    	Session session = mock(Session.class);
    	ResourceResolver resolver = mock(ResourceResolver.class);
		try {
			when(slingHttpServletRequest.getParameter("mode")).thenReturn("save");
			when(slingHttpServletRequest.getParameter("formData")).thenReturn(formData);
			when(slingHttpServletRequest.getParameter("startDateTime")).thenReturn(startDateTime);
			StatusLine status = mock(StatusLine.class);
	        when(closeableHttpResponse.getStatusLine()).thenReturn(status);
	        when(status.getStatusCode()).thenReturn(200);
	        when(slingHttpServletRequest.getResourceResolver()).thenReturn(resolver);
			when(resolver.adaptTo(Session.class)).thenReturn(session);
			when(session.getUserID()).thenReturn("V41075");
			when(slingHttpServletResponse.getWriter()).thenReturn(printWriter);
			when(slingHttpServletRequest.getParameter("startDateTime")).thenReturn(startDateTime);
	        when(closeableHttpResponse.getStatusLine()).thenReturn(status);
	        when(status.getStatusCode()).thenReturn(200);
			when(slingHttpServletRequest.getResourceResolver()).thenReturn(resolver);
			when(resolver.adaptTo(Session.class)).thenReturn(session);
			when(session.getUserID()).thenReturn("V41075");
			setUpClient(closeableHttpClient, closeableHttpResponse,response);
			masterAppServlet.doPost(slingHttpServletRequest, slingHttpServletResponse);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDownloadMasterApp() {
		String seriesData ="[{\"apiFormId\":\"C03204AL\",\"form-id\":\"C03204\",\"product-series\":[\"7700\",\"7800\",\"70000\",\"2800\",\"20000\",\"21000\",\"22000\",\"8500\",\"80000\",\"81000\",\"1100\",\"5000\",\"50000\",\"9100\",\"9800\",\"60000\"]},{\"apiFormId\":\"ICC22 C93201\",\"form-id\":\"ICC22 C93201\",\"product-series\":[\"93000\"]}]";
		String formData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><afData><afUnboundData><data><GroupNoRadioButton>GP-ID</GroupNoRadioButton><masterAppGroupNo>GP-123</masterAppGroupNo><coverageEffDateMasterApp>2024-01-10</coverageEffDateMasterApp><accidentCheckbox>7700,70000</accidentCheckbox><termLifeCheckbox>93000</termLifeCheckbox><masterAppAddedProducts>AC7700,AC70000,TL93000</masterAppAddedProducts><masterAppFormCase>Edit</masterAppFormCase><employeeType>full-time</employeeType><eSignCheckbox>Yes</eSignCheckbox></data></afUnboundData><afBoundData><masterApp xmlns:xfa=\"http://www.xfa.org/schema/xfa-data/1.0/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><situsState>AL</situsState><groupType>Employer Group</groupType><censusEnrollment/><eligible-series>7700,70000,93000,</eligible-series><MetaData><title/><masterAppId/></MetaData><masterAppEntityId/><version>6</version><formIds>C03204,ICC22 C93201</formIds><AccountAndEligiblity><groupNumberOrGpId>GP-123</groupNumberOrGpId><organizationName>GP-123</organizationName><cityOfHq>123</cityOfHq><subsidiariesAffiliates>123</subsidiariesAffiliates><certHolders>Employee</certHolders><hourPerWeekFullTime>123</hourPerWeekFullTime><hourPerWeekpartTime>123</hourPerWeekpartTime><enrollEmployeeCount>25</enrollEmployeeCount><eligibleEmployeeCount>123</eligibleEmployeeCount><effectiveDate>2024-01-10</effectiveDate><otherRequirement>123</otherRequirement></AccountAndEligiblity><Product><productName>AC7700</productName><productSequence>1</productSequence><series>7700</series><benefitType>24 Hours</benefitType><applicationReason>Change to Existing Policy Number</applicationReason><existingPolicyNumber>123</existingPolicyNumber><eligibleEmployee>Regular full-time Employee Between Age 18 and 69</eligibleEmployee><eligibleEmployeeExcept>123</eligibleEmployeeExcept><eligibleSpouse>Spouses of Eligible Employee Between Age 18 And 64</eligibleSpouse><eligibleEmployeeOther>123</eligibleEmployeeOther><ActivelyAtWork><ActiveAtWorkData><className/><activelyAtWorkHours/></ActiveAtWorkData></ActivelyAtWork><WaitingPeriodEligibility><WaitingPeriodEligibilityData><className/><waitingPeriod/><numberOfDays/></WaitingPeriodEligibilityData></WaitingPeriodEligibility><optionalFeatures>Initial Accident Treatment Category: High,Initial Accident Treatment Category: Mid,Initial Accident Treatment Category: Low,After Care Category: Mid</optionalFeatures><plan>1</plan><effectiveDate>2024-01-10</effectiveDate><rates>123</rates><productContribution>100% Voluntary</productContribution><employeePremium>100</employeePremium><employerPremium>0</employerPremium><onDate>2024-01-10</onDate></Product><Product><productName>AC70000</productName><productSequence>1</productSequence><series>70000</series><benefitType>24 Hours</benefitType><applicationReason>New Policy</applicationReason><eligibleEmployee>Regular part-time Employee Minimum Age 18 </eligibleEmployee><eligibleEmployeeExcept>ww</eligibleEmployeeExcept><eligibleSpouse>Spouses of Eligible Employee Minumum Age 18</eligibleSpouse><eligibleEmployeeOther>w</eligibleEmployeeOther><ActivelyAtWork><ActiveAtWorkData><className/><activelyAtWorkHours/></ActiveAtWorkData></ActivelyAtWork><WaitingPeriodEligibility><WaitingPeriodEligibilityData><className/><waitingPeriod/><numberOfDays/></WaitingPeriodEligibilityData></WaitingPeriodEligibility><optionalFeatures>Initial Accident Treatment Category: High,Gunshot Wound Rider: $5000,Catastrophic Accident Rider,Dependent Rider</optionalFeatures><plan>1</plan><effectiveDate>2024-01-16</effectiveDate><rates>3</rates><productContribution>100% Employer-Paid</productContribution><employeePremium>0</employeePremium><employerPremium>100</employerPremium><onDate>2024-01-10</onDate></Product><Product><productName>TL93000</productName><productSequence>6</productSequence><series>93000</series><applicationReason/><eligibleEmployee>Regular full-time Employee Between Age 18 and 70</eligibleEmployee><eligibleSpouse>Spouses of Eligible Employee Between Age 18 And 70</eligibleSpouse><eligibleEmployeeOther>na</eligibleEmployeeOther><ActivelyAtWork><ActiveAtWorkData><className/><activelyAtWorkHours/></ActiveAtWorkData></ActivelyAtWork><WaitingPeriodEligibility><WaitingPeriodEligibilityData><className/><waitingPeriod/><numberOfDays/></WaitingPeriodEligibilityData></WaitingPeriodEligibility><effectiveDate>2024-01-10</effectiveDate><rates/><productContribution>100% Voluntary</productContribution><employeePremium>100</employeePremium><employerPremium>0</employerPremium><groupPolicyState>10</groupPolicyState><optionalTermlifeCoverages>Spouse Term Life,Extension Rider</optionalTermlifeCoverages><onDate>2024-01-10</onDate><minimumNoOfEmployees>10</minimumNoOfEmployees><percentileOfEligibleEmployees>10</percentileOfEligibleEmployees><ciac>No</ciac></Product><eSignatureRequired>Yes</eSignatureRequired><signerEmailId/><formId>ICC22 C93201</formId><formFolderLocation>/content/dam/formsanddocuments/aflacapps/masterapp-designs</formFolderLocation><contractTitle>GP-123 Master Application</contractTitle></masterApp></afBoundData><afSubmissionInfo><lastFocusItem>guide[0].guide1[0].guideRootPanel[0].masterAppOuterPanel[0].MasterAppDetails[0].reviewSection[0]</lastFocusItem><stateOverrides/><signers/></afSubmissionInfo></afData>";
		String formId = "C03204";
		String response = "pdf Downloaded";
		SlingHttpServletRequest slingHttpServletRequest=mock(SlingHttpServletRequest.class);
    	SlingHttpServletResponse slingHttpServletResponse = mock(SlingHttpServletResponse.class);
		try {
			ResourceResolver resolver = mock(ResourceResolver.class);
			Resource resourseXDP = mock(Resource.class);
			Asset asset = mock(Asset.class);
			Rendition rend = mock(Rendition.class);
			ServletOutputStream outStream = mock(ServletOutputStream.class);
			InputStream is = new ByteArrayInputStream("pdf file".getBytes());
						
			when(slingHttpServletRequest.getResourceResolver()).thenReturn(resolver);
			when(slingHttpServletRequest.getServerName()).thenReturn("local");
			when(slingHttpServletResponse.getOutputStream()).thenReturn(outStream);
			doNothing().when(outStream).flush();
			doNothing().when(outStream).close();
			when(resolver.getResource(anyString())).thenReturn(resourseXDP);
			when(resourseXDP.adaptTo(Asset.class)).thenReturn(asset);
			when(asset.getOriginal()).thenReturn(rend);
			when(resourseXDP.adaptTo(InputStream.class)).thenReturn(is);
			when(slingHttpServletRequest.getParameter("mode")).thenReturn("download");
			when(slingHttpServletRequest.getParameter("formData")).thenReturn(formData);
			when(slingHttpServletRequest.getParameter("seriesData")).thenReturn(seriesData);
			when(slingHttpServletRequest.getParameter("formId")).thenReturn(formId);
			StatusLine status = mock(StatusLine.class);
	        when(closeableHttpResponse.getStatusLine()).thenReturn(status);
	        when(status.getStatusCode()).thenReturn(200);
			when(slingHttpServletResponse.getWriter()).thenReturn(printWriter);
			setUpClient(closeableHttpClient, closeableHttpResponse,response);
			masterAppServlet.doPost(slingHttpServletRequest, slingHttpServletResponse);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void getMasterAppAttchment() {
		SlingHttpServletRequest slingHttpServletRequest=mock(SlingHttpServletRequest.class);
    	SlingHttpServletResponse slingHttpServletResponse = mock(SlingHttpServletResponse.class);
    	Map<String, String> map = new HashMap<>();
		try {
			String response="";
			map.put("path", "/content/dam/aflacapps/master-app-attchments");
			map.put("1_property", "AppUsed");
			map.put("1_property.value", "Master App");
			map.put("2_property", "AttachmentGroup");
			map.put("2_property.value", String.valueOf(1));
			map.put("3_property", "DocType");
			map.put("3_property.value", "Attachment");
			ResourceResolver resolver = mock(ResourceResolver.class);
			Resource resource = mock(Resource.class);
			Asset asset = mock(Asset.class);
			Session session = mock(Session.class);
			Query query = mock(Query.class);
			SearchResult result = mock(SearchResult.class);
			QueryBuilder queryBuilder = mock(QueryBuilder.class);
			Hit hit = mock(Hit.class);
			List<Hit> hitList =new ArrayList<Hit>();
			hitList.add(hit);
			when(slingHttpServletRequest.getResourceResolver()).thenReturn(resolver);
			when(slingHttpServletRequest.getParameter("mode")).thenReturn("getFiles");
			when(slingHttpServletRequest.getParameter("attachmentGroup")).thenReturn("1");
			when(slingHttpServletRequest.getParameter("appType")).thenReturn("Master App");
			when(slingHttpServletRequest.getParameter("attachmentType")).thenReturn("Attachment");
			when(resolver.adaptTo(Session.class)).thenReturn(session);
			when(resolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
			when(queryBuilder.createQuery(PredicateGroup.create(map), session)).thenReturn(query);
			when(query.getResult()).thenReturn(result);
			when(result.getHits()).thenReturn(hitList);
			when(hit.getPath()).thenReturn("/content/dam/aflacapps/master-app-attchments");
			when(resolver.getResource(hit.getPath())).thenReturn(resource);
			when(resource.adaptTo(Asset.class)).thenReturn(asset);
			when(asset.getName()).thenReturn("Test");
			when(slingHttpServletResponse.getWriter()).thenReturn(printWriter);
			setUpClient(closeableHttpClient, closeableHttpResponse,response);
			masterAppServlet.doGet(slingHttpServletRequest, slingHttpServletResponse);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private void setUpClient(CloseableHttpClient closeableHttpClient, CloseableHttpResponse closeableHttpResponse,String response) throws NoSuchFieldException, IOException {
        InputStream is = new ByteArrayInputStream(response.getBytes());
        HttpEntity httpEntity = mock(HttpEntity.class); 
        when(httpEntity.getContent()).thenReturn(is);
        when(closeableHttpResponse.getEntity()).thenReturn(httpEntity);
        when(httpClient.execute(any())).thenReturn(closeableHttpResponse);
   }
}
