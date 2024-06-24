package com.aflac.core.config.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.util.converter.Converters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.config.AdobeSignConfig;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Component(service = { AdobeSignConfig.class })
public class AdobeSignConfigImpl implements AdobeSignConfig{
	
	private transient Logger log = LoggerFactory.getLogger(AdobeSignConfigImpl.class);
	
	private String refreshToken;
	private String refreshUrl;
	private String clientId;
	private String clientSecret;
	private String grantType;
	private String baseUrl;
	private int waitTime;
	private String server;

	@Activate
	protected void activate(Map<String, Object> properties) {
		this.refreshUrl = Converters.standardConverter().convert(properties.get("refreshUrl"))
				.defaultValue("https://api.na2.documents.adobe.com/oauth/v2/refresh").to(String.class);
		this.refreshToken = Converters.standardConverter().convert(properties.get("refreshToken")).defaultValue("not found")
				.to(String.class);
		this.grantType = Converters.standardConverter().convert(properties.get("grantType")).defaultValue("refresh_token").to(String.class);
		this.clientId = Converters.standardConverter().convert(properties.get("clientId")).defaultValue("1")
				.to(String.class);
		this.clientSecret = Converters.standardConverter().convert(properties.get("clientSecret"))
				.defaultValue("secrete").to(String.class);
		this.baseUrl = Converters.standardConverter().convert(properties.get("baseUrl"))
				.defaultValue("https://api.na2.echosign.com/api/rest/v2").to(String.class);
		this.waitTime = Converters.standardConverter().convert(properties.get("waitTime"))
				.defaultValue(120000).to(Integer.class);
		this.server = Converters.standardConverter().convert(properties.get("server")).to(String.class);
	}
	
	@Override
	public String getRefreshUrl() {
		return refreshUrl;
	}

	@Override
	public String getRefreshToekn() {
		return refreshToken;
	}

	@Override
	public String getGrantType() {
		return grantType;
	}

	@Override
	public String getClientId() {
		return clientId;
	}

	@Override
	public String getClientSecret() {
		return clientSecret;
	}
	
	@Override
	public String getAdobeSignApiUrl() {
		return baseUrl;
	}
	
	@Override
	public int getWaitTime() {
		return waitTime;
	}
	
	@Override
	public String getServer() {
		return server;
	}
	
	@Override
	public String getAccessToken() {
		log.info("---------Entering into getAccessToken()--------------- ");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		log.info("---------refreshUrl {} --------------- " + refreshUrl);
		HttpPost httpPost = new HttpPost(refreshUrl);
		log.info("---------httpPost {} --------------- " + httpPost);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("refresh_token", refreshToken));
		nameValuePairs.add(new BasicNameValuePair("grant_type", grantType));
		nameValuePairs.add(new BasicNameValuePair("client_id", clientId));
		nameValuePairs.add(new BasicNameValuePair("client_secret", clientSecret));
		log.info("---------nameValuePairs {} --------------- " + nameValuePairs.toString());
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8));
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

		try {
			log.info("---------httpPost {} --------------- " + httpPost);
			HttpResponse response = httpclient.execute(httpPost);
			log.info("---------response {} --------------- " + response);
			StatusLine statusLine = response.getStatusLine();
			log.info("AdobeSign Refresh Token status code is |" + statusLine.getStatusCode());
			//if response is not 200, then it should be handled
			String resJson = EntityUtils.toString(response.getEntity(), "UTF-8");
			log.info("---------resJson {} --------------- " + resJson);
			JsonReader jsonReader = new JsonReader(new StringReader(resJson));
			JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
			log.info("Returning AdobeSign access_token");
			String accessTokenElm = jsonObject.get("access_token").getAsString();
			log.info("---------accessTokenElm {} --------------- " + accessTokenElm);
			//log.info("Access Token: " + accessTokenElm);

			log.info("---------Exiting getAccessToken() with return accessTokenElm {} --------------- ", accessTokenElm);
			return accessTokenElm;
		} catch (IOException e) {
			log.error("IOException in AccessToken: ", e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		log.info("---------Exiting getAccessToken() with return  --------------- ");
		return "";
	}

}
