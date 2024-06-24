package com.aflac.core.osgi.service.impl;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.config.ExperienceApiConfig;
import com.aflac.core.experience.api.model.AuditUserActivity;
import com.aflac.core.osgi.service.AuditService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Component(service = { AuditService.class })
public class AuditServiceImpl implements AuditService {

	private static transient Logger log = LoggerFactory.getLogger(AuditServiceImpl.class);
	private transient CloseableHttpClient httpclient;
	private transient ObjectMapper objectMapper;
	@Reference
	private transient ExperienceApiConfig apiUrlService;
	
	public AuditServiceImpl() { 
		this.httpclient = HttpClients.createDefault();
		this.objectMapper = new ObjectMapper();
	}

	public AuditServiceImpl(CloseableHttpClient httpclient,ExperienceApiConfig apiUrlService) {
		this.httpclient = httpclient;
		this.objectMapper = new ObjectMapper();
		this.apiUrlService = apiUrlService;
	}
	
	@Override
	public boolean saveAuditEntry(AuditUserActivity audit) {
		boolean status = false;
		try {
			String formJson = objectMapper.writeValueAsString(audit);
			log.info(formJson);
			HttpPost httpPost = new HttpPost(apiUrlService.getAuditSaveUrl());
			httpPost.setHeader("Content-type", "application/json");
			String apiResponse = apiUrlService.executeHttpPost(httpclient, httpPost,formJson);
			if(apiResponse != null) {
				JsonReader jsonReader = new JsonReader(new StringReader(apiResponse));
    			JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
    			status = jsonObject.get("status").getAsBoolean();
    			if(status)
    				log.info("Audit Entry added successfully...");
    			else
    				log.info("Save Audit Entry failed...");
			}
			return status;
		} catch (JsonProcessingException | UnsupportedEncodingException e) {
			log.error("Json Exception: ", e);
		}
		return status;
	}
	
	@Override
	public String getTurnAroundTime(LocalDateTime current, LocalDateTime start) {
		Duration duration = Duration.between(start, current);
		long hr = duration.toHours();
		long min = duration.minusHours(hr).toMinutes();
		long sec = duration.minusHours(hr).minusMinutes(min).getSeconds();
		String totalTime = hr + ":" + min + ":" + sec;
		log.info("Totla Time taken: " + totalTime);
		return totalTime;
	}

}
