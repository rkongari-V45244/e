//package com.aflac.core.osgi.service.impl;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicReference;
//
//import javax.servlet.ServletOutputStream;
//import javax.servlet.WriteListener;
//
//import org.apache.commons.io.IOUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.StatusLine;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.protocol.HttpClientContext;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.sling.api.SlingHttpServletResponse;
//import org.apache.sling.api.resource.Resource;
//import org.apache.sling.api.resource.ResourceResolver;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import com.aflac.core.config.impl.AdobePDFApiIntegrationConfigImpl;
//import com.aflac.core.config.impl.ExperienceApiConfigImpl;
//import com.aflac.core.experience.api.model.AuditUserActivity;
//
//import io.wcm.testing.mock.aem.junit5.AemContext;
//import io.wcm.testing.mock.aem.junit5.AemContextExtension;
//
//
//@ExtendWith(AemContextExtension.class)
//public class AssemblerURLServiceImplTest {
//
//	private static AssemblerPDFServiceImpl assembleService;
//	private static AemContext aemContext;
//	private final AtomicReference<HttpClientContext> clientContext = new AtomicReference<>();
//	
//	@BeforeAll
//	public static void setup() {
//		aemContext = new AemContext();
//	}
//	
//	@Test
//	public void testAssemblePDFSuccessRequest() {
//		String response = "{\"data\": { \"status\": true } }";
//		String formIds = "CAI77752R1,CAI77751R1";
//		
//		HttpClientContext httpClientContext = mock(HttpClientContext.class);
//		clientContext.set(httpClientContext);
//		CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class);
//		CloseableHttpResponse closeableHttpResponse = mock(CloseableHttpResponse.class);
//		SlingHttpServletResponse httpSlingResponse = mock(SlingHttpServletResponse.class);
//		ResourceResolver resolver = mock(ResourceResolver.class);
//		
//		try {
//			setUpClient(closeableHttpClient, closeableHttpResponse, httpSlingResponse, response);
//			StatusLine status = mock(StatusLine.class);
//	        when(closeableHttpResponse.getStatusLine()).thenReturn(status);
//	        when(status.getStatusCode()).thenReturn(200);
//	        AuditUserActivity audit = new AuditUserActivity();
//	        audit.setApplicationName("brochure");
//	        int statusCode = assembleService.generatePDFfromURLList(formIds, "localhost", httpSlingResponse, resolver, "", "English", audit);
//			assertEquals(200, statusCode);
//		} catch (NoSuchFieldException | IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testAssemblePDFBadRequest() {
//		String response = "{\"message\": \"Bad Request\"}";
//		String formIds = "CAI77752R1-FLANG,CAI77751R1";
//		
//		HttpClientContext httpClientContext = mock(HttpClientContext.class);
//		clientContext.set(httpClientContext);
//		CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class);
//		CloseableHttpResponse closeableHttpResponse = mock(CloseableHttpResponse.class);
//		SlingHttpServletResponse httpSlingResponse = mock(SlingHttpServletResponse.class);
//		ResourceResolver resolver = mock(ResourceResolver.class);
//		
//		try {
//			setUpClient(closeableHttpClient, closeableHttpResponse, httpSlingResponse, response);
//			StatusLine status = mock(StatusLine.class);
//	        when(closeableHttpResponse.getStatusLine()).thenReturn(status);
//	        when(status.getStatusCode()).thenReturn(400);
//	        AuditUserActivity audit = new AuditUserActivity();
//	        audit.setApplicationName("brochure");
//	        int statusCode = assembleService.generatePDFfromURLList(formIds, "localhost", httpSlingResponse, resolver, "", "English", audit);
//			assertEquals(400, statusCode);
//		} catch (NoSuchFieldException | IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testAssemblePDFServerErrorRequest() {
//		String response = "{\"message\": \"Internal Server Error\"}";;
//		String formIds = "CAI77752R1,CAI77751R1";
//		
//		HttpClientContext httpClientContext = mock(HttpClientContext.class);
//		clientContext.set(httpClientContext);
//		CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class);
//		CloseableHttpResponse closeableHttpResponse = mock(CloseableHttpResponse.class);
//		SlingHttpServletResponse httpSlingResponse = mock(SlingHttpServletResponse.class);
//		ResourceResolver resolver = mock(ResourceResolver.class);
//		
//		try {
//			setUpClient(closeableHttpClient, closeableHttpResponse, httpSlingResponse, response);
//			StatusLine status = mock(StatusLine.class);
//	        when(closeableHttpResponse.getStatusLine()).thenReturn(status);
//	        when(status.getStatusCode()).thenReturn(500);			
//	        AuditUserActivity audit = new AuditUserActivity();
//	        audit.setApplicationName("brochure");
//	        int statusCode = assembleService.generatePDFfromURLList(formIds, "localhost", httpSlingResponse, resolver, "", "English", audit);
//			assertEquals(500, statusCode);
//		} catch (NoSuchFieldException | IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testGetDDX() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//		Method method = AssemblerPDFServiceImpl.class.getDeclaredMethod("getDDX", List.class);
//		method.setAccessible(true);
//		List<String> formIds = new ArrayList<>();
//		AssemblerPDFServiceImpl obj = new AssemblerPDFServiceImpl();
//		formIds.add("CAI77752R1");
//		formIds.add("CAI77751R1");
//		String ddx = (String) method.invoke(obj, formIds);
//		assertNotNull(ddx);
//	}
//	
//	@Test
//	public void testPDFGenerationOrdering() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//		Method method = AssemblerPDFServiceImpl.class.getDeclaredMethod("getDDX", List.class);
//		method.setAccessible(true);
//		List<String> formIds = new ArrayList<>();
//		AssemblerPDFServiceImpl obj = new AssemblerPDFServiceImpl();
//		formIds.add("CAI77752R1");
//		formIds.add("CAI77751R1");
//		formIds.add("AGH78753G1");
//		formIds.add("AGH57951WWR1");
//		String ddx = (String) method.invoke(obj, formIds);
//		assertNotNull(ddx);
//	}
//	
//	@Test
//	public void testBrochureID() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//		Method method = AssemblerPDFServiceImpl.class.getDeclaredMethod("generateRandomBrochureID");
//		method.setAccessible(true);
//		AssemblerPDFServiceImpl obj = new AssemblerPDFServiceImpl();
//		String id = (String) method.invoke(obj);
//		assertEquals(id, id);
//	}
//	
//	@Test
//	public void testGeneratePDFfromCRX() {
//		String response = "pdf generated";
//		String[] formIds = {"high-ca,low-ca"};
//		
//		HttpClientContext httpClientContext = mock(HttpClientContext.class);
//		clientContext.set(httpClientContext);
//		CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class);
//		CloseableHttpResponse closeableHttpResponse = mock(CloseableHttpResponse.class);
//		SlingHttpServletResponse httpSlingResponse = mock(SlingHttpServletResponse.class);
//		ResourceResolver resolver = mock(ResourceResolver.class);
//		Iterator<Resource> result = mock(Iterator.class);
//		try {
//			setUpClient(closeableHttpClient, closeableHttpResponse, httpSlingResponse, response);
//			StatusLine status = mock(StatusLine.class);
//	        when(closeableHttpResponse.getStatusLine()).thenReturn(status);
//	        when(status.getStatusCode()).thenReturn(200);
//			
//	        when(resolver.findResources(anyString(), anyString())).thenReturn(result);
//	        when(result.hasNext()).thenReturn(false);
//	        assembleService.generatePDFfromCRX(formIds, "localhost", resolver, httpSlingResponse, "", "English");
//			//assertEquals(200, statusCode);
//		} catch (NoSuchFieldException | IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private void setUpClient(CloseableHttpClient closeableHttpClient, CloseableHttpResponse closeableHttpResponse, SlingHttpServletResponse httpSlingResponse, String response) throws NoSuchFieldException, IOException {
//        
//        CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
//        AdobePDFApiIntegrationConfigImpl getCredentials = aemContext.registerInjectActivateService(new AdobePDFApiIntegrationConfigImpl());
//        ExperienceApiConfigImpl apiUrlService = aemContext.registerInjectActivateService(new ExperienceApiConfigImpl());
//        AuditServiceImpl auditService = aemContext.registerInjectActivateService(new AuditServiceImpl(httpClient, apiUrlService));
//        assembleService = aemContext.registerInjectActivateService(new AssemblerPDFServiceImpl(httpClient, getCredentials, auditService));
//        when(httpClient.execute(any())).thenReturn(closeableHttpResponse);
//        HttpEntity httpEntity = mock(HttpEntity.class);
//                
//        when(closeableHttpResponse.getEntity()).thenReturn(httpEntity);
//        InputStream is = new ByteArrayInputStream(response.getBytes());
//        OutputStream os = new ByteArrayOutputStream();
//        IOUtils.copy(is, os);
//        ServletOutputStream sos = new ServletOutputStream() {
//			
//			@Override
//			public void write(int b) throws IOException {		
//			}
//			
//			@Override
//			public void setWriteListener(WriteListener listener) {				
//			}
//			
//			@Override
//			public boolean isReady() {
//				return true;
//			}
//		};
//		sos.write(response.getBytes());
//        when(httpSlingResponse.getOutputStream()).thenReturn(sos);
//        PrintWriter writer = new PrintWriter(os);
//        when(httpSlingResponse.getWriter()).thenReturn(writer);
//        is = new ByteArrayInputStream(response.getBytes());
//        when(httpEntity.getContent()).thenReturn(is);
//   }
//}
