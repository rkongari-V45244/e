//package com.aflac.core.osgi.service.impl;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.StringReader;
//import java.util.concurrent.atomic.AtomicReference;
//import org.apache.http.HttpEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.protocol.HttpClientContext;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import com.aflac.core.osgi.service.ApiUrlService;
//import com.aflac.core.osgi.service.ComplianceService;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.google.gson.stream.JsonReader;
//import io.wcm.testing.mock.aem.junit5.AemContext;
//import io.wcm.testing.mock.aem.junit5.AemContextExtension;
//
//@ExtendWith(AemContextExtension.class)
//public class ComplianceServiceImplTest {
//	
//	private static ComplianceService service;
//	private static AemContext aemContext;
//	private static ApiUrlService apiUrlService;
//	
//    private final AtomicReference<HttpClientContext> clientContext = new AtomicReference<>();
//	
//	@BeforeAll
//	public static void setup() {
//		aemContext = new AemContext();
//	}
//		
//	@Test
//	public void testInitComplianceForm() {
//		String response = "{\"metadata\":{\"status\":\"success\",\"code\":\"OK\",\"descriptions\":[{\"context\":\"success\",\"type\":\"info\",\"code\":\"200 OK\",\"short-description\":\"generic.success.message\",\"long-description\":\"Request completed successfully.\"}]},\"data\":[{\"id\":\"LABEL\",\"items\":[{\"id\":\"0\",\"value\":\"All\"},{\"id\":\"1\",\"value\":\"Actively At Work\"},{\"id\":\"2\",\"value\":\"Tobacco\"},{\"id\":\"3\",\"value\":\"Spouse Tobacco\"},{\"id\":\"4\",\"value\":\"General Replacement\"},{\"id\":\"5\",\"value\":\"General Replacement Continuation\"},{\"id\":\"6\",\"value\":\"Spouse Disabled\"},{\"id\":\"7\",\"value\":\"Health Questions\"}]},{\"id\":\"LOB\",\"items\":[{\"id\":\"0\",\"value\":\"All\"},{\"id\":\"1\",\"value\":\"Accident\"},{\"id\":\"2\",\"value\":\"BenExtend\"},{\"id\":\"3\",\"value\":\"Critical Illness\"},{\"id\":\"4\",\"value\":\"Hospital Indemnity\"},{\"id\":\"5\",\"value\":\"Worksite Disability\"},{\"id\":\"6\",\"value\":\"Whole Life\"},{\"id\":\"7\",\"value\":\"Term Life\"}]}]}";
//		String exceptedData = "[{\"id\":\"LABEL\",\"items\":[{\"id\":\"0\",\"value\":\"All\"},{\"id\":\"1\",\"value\":\"Actively At Work\"},{\"id\":\"2\",\"value\":\"Tobacco\"},{\"id\":\"3\",\"value\":\"Spouse Tobacco\"},{\"id\":\"4\",\"value\":\"General Replacement\"},{\"id\":\"5\",\"value\":\"General Replacement Continuation\"},{\"id\":\"6\",\"value\":\"Spouse Disabled\"},{\"id\":\"7\",\"value\":\"Health Questions\"}]},{\"id\":\"LOB\",\"items\":[{\"id\":\"0\",\"value\":\"All\"},{\"id\":\"1\",\"value\":\"Accident\"},{\"id\":\"2\",\"value\":\"BenExtend\"},{\"id\":\"3\",\"value\":\"Critical Illness\"},{\"id\":\"4\",\"value\":\"Hospital Indemnity\"},{\"id\":\"5\",\"value\":\"Worksite Disability\"},{\"id\":\"6\",\"value\":\"Whole Life\"},{\"id\":\"7\",\"value\":\"Term Life\"}]}]";
//		HttpClientContext httpClientContext = mock(HttpClientContext.class);
//		clientContext.set(httpClientContext);
//		CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class);
//		CloseableHttpResponse closeableHttpResponse = mock(CloseableHttpResponse.class);
//		String apiResponse = null;
//		String itemTypeList = "LABEL,LOB";
//		try {
//			setUpClient(closeableHttpClient, closeableHttpResponse, response);
//			apiResponse = service.getInitComplianceData(itemTypeList);
//			assertEquals(exceptedData, apiResponse);
//		} catch (NoSuchFieldException | IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
////	@Test
////	public void testFetchComplianceForm() {
////		String response = "{\"metadata\":{\"status\":\"success\",\"code\":\"OK\",\"descriptions\":[{\"context\":\"success\",\"type\":\"info\",\"code\":\"200 OK\",\"short-description\":\"generic.success.message\",\"long-description\":\"Request completed successfully.\"}]},\"data\":[{\"compliance-text\":\"Are you actively at work?\",\"verbiage-type\":{\"id\":\"1\",\"value\":\"Question\"},\"state\":[{\"id\":\"AR\",\"value\":\"Arizona\"}],\"lob\":[{\"id\":\"2\",\"value\":\"Critical Illness\"}],\"label\":{\"id\":\"1\",\"value\":\"Actively At Work\"},\"active-status\":true},{\"compliance-text\":\"Does this coverage replace any existing Aflac individual policy?\",\"verbiage-type\":{\"id\":\"1\",\"value\":\"Question\"},\"state\":[{\"id\":\"AR\",\"value\":\"Arizona\"}],\"lob\":[{\"id\":\"2\",\"value\":\"Critical Illness\"}],\"label\":{\"id\":\"1\",\"value\":\"Actively At Work\"},\"active-status\":true}]}";
////		HttpClientContext httpClientContext = mock(HttpClientContext.class);
////		clientContext.set(httpClientContext);
////		CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class);
////		CloseableHttpResponse closeableHttpResponse = mock(CloseableHttpResponse.class);
////		String apiResponse = null;
////		String situsState = "CA";
////		String lob = null;
////		String type = null;
////		String label = null;
////		try {
////			setUpClient(closeableHttpClient, closeableHttpResponse, response);
////			apiResponse = service.getComplianceData(situsState,lob,type,label);
////			assertNotNull(apiResponse);
////		} catch (NoSuchFieldException | IOException e) {
////			e.printStackTrace();
////		}
////	}
//	
//	@Test
//	public void testAddComplianceForm() {
//		String response = "{\"metadata\":{\"status\":\"success\",\"code\":\"OK\",\"descriptions\":[{\"context\":\"success\",\"type\":\"info\",\"code\":\"200 OK\",\"short-description\":\"generic.success.message\",\"long-description\":\"Request completed successfully.\"}]},\"data\":[{\"compliance-text\":\"Are you actively at work?\",\"verbiage-type\":{\"id\":\"1\",\"value\":\"Question\"},\"state\":[{\"id\":\"AR\",\"value\":\"Arizona\"}],\"lob\":[{\"id\":\"2\",\"value\":\"Critical Illness\"}],\"label\":{\"id\":\"1\",\"value\":\"Actively At Work\"},\"active-status\":true},{\"compliance-text\":\"Does this coverage replace any existing Aflac individual policy?\",\"verbiage-type\":{\"id\":\"1\",\"value\":\"Question\"},\"state\":[{\"id\":\"AR\",\"value\":\"Arizona\"}],\"lob\":[{\"id\":\"2\",\"value\":\"Critical Illness\"}],\"label\":{\"id\":\"1\",\"value\":\"Actively At Work\"},\"active-status\":true}]}";
//		String formData = "{\"record-id\":null,\"compliance-text\":\"Does the person to be insured have comprehensive health benefits from an insurance policy, an HMO plan, an employer health benefit plan, or other coverage that satisfies minimum essential coverage under the Affordable Care Act.Persons without such comprehensive coverage are not eligible for this Group Hospital Indemnity coverage.\",\"verbiage-type\":{\"id\":\"1\",\"value\":\"Question\"},\"state\":[{\"id\":\"CA\",\"value\":\"California\"}],\"lob\":[{\"id\":\"3\",\"value\":\"Hospital Indemnity\"},{\"id\":\"4\",\"value\":\"BenExtend\"}],\"label\":{\"id\":\"2\",\"value\":\"Major Medical\"},\"active-status\":true}";
//		HttpClientContext httpClientContext = mock(HttpClientContext.class);
//		clientContext.set(httpClientContext);
//		CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class);
//		CloseableHttpResponse closeableHttpResponse = mock(CloseableHttpResponse.class);
//		String apiResponse = null;
//		try {
//			setUpClient(closeableHttpClient, closeableHttpResponse, response);
//			apiResponse = service.addComplianceData(formData);
//			assertNotNull(apiResponse);
//		} catch (NoSuchFieldException | IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testUpdateComplianceForm() {
//		String response = "{\"metadata\":{\"status\":\"success\",\"code\":\"OK\",\"descriptions\":[{\"context\":\"success\",\"type\":\"info\",\"code\":\"200 OK\",\"short-description\":\"generic.success.message\",\"long-description\":\"Request completed successfully.\"}]},\"data\":[{\"compliance-text\":\"Are you actively at work?\",\"verbiage-type\":{\"id\":\"1\",\"value\":\"Question\"},\"state\":[{\"id\":\"AR\",\"value\":\"Arizona\"}],\"lob\":[{\"id\":\"2\",\"value\":\"Critical Illness\"}],\"label\":{\"id\":\"1\",\"value\":\"Actively At Work\"},\"active-status\":true},{\"compliance-text\":\"Does this coverage replace any existing Aflac individual policy?\",\"verbiage-type\":{\"id\":\"1\",\"value\":\"Question\"},\"state\":[{\"id\":\"AR\",\"value\":\"Arizona\"}],\"lob\":[{\"id\":\"2\",\"value\":\"Critical Illness\"}],\"label\":{\"id\":\"1\",\"value\":\"Actively At Work\"},\"active-status\":true}]}";
//		String formData = "{\"record-id\":null,\"compliance-text\":\"Does the person to be insured have comprehensive health benefits from an insurance policy, an HMO plan, an employer health benefit plan, or other coverage that satisfies minimum essential coverage under the Affordable Care Act.Persons without such comprehensive coverage are not eligible for this Group Hospital Indemnity coverage.\",\"verbiage-type\":{\"id\":\"1\",\"value\":\"Question\"},\"state\":[{\"id\":\"CA\",\"value\":\"California\"}],\"lob\":[{\"id\":\"3\",\"value\":\"Hospital Indemnity\"},{\"id\":\"4\",\"value\":\"BenExtend\"}],\"label\":{\"id\":\"2\",\"value\":\"Major Medical\"},\"active-status\":true}";
//		HttpClientContext httpClientContext = mock(HttpClientContext.class);
//		clientContext.set(httpClientContext);
//		CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class);
//		CloseableHttpResponse closeableHttpResponse = mock(CloseableHttpResponse.class);
//		String apiResponse = null;
//		try {
//			setUpClient(closeableHttpClient, closeableHttpResponse, response);
//			apiResponse = service.updateComplianceData(formData);
//			assertNotNull(apiResponse);
//		} catch (NoSuchFieldException | IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testEditComplianceForm() {
//		String response = "{\"metadata\":{\"status\":\"success\",\"code\":\"OK\",\"descriptions\":[{\"context\":\"success\",\"type\":\"info\",\"code\":\"200 OK\",\"short-description\":\"generic.success.message\",\"long-description\":\"Request completed successfully.\"}]},\"data\":{\"record-id\":\"1\",\"compliance-text\":\"Are you actively at work?\",\"verbiage-type\":{\"id\":\"1\",\"value\":\"Question\"},\"state\":[{\"id\":\"All\",\"value\":\"All\"}],\"lob\":[{\"id\":\"1\",\"value\":\"Accident\"},{\"id\":\"2\",\"value\":\"Critical Illness 21000\"},{\"id\":\"3\",\"value\":\"Hospital Indemnity\"},{\"id\":\"4\",\"value\":\"BenExtend\"},{\"id\":\"7\",\"value\":\"Term Life\"}],\"label\":{\"id\":\"1\",\"value\":\"Actively At Work\"},\"active-status\":true}}";
//		String recordId = "1";
//		HttpClientContext httpClientContext = mock(HttpClientContext.class);
//		clientContext.set(httpClientContext);
//		CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class);
//		CloseableHttpResponse closeableHttpResponse = mock(CloseableHttpResponse.class);
//		String apiResponse = null;
//		try {
//			setUpClient(closeableHttpClient, closeableHttpResponse, response);
//			apiResponse = service.getEditComplianceData(recordId);
//			assertNotNull(apiResponse);
//		} catch (NoSuchFieldException | IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private void setUpClient(CloseableHttpClient closeableHttpClient, CloseableHttpResponse closeableHttpResponse, String response) throws NoSuchFieldException, IOException {
//        
//        CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
//        apiUrlService = aemContext.registerInjectActivateService(new ApiUrlServiceImpl());
//		service = aemContext.registerInjectActivateService(new ComplianceServiceImpl(httpClient,apiUrlService));
//        when(httpClient.execute(any())).thenReturn(closeableHttpResponse);
//        HttpEntity httpEntity = mock(HttpEntity.class);
//        when(closeableHttpResponse.getEntity()).thenReturn(httpEntity);
//        InputStream is = new ByteArrayInputStream(response.getBytes());
//        when(httpEntity.getContent()).thenReturn(is);
//        
//   }
//}
