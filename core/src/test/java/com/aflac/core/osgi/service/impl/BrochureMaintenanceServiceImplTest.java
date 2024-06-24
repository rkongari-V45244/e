package com.aflac.core.osgi.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.aflac.core.config.ExperienceApiConfig;
import com.aflac.core.config.impl.ExperienceApiConfigImpl;
import com.aflac.core.servlets.BrochureMaintenanceServlet;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
public class BrochureMaintenanceServiceImplTest {

	private static AemContext aemContext;
	private static ExperienceApiConfig apiUrlService;
	private static BrochureMaintenanceServiceImpl service;
	private static BrochureMaintenanceServlet brochure;
	private static CloseableHttpClient httpClient;
	private static PrintWriter printWriter;
	private static CloseableHttpClient closeableHttpClient; 
	private static CloseableHttpResponse closeableHttpResponse;
    private final AtomicReference<HttpClientContext> clientContext = new AtomicReference<>();
	
	@BeforeAll
	public static void setup() {
		aemContext = new AemContext();
		httpClient = mock(CloseableHttpClient.class);
        apiUrlService = aemContext.registerInjectActivateService(new ExperienceApiConfigImpl());
        service = aemContext.registerInjectActivateService(new BrochureMaintenanceServiceImpl(httpClient,apiUrlService));
        brochure = new BrochureMaintenanceServlet(service);
        printWriter = mock(PrintWriter.class);
		closeableHttpClient = mock(CloseableHttpClient.class);
        closeableHttpResponse = mock(CloseableHttpResponse.class);
	}
	
	@Test
	public void testApplicationNumber() {
		String situs = "MA";
		String series = "7800";
		String lob = "Accident";
		String lang = "English";
		String response = "{\"metadata\":{\"status\":\"success\",\"code\":\"OK\",\"descriptions\":[{\"context\":\"success\",\"type\":\"info\",\"code\":\"200 OK\",\"short-description\":\"generic.success.message\",\"long-description\":\"Request completed successfully.\"}]},\"data\":{\"form-id\":\"C02205.1MA\"}}";
        SlingHttpServletRequest slingHttpServletRequest=mock(SlingHttpServletRequest.class);
    	SlingHttpServletResponse slingHttpServletResponse = mock(SlingHttpServletResponse.class);
        try {
			when(slingHttpServletRequest.getParameter("lob")).thenReturn(lob);
			when(slingHttpServletRequest.getParameter("situs")).thenReturn(situs);
			when(slingHttpServletRequest.getParameter("series")).thenReturn(series);
			when(slingHttpServletRequest.getParameter("language")).thenReturn(lang);
			when(slingHttpServletResponse.getWriter()).thenReturn(printWriter);
			setUpClient(closeableHttpClient, closeableHttpResponse,response);
			brochure.doGet(slingHttpServletRequest, slingHttpServletResponse);
		} catch (Exception e) {
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
