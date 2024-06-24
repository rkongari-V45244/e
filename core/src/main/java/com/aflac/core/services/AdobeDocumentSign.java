package com.aflac.core.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

public class AdobeDocumentSign {
	
	private transient Logger log = LoggerFactory.getLogger(AdobeDocumentSign.class);
	private static int timeout = 7;	
	//private static final String SERVICE_USER_ACCOUNT = "aflac-service-user";

	public String getTransientDocumentID(InputStream is) throws IOException {
		log.info("Agreement Signing started");

		HttpPost httpPost = new HttpPost("https://api.in1.adobesign.com/api/rest/v6/transientDocuments");
		httpPost.addHeader("Authorization",
				"Bearer 3AAABLblqZhBc9jRIV9ZTA_uStAC2s41yu-Hgw8oKZJG5LH2dmDhby0iaSnkJutS986KBDh7EFocvgIlZRl4w0pMXJF7r4CcV");
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
		httpPost.setConfig(config);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("File-Name", "Test.pdf", ContentType.TEXT_PLAIN);
		builder.addBinaryBody("File", is, ContentType.APPLICATION_OCTET_STREAM, "Sign Agreement");

		HttpEntity multipart = builder.build();
		httpPost.setEntity(multipart);
		String transientDocumentID = "";
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			CloseableHttpResponse response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			String responseString = EntityUtils.toString(responseEntity, "UTF-8");

			JsonReader jsonReader = new JsonReader(new StringReader(responseString));
			JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
			transientDocumentID = jsonObject.get("transientDocumentId").getAsString();
			log.info("Transient Document ID generated ==> " + transientDocumentID);
		}

		return transientDocumentID;

	}

	public String sendDocumentforSigning(String transientDocumentID, String recipientEmailID) throws IOException {

		HttpPost httpPost = new HttpPost("https://api.in1.adobesign.com/api/rest/v6/agreements");
		httpPost.addHeader("Authorization",
				"Bearer 3AAABLblqZhBc9jRIV9ZTA_uStAC2s41yu-Hgw8oKZJG5LH2dmDhby0iaSnkJutS986KBDh7EFocvgIlZRl4w0pMXJF7r4CcV");
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
		httpPost.setConfig(config);
		String agreementInfoJSON = "{\"fileInfos\":[{\"transientDocumentId\":\"transID\"}],\"name\":\"Group Master Application\",\"participantSetsInfo\":[{\"memberInfos\":[{\"email\":\"outEmail\"}],\"order\":1,\"role\":\"SIGNER\"}],\"signatureType\":\"ESIGN\",\"state\":\"IN_PROCESS\"}";
		agreementInfoJSON = agreementInfoJSON.replaceAll("transID", transientDocumentID);
		agreementInfoJSON = agreementInfoJSON.replaceAll("outEmail", recipientEmailID);
		HttpEntity stringEntity = new StringEntity(agreementInfoJSON, ContentType.APPLICATION_JSON);
		httpPost.setEntity(stringEntity);
		String agreementID = "";
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			CloseableHttpResponse response = httpclient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			String responseString = EntityUtils.toString(responseEntity, "UTF-8");
			log.info("Agreement Document sent for signatures ==> " + responseString);
			
			try {
				
				JsonReader jsonReader = new JsonReader(new StringReader(responseString));
				JsonObject responseData = JsonParser.parseReader(jsonReader).getAsJsonObject();
				agreementID = responseData.get("id").getAsString();
				log.info("agreementID generated ==> " + agreementID);
			} catch (JsonIOException e) {
				throw new IOException("Unable to parse the Json:",e);
			} catch (JsonSyntaxException e) {
				log.error("Error ", e);
			}			
		}
		return agreementID;
	}

	public String saveDocumentafterSigning(String agreementID, String savePath, ResourceResolver resourceResolver) throws IOException {

		String url = "https://api.in1.adobesign.com/api/rest/v6/agreements/" + agreementID + "/combinedDocument";
		URLConnection connection = new URL(url).openConnection();
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);
		connection.setRequestProperty("Authorization",
				"Bearer 3AAABLblqZhBc9jRIV9ZTA_uStAC2s41yu-Hgw8oKZJG5LH2dmDhby0iaSnkJutS986KBDh7EFocvgIlZRl4w0pMXJF7r4CcV");
		InputStream response = connection.getInputStream();
		AssetManager am = resourceResolver.adaptTo(AssetManager.class);
		Asset assetPDF = am.createAsset(savePath + "/" + agreementID +".pdf",response, "application/pdf", true);
		return assetPDF.getPath();
	}

}
