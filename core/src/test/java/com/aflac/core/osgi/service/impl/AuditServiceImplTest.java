package com.aflac.core.osgi.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.config.ExperienceApiConfig;
import com.aflac.core.config.impl.ExperienceApiConfigImpl;
import com.aflac.core.experience.api.model.AuditUserActivity;
import com.aflac.core.osgi.service.AuditService;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
public class AuditServiceImplTest {
	
	private static Logger log = LoggerFactory.getLogger(AuditServiceImplTest.class);
	private static AuditService auditService;
	private static AemContext aemContext;
	private static ExperienceApiConfig apiUrlService;
	
	private final AtomicReference<HttpClientContext> clientContext = new AtomicReference<>();
	
	@BeforeAll
	public static void setup() {
		aemContext = new AemContext();
	}
	
	@Test
	public void testSaveAuditEntrySuccess() {
		HttpClientContext httpClientContext = mock(HttpClientContext.class);
        clientContext.set(httpClientContext);
        CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class);
        CloseableHttpResponse closeableHttpResponse = mock(CloseableHttpResponse.class);
		String response = "{\"metadata\": {\"status\": \"success\",\"code\": \"CREATED\",\"descriptions\": [{\"context\": \"success\",\"type\": \"info\",\"code\": \"201 CREATED\",\"short-description\": \"generic.success.message\", \"long-description\": \"Request completed successfully.\" } ] },\"data\": {\"status\": true } }";
		AuditUserActivity audit = new AuditUserActivity();
		audit.setUserId("admin");
		audit.setApplicationId("0000123");
		audit.setApplicationName("Case Build");
		audit.setQueryDateTime("2023-05-25 15:28:13 Asia/Calcutta");
		audit.setCreatedDateTime("2023-05-25T15:30:26");
		try {
			setUpClient(closeableHttpClient, closeableHttpResponse, response);
			StatusLine statusLine = mock(StatusLine.class);
	        when(closeableHttpResponse.getStatusLine()).thenReturn(statusLine);
	        when(statusLine.getStatusCode()).thenReturn(201);
			boolean status = auditService.saveAuditEntry(audit);
			assertEquals(true, status);
		} catch (NoSuchFieldException | IOException e) {
			log.error("Exception in Audit Test: ", e);
		}
	}
	
	@Test
	public void testSaveAuditEntryFailure() {
		HttpClientContext httpClientContext = mock(HttpClientContext.class);
        clientContext.set(httpClientContext);
        CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class);
        CloseableHttpResponse closeableHttpResponse = mock(CloseableHttpResponse.class);
		String response = "{\"metadata\": {\"status\": \"fail\",\"code\": \"CREATED\",\"descriptions\": [{\"context\": \"success\",\"type\": \"info\",\"code\": \"201 CREATED\",\"short-description\": \"generic.success.message\", \"long-description\": \"Request completed successfully.\" } ] },\"data\": {\"status\": false } }";
		AuditUserActivity audit = new AuditUserActivity();
		audit.setUserId("admin");
		audit.setApplicationId("0000123");
		audit.setApplicationName("Case Build");
		audit.setQueryDateTime("2023-05-25 15:28:13 Asia/Calcutta");
		audit.setCreatedDateTime("2023-05-25T15:30:26");
		try {
			setUpClient(closeableHttpClient, closeableHttpResponse, response);
			StatusLine statusLine = mock(StatusLine.class);
	        when(closeableHttpResponse.getStatusLine()).thenReturn(statusLine);
	        when(statusLine.getStatusCode()).thenReturn(201);
			boolean status = auditService.saveAuditEntry(audit);
			assertEquals(false, status);
		} catch (NoSuchFieldException | IOException e) {
			log.error("Exception in Audit Test: ", e);
		}
	}
	
	@Test
	public void testTurnAroundTime() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime start = LocalDateTime.parse("2023-12-01 01:01:10", format);
		LocalDateTime current = LocalDateTime.parse("2023-12-01 01:03:10", format);
		CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
        apiUrlService = aemContext.registerInjectActivateService(new ExperienceApiConfigImpl());
		auditService = aemContext.registerInjectActivateService(new AuditServiceImpl(httpClient, apiUrlService));
		String totalTime = auditService.getTurnAroundTime(current,start);
		assertEquals("0:2:0", totalTime);
	}
	
	private void setUpClient(CloseableHttpClient closeableHttpClient, CloseableHttpResponse closeableHttpResponse,String response) throws NoSuchFieldException, IOException {
        CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
        apiUrlService = aemContext.registerInjectActivateService(new ExperienceApiConfigImpl());
		auditService = aemContext.registerInjectActivateService(new AuditServiceImpl(httpClient, apiUrlService));
        //when(apiUrlService.executeHttpPost(httpClient, null, response)).thenReturn(response);
		when(httpClient.execute(any())).thenReturn(closeableHttpResponse);
        HttpEntity httpEntity = mock(HttpEntity.class);
        when(closeableHttpResponse.getEntity()).thenReturn(httpEntity);
        InputStream is = new ByteArrayInputStream(response.getBytes());
        when(httpEntity.getContent()).thenReturn(is);
        
   }
	
}
