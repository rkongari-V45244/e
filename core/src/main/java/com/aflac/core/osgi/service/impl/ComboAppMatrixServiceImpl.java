package com.aflac.core.osgi.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.config.ExperienceApiConfig;
import com.aflac.core.experience.api.model.ComboAppMatrixModel;
import com.aflac.core.experience.api.model.FormIdsApiModel;
import com.aflac.core.experience.api.model.FormIdsSitusDetail;
import com.aflac.core.osgi.service.ComboAppMatrixService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Component(
	service = { ComboAppMatrixService.class }
)
public class ComboAppMatrixServiceImpl implements ComboAppMatrixService,Serializable {
	
	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(CaseBuilderServiceImpl.class);
	private transient CloseableHttpClient httpclient;
	@Reference
	private transient ExperienceApiConfig apiUrlService;
	
	public ComboAppMatrixServiceImpl() { 
		this.httpclient = HttpClients.createDefault();
	}

	public ComboAppMatrixServiceImpl(CloseableHttpClient httpclient,ExperienceApiConfig apiUrlService) {
		this.httpclient = httpclient;
		this.apiUrlService = apiUrlService;
	}

	@Override
	public String getComboAppData(String situs, String formID) {
		ObjectMapper objectMapper = new ObjectMapper();
		HttpGet http = new HttpGet(apiUrlService.getComboAppDataUrl(situs, formID));
		String sampleData = null;
		try {
			//String result = "{\"data\":{\"situs-state\":\"CW\",\"form-number\":\"C02205CO\",\"products\":[{\"id\":\"CI\",\"name\":\"Critical Illness\",\"series-details\":[{\"series-name\":\"2100\",\"availability\":false},{\"series-name\":\"2800\",\"availability\":false},{\"series-name\":\"20000\",\"availability\":false},{\"series-name\":\"21000\",\"availability\":false}]},{\"id\":\"HI\",\"name\":\"Hospital Indemnity\",\"series-details\":[{\"series-name\":\"8500\",\"availability\":false},{\"series-name\":\"8800\",\"availability\":false},{\"series-name\":\"80000\",\"availability\":false}]},{\"id\":\"AC\",\"name\":\"Accident\",\"series-details\":[{\"series-name\":\"7700\",\"availability\":false},{\"series-name\":\"7800\",\"availability\":false},{\"series-name\":\"70000\",\"availability\":false}]},{\"id\":\"WD\",\"name\":\"Worklife Disability\",\"series-details\":[{\"series-name\":\"5000\",\"availability\":false},{\"series-name\":\"50000\",\"availability\":false}]},{\"id\":\"BE\",\"name\":\"BenExtend\",\"series-details\":[{\"series-name\":\"81000\",\"availability\":false},{\"series-name\":\"82000\",\"availability\":false}]}]}}";
			String data = apiUrlService.executeHttpGet(httpclient, http);
			ComboAppMatrixModel comboAppMatrixModel = objectMapper.readValue(data, ComboAppMatrixModel.class);
			sampleData = objectMapper.writeValueAsString(comboAppMatrixModel);
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		return sampleData;
	}

	@Override
	public String getMasterAppData(String situs, String formID) {
		String sampleData;
		
		if (situs.equals("ca")) {
			sampleData = "{ \"situs-state\": \"AZ\", \"form-number\": \"C02204\", \"products\": [ { \"id\": 0, \"name\": \"Accident\", \"series-details\": [ { \"series-name\": \"7000\", \"availability\": true }, { \"series-name\": \"7500\", \"availability\": false }, { \"series-name\": \"7700\", \"availability\": true } ] }, { \"id\": 0, \"name\": \"Critical Illness\", \"series-details\": [ { \"series-name\": \"7000\", \"availability\": false }, { \"series-name\": \"8700\", \"availability\": true } ] } ] }";
		} else {
			sampleData = "{ \"situs-state\": \"AZ\", \"form-number\": \"C02204\", \"products\": [ { \"id\": 0, \"name\": \"Critical Illness\", \"series-details\": [ { \"series-name\": \"7000\", \"availability\": true }, { \"series-name\": \"7700\", \"availability\": false } ] }, { \"id\": 0, \"name\": \"Accident\", \"series-details\": [ { \"series-name\": \"7000\", \"availability\": false }, { \"series-name\": \"8700\", \"availability\": false } ] } ] }";
		}
		return sampleData;
	}

	@Override
	public String getComboAppAddData() {
		HttpGet http = new HttpGet(apiUrlService.getComboAppProductsUrl());
		String result = "";
		try {
			CloseableHttpResponse outputResponse = httpclient.execute(http);
			HttpEntity entity = outputResponse.getEntity();
			result = EntityUtils.toString(entity);
			return result;
		} catch (IOException e) {
			log.error(e.getMessage());
		}	
		//result = "{\r\n\"metadata\": {\r\n\"status\": \"success\",\r\n\"code\": \"OK\",\r\n\"descriptions\": [\r\n{\r\n\"context\": \"success\",\r\n\"type\": \"info\",\r\n\"code\": \"200 OK\",\r\n\"short-description\": \"generic.success.message\",\r\n\"long-description\": \"Request completed successfully.\"\r\n}\r\n]\r\n},\r\n\"data\": [\r\n{\r\n\"product-id\": \"CI\",\r\n\"product-name\": \"Critical Illness\",\r\n\"series-list\": [\r\n\"2100\",\r\n\"2800\",\r\n\"20000\",\r\n\"21000\"\r\n]\r\n},\r\n{\r\n\"product-id\": \"HI\",\r\n\"product-name\": \"Hospital Indemnity\",\r\n\"series-list\": [\r\n\"8500\",\r\n\"8800\",\r\n\"80000\"\r\n]\r\n},\r\n{\r\n\"product-id\": \"AC\",\r\n\"product-name\": \"Accident\",\r\n\"series-list\": [\r\n\"7700\",\r\n\"7800\",\r\n\"70000\"\r\n]\r\n},\r\n{\r\n\"product-id\": \"WD\",\r\n\"product-name\": \"Worklife Disability\",\r\n\"series-list\": [\r\n\"5000\",\r\n\"50000\"\r\n]\r\n},\r\n{\r\n\"product-id\": \"BE\",\r\n\"product-name\": \"BenExtend\",\r\n\"series-list\": [\r\n\"81000\",\r\n\"82000\"\r\n]\r\n}\r\n]\r\n}";
		return result;
	}
	
	@Override
	public String saveComboAppData(String formData) {
		HttpPut httpPut = new HttpPut(apiUrlService.getComboAppSaveUrl());
		httpPut.setHeader("Content-type", "application/json");
		String response = null;
		try {
			StringEntity stringEntity = new StringEntity(formData);
			httpPut.getRequestLine();
			httpPut.setEntity(stringEntity);
			CloseableHttpResponse outputResponse = httpclient.execute(httpPut);
			HttpEntity entity = outputResponse.getEntity();
			String result = EntityUtils.toString(entity);
			JsonReader jsonReader = new JsonReader(new StringReader(result));
			JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
			response = jsonObject.get("data").toString();
			return response;
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	@Override
	public String getFormIds(String situsState) {
		HttpGet http = new HttpGet(apiUrlService.getComboAppFormIdsUrl(situsState));
		String apiResponse = null;
		ObjectMapper objectMapper = new ObjectMapper();
		List<String> formIdsList = new ArrayList<String>();
		try {
			apiResponse = apiUrlService.executeHttpGet(httpclient, http);
			FormIdsApiModel formIdsApiModel = objectMapper.readValue(apiResponse, FormIdsApiModel.class);
			for(FormIdsSitusDetail formIds:formIdsApiModel) {
				formIdsList.add(formIds.getFormId());
			}
			apiResponse = objectMapper.writeValueAsString(formIdsList);
		} catch (JsonMappingException e) {
			log.error("Error ", e);
		} catch (JsonProcessingException e) {
			log.error("Error ", e);
		} catch (IOException e) {
			log.error("IOException ", e);
		}
		return apiResponse;
	}

}
