//
//package com.aflac.core.osgi.service.impl;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
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
//import com.aflac.core.experience.api.model.ComboAppMatrixModel;
//import com.aflac.core.osgi.service.ApiUrlService;
//import com.aflac.core.osgi.service.CaseBuilderService;
//import com.aflac.core.osgi.service.ComboAppMatrixService;
//import com.aflac.xml.casebuilderProducts.models.CaseBuilderForm;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.google.gson.stream.JsonReader;
//import io.wcm.testing.mock.aem.junit5.AemContext;
//import io.wcm.testing.mock.aem.junit5.AemContextExtension;
//
//@ExtendWith(AemContextExtension.class)
//public class ComboAppMatrixServiceImplTest {
//	
//	private static ComboAppMatrixService service;
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
//	public void testComboAppMatrixForm() {
//		String response = "{\"metadata\":{\"status\":\"success\",\"code\":\"OK\",\"descriptions\":[{\"context\":\"success\",\"type\":\"info\",\"code\":\"200 OK\",\"short-description\":\"generic.success.message\",\"long-description\":\"Request completed successfully.\"}]},\"data\":{\"situs-state\":\"CW\",\"form-id\":\"C02205CO\",\"products\":[{\"id\":\"CI\",\"name\":\"Critical Illness\",\"series-details\":[{\"series-name\":\"2100\",\"availability\":false},{\"series-name\":\"2800\",\"availability\":false},{\"series-name\":\"20000\",\"availability\":false},{\"series-name\":\"21000\",\"availability\":false}]},{\"id\":\"HI\",\"name\":\"Hospital Indemnity\",\"series-details\":[{\"series-name\":\"8500\",\"availability\":false},{\"series-name\":\"8800\",\"availability\":false},{\"series-name\":\"80000\",\"availability\":false}]},{\"id\":\"AC\",\"name\":\"Accident\",\"series-details\":[{\"series-name\":\"7700\",\"availability\":false},{\"series-name\":\"7800\",\"availability\":false},{\"series-name\":\"70000\",\"availability\":false}]},{\"id\":\"WD\",\"name\":\"Worklife Disability\",\"series-details\":[{\"series-name\":\"5000\",\"availability\":false},{\"series-name\":\"50000\",\"availability\":false}]},{\"id\":\"BE\",\"name\":\"BenExtend\",\"series-details\":[{\"series-name\":\"81000\",\"availability\":false},{\"series-name\":\"82000\",\"availability\":false}]}]}}";
//		HttpClientContext httpClientContext = mock(HttpClientContext.class);
//		clientContext.set(httpClientContext);
//		CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class);
//		CloseableHttpResponse closeableHttpResponse = mock(CloseableHttpResponse.class);
//		ObjectMapper objectMapper = new ObjectMapper();
//		String formID = "C02205CO";
//		String situs = "CW";
//		String sampleData = null;
//		try {
//			setUpClient(closeableHttpClient, closeableHttpResponse, response);
//			sampleData = service.getComboAppData(situs, formID);
//			ComboAppMatrixModel comboAppMatrixModel = objectMapper.readValue(sampleData, ComboAppMatrixModel.class);
//			assertEquals(situs, comboAppMatrixModel.getSitusState());
//		} catch (NoSuchFieldException | IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testComboAppMatrixAdd() {
//		String response = "{\r\n\"metadata\": {\r\n\"status\": \"success\",\r\n\"code\": \"OK\",\r\n\"descriptions\": [\r\n{\r\n\"context\": \"success\",\r\n\"type\": \"info\",\r\n\"code\": \"200 OK\",\r\n\"short-description\": \"generic.success.message\",\r\n\"long-description\": \"Request completed successfully.\"\r\n}\r\n]\r\n},\r\n\"data\": [\r\n{\r\n\"product-id\": \"CI\",\r\n\"product-name\": \"Critical Illness\",\r\n\"series-list\": [\r\n\"2100\",\r\n\"2800\",\r\n\"20000\",\r\n\"21000\"\r\n]\r\n},\r\n{\r\n\"product-id\": \"HI\",\r\n\"product-name\": \"Hospital Indemnity\",\r\n\"series-list\": [\r\n\"8500\",\r\n\"8800\",\r\n\"80000\"\r\n]\r\n},\r\n{\r\n\"product-id\": \"AC\",\r\n\"product-name\": \"Accident\",\r\n\"series-list\": [\r\n\"7700\",\r\n\"7800\",\r\n\"70000\"\r\n]\r\n},\r\n{\r\n\"product-id\": \"WD\",\r\n\"product-name\": \"Worklife Disability\",\r\n\"series-list\": [\r\n\"5000\",\r\n\"50000\"\r\n]\r\n},\r\n{\r\n\"product-id\": \"BE\",\r\n\"product-name\": \"BenExtend\",\r\n\"series-list\": [\r\n\"81000\",\r\n\"82000\"\r\n]\r\n}\r\n]\r\n}";
//		String exceptedData = "[{\"product-id\":\"CI\",\"product-name\":\"Critical Illness\",\"series-list\":[\"2100\",\"2800\",\"20000\",\"21000\"]},{\"product-id\":\"HI\",\"product-name\":\"Hospital Indemnity\",\"series-list\":[\"8500\",\"8800\",\"80000\"]},{\"product-id\":\"AC\",\"product-name\":\"Accident\",\"series-list\":[\"7700\",\"7800\",\"70000\"]},{\"product-id\":\"WD\",\"product-name\":\"Worklife Disability\",\"series-list\":[\"5000\",\"50000\"]},{\"product-id\":\"BE\",\"product-name\":\"BenExtend\",\"series-list\":[\"81000\",\"82000\"]}]";
//		HttpClientContext httpClientContext = mock(HttpClientContext.class);
//		clientContext.set(httpClientContext);
//		CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class);
//		CloseableHttpResponse closeableHttpResponse = mock(CloseableHttpResponse.class);
//		String sampleData = null;
//		try {
//			setUpClient(closeableHttpClient, closeableHttpResponse, response);
//			sampleData = service.getComboAppAddData();
//			JsonReader jsonReader = new JsonReader(new StringReader(sampleData));
//			JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
//			String actualData = jsonObject.get("data").toString();
//			assertEquals(exceptedData, actualData);
//		} catch (NoSuchFieldException | IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testComboAppSaveFormFound() {
//		String response = "{\"metadata\":{\"status\":\"success\",\"code\":\"OK\",\"descriptions\":[{\"context\":\"success\",\"type\":\"info\",\"code\":\"200 OK\",\"short-description\":\"generic.success.message\",\"long-description\":\"Request completed successfully.\"}]},\"data\":{\"update-status\":true,\"form-found-status\":true,\"available-form-list\":null}}";
//		String formData = "{\"situs-state\":\"CZ\",\"form-id\":\"C02205CO5\",\"products\":[{\"id\":\"AC\",\"name\":\"string\",\"series-details\":[{\"series-name\":\"7001\",\"availability\":false},{\"series-name\":\"7002\",\"availability\":true}]},{\"id\":\"CI\",\"name\":\"string\",\"series-details\":[{\"series-name\":\"8002\",\"availability\":true}]},{\"id\":\"HI\",\"name\":\"string\",\"series-details\":[{\"series-name\":\"9002\",\"availability\":true}]}]}";
//		HttpClientContext httpClientContext = mock(HttpClientContext.class);
//		clientContext.set(httpClientContext);
//		CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class);
//		CloseableHttpResponse closeableHttpResponse = mock(CloseableHttpResponse.class);
//		String sampleData = null;
//		try {
//			setUpClient(closeableHttpClient, closeableHttpResponse, response);
//			sampleData = service.saveComboAppData(formData);
//			JsonReader jsonReader = new JsonReader(new StringReader(sampleData));
//			JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
//			Boolean formFoundtaus = jsonObject.get("form-found-status").getAsBoolean();
//			assertEquals(true, formFoundtaus);
//		} catch (NoSuchFieldException | IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testComboAppFormIds() {
//		String response = "{\"metadata\":{\"status\":\"success\",\"code\":\"OK\",\"descriptions\":[{\"context\":\"success\",\"type\":\"info\",\"code\":\"200 OK\",\"short-description\":\"generic.success.message\",\"long-description\":\"Request completed successfully.\"}]},\"data\":[{\"form-id\":\"C01110AZ\"},{\"form-id\":\"C02205AZ\"},{\"form-id\":\"C12345AZ\"},{\"form-id\":\"C22202\"}]}";
//		String situs = "CA";
//		HttpClientContext httpClientContext = mock(HttpClientContext.class);
//		clientContext.set(httpClientContext);
//		CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class);
//		CloseableHttpResponse closeableHttpResponse = mock(CloseableHttpResponse.class);
//		String apiResponse = null;
//		try {
//			setUpClient(closeableHttpClient, closeableHttpResponse, response);
//			apiResponse = service.getFormIds(situs);
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
//		service = aemContext.registerInjectActivateService(new ComboAppMatrixServiceImpl(httpClient,apiUrlService));
//        when(httpClient.execute(any())).thenReturn(closeableHttpResponse);
//        HttpEntity httpEntity = mock(HttpEntity.class);
//        when(closeableHttpResponse.getEntity()).thenReturn(httpEntity);
//        InputStream is = new ByteArrayInputStream(response.getBytes());
//        when(httpEntity.getContent()).thenReturn(is);
//        
//   }
//}
//
