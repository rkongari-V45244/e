package com.aflac.core.osgi.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.config.ExperienceApiConfig;
import com.aflac.core.osgi.service.BrochureMaintenanceService;

@Component(
	service = { BrochureMaintenanceService.class }
)
public class BrochureMaintenanceServiceImpl implements BrochureMaintenanceService {
	private transient Logger log = LoggerFactory.getLogger(BrochureMaintenanceServiceImpl.class);
	private transient CloseableHttpClient httpclient;
	@Reference
	private transient ExperienceApiConfig apiUrlService;
	
	public BrochureMaintenanceServiceImpl() {
		httpclient = HttpClients.createDefault();
	}

	public BrochureMaintenanceServiceImpl(CloseableHttpClient httpclient,
			ExperienceApiConfig apiUrlService) {
		super();
		this.httpclient = httpclient;
		this.apiUrlService = apiUrlService;
	}

	@Override
	public String getBrochureData(String lob, String series, String situs, String lang) throws UnsupportedEncodingException {
		HttpGet http = new HttpGet(apiUrlService.getBrochureData(lob, series, situs, lang));
		String apiResponse = "";
		try {
			apiResponse = apiUrlService.executeHttpGet(httpclient, http);
		} catch (IOException e) {
			log.error("getBrochureData Api Error: ", e);
		}		
		return apiResponse;
	}
	
	@Override
	public String saveBrochureData(String data) {
		log.info("data: " + data);
		HttpPut httpPut = new HttpPut(apiUrlService.getSaveBrochureUrl());
		httpPut.setHeader("Content-type", "application/json");
		String apiResponse = null;
		try {
			apiResponse = apiUrlService.executeHttpPut(httpclient, httpPut,data);
		} catch (IOException e) {
			log.error("Error while saving Brochure Data: ", e);
		}
		return apiResponse;
	}
}
