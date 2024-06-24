package com.aflac.core.osgi.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.config.ExperienceApiConfig;
import com.aflac.core.experience.api.model.PlatformMaintenance;
import com.aflac.core.osgi.service.PlatformMaintenanceService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(	service = { PlatformMaintenanceService.class } )
public class PlatformMaintenanceServiceImpl implements PlatformMaintenanceService {

	private transient Logger log = LoggerFactory.getLogger(PlatformMaintenanceServiceImpl.class);
	private transient CloseableHttpClient httpclient;
	private transient ObjectMapper objectMapper;
	@Reference
	private transient ExperienceApiConfig apiUrlService;
	
	@Override
	public PlatformMaintenance getPlatformMaintenanceDetails(String platformId) {
		httpclient = HttpClients.createDefault();
		objectMapper = new ObjectMapper();
		PlatformMaintenance platformDetails = new PlatformMaintenance();
		try{
			HttpGet http = new HttpGet(apiUrlService.getPlatformMaintenanceDetailsUrl(platformId));
			String data = apiUrlService.executeHttpGet(httpclient, http);
			platformDetails = objectMapper.readValue(data, PlatformMaintenance.class);
			if(platformDetails != null) {
				platformDetails.setAbletoratebasedon(platformDetails.getAbletoratebasedon().replace(";#", ","));
				platformDetails.setAgerateformat(platformDetails.getAgerateformat().replace(";#", ","));
				platformDetails.setRateper(platformDetails.getRateper().replace(";#", ","));
				platformDetails.setRatecalculationmethod(platformDetails.getRatecalculationmethod().replace(";#", ","));
			}
			return platformDetails;
		} catch (IOException e1) {
			log.error("Api error: ", e1);
		}
		return platformDetails;
	}
	
	@Override
	public String savePlatformMaintenanceDetails(String formData) {
		String response = "";
		httpclient = HttpClients.createDefault();
		HttpPut httpPut = new HttpPut(apiUrlService.getSavePlatformMaintenanceDetailsUrl());
		httpPut.setHeader("Content-type", "application/json");
		try {
			response = apiUrlService.executeHttpPut(httpclient, httpPut,formData);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return response;
	}
	
	@Override
	public String deletePlatformMaintenanceDetails(String platformId) {
		String response = "";
		httpclient = HttpClients.createDefault();
		HttpDelete httpDel;
		try {
			httpDel = new HttpDelete(apiUrlService.getDeletePlatformMaintenanceDetailsUrl(platformId));
			httpDel.setHeader("Content-type", "application/json");
			response = apiUrlService.executeHttpDelete(httpclient, httpDel);
		} catch (UnsupportedEncodingException e) {
			log.error("Delete Platform Failed: ", e);
		}
		return response;
	}

}
