package com.aflac.core.config.impl;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.util.converter.Converters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aflac.core.config.ExperienceApiConfig;
import com.aflac.core.util.ComplianceCase;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Component(service = { ExperienceApiConfig.class })
public class ExperienceApiConfigImpl implements ExperienceApiConfig {

	private transient Logger log = LoggerFactory.getLogger(ExperienceApiConfigImpl.class);

	private String baseApiUrl;
	private String authUrl;
	private String env;
	private String clientId;
	private String clientSecret;
	private static int timeout = 7;

	@Activate
	protected void activate(Map<String, Object> properties) {
		this.baseApiUrl = Converters.standardConverter().convert(properties.get("apiUrl"))
				.defaultValue("https://ndi-d.nt.lab.com:10348/e-doc-ex-1/v1").to(String.class);
		this.authUrl = Converters.standardConverter().convert(properties.get("authUrl")).defaultValue("not found")
				.to(String.class);
		this.env = Converters.standardConverter().convert(properties.get("env")).defaultValue("local").to(String.class);
		this.clientId = Converters.standardConverter().convert(properties.get("clientId")).defaultValue("1")
				.to(String.class);
		this.clientSecret = Converters.standardConverter().convert(properties.get("clientSecret"))
				.defaultValue("secrete").to(String.class);
	}

	@Override
	public String getCaseBuildAccountsUrl(String groupNo, String effDate, String platform) {
		String url =null;
		try {
			url = baseApiUrl + "/accounts?group-number=" + groupNo + "&effective-date=" + effDate + "&platform-name="
					+URLEncoder.encode(platform, "UTF-8").replace("+","%20");
		}
		catch (UnsupportedEncodingException e) {
			log.error("Exception: ", e);
		}
		return url;
		
	}

	@Override
	public String getCaseBuildSaveUrl() {
		return baseApiUrl + "/accounts";
	}
	
	@Override
	public String getAuditSaveUrl() {
		return baseApiUrl + "/activity-details";
	}
	
	@Override
	public String getFormIdUrl(String situs, String series,String groupType,Boolean giAmount) {
		try {
			if(giAmount) {
				return baseApiUrl + "/form-id?situs-state=" + situs + "&series=" + series +"&group-type=" + URLEncoder.encode(groupType, "UTF-8").replace("+","%20")+"&gi-amount="+giAmount;
			}
			else {
				return baseApiUrl + "/form-id?situs-state=" + situs + "&series=" + series +"&group-type=" + URLEncoder.encode(groupType, "UTF-8").replace("+","%20");
			}	
		}catch (UnsupportedEncodingException e) {
			log.error("Exception: ", e);
		}
		return "";
		
	}
	
	@Override
	public String getAgeBasedOnUrl(String lob, String platform,String ageCalculation) {
		try {
			if(ageCalculation.isEmpty())
				return baseApiUrl + "/age-method?lob=" + URLEncoder.encode(lob, "UTF-8").replace("+","%20") + "&platform=" + URLEncoder.encode(platform, "UTF-8").replace("+","%20");
			else
				return baseApiUrl + "/age-method?lob=" + URLEncoder.encode(lob, "UTF-8").replace("+","%20") + "&platform=" + URLEncoder.encode(platform, "UTF-8").replace("+","%20")  +"&age-rating=" + URLEncoder.encode(ageCalculation, "UTF-8").replace("+","%20");
		} catch (UnsupportedEncodingException e) {
			log.error("Exception: ", e);
		}
		return "";
	}
	
	@Override
	public String getIssueAgesUrl(String situs, String series,String term,String lob) {
		try {
			if(term.isEmpty())
				return baseApiUrl + "/age-details?series=" + series + "&situs-state=" + situs + "&lob=" + URLEncoder.encode(lob, "UTF-8").replace("+","%20");
			else
				return baseApiUrl + "/age-details?series=" + series + "&situs-state=" + situs + "&term-length=" + term + "&lob=" + URLEncoder.encode(lob, "UTF-8").replace("+","%20");
		}catch (UnsupportedEncodingException e) {
			log.error("Exception: ", e);
		}
		return "";
	}

	@Override
	public String getCaseBuildPlaformsUrl() {
		//return baseApiUrl + "/platforms?group-number=" + groupNo + "&effective-date=" + effDate;
		return baseApiUrl + "/platforms";
	}

	@Override
	public String getComboAppDataUrl(String situs, String formId) {
		return baseApiUrl + "/situs-series-mapping?situs-state=" + situs + "&form-id=" + formId;
	}

	@Override
	public String getComboAppProductsUrl() {
		return baseApiUrl + "/products";
	}

	@Override
	public String getComboAppSaveUrl() {
		return baseApiUrl + "/situs-series-mapping";
	}

	@Override
	public String getComboAppFormIdsUrl(String situs) {
		return baseApiUrl + "/form-ids?situs-state=" + situs;
	}

	@Override
	public String getComplianceDataUrl(String situsState, String lob, String type, String label,Boolean isCi22kEnabled) {
		StringBuilder url = new StringBuilder(baseApiUrl + "/compliance-verbiage?");
		if (situsState != null && !situsState.isEmpty())
			url.append("situs-state=" + situsState);
		if (lob != null && !lob.isEmpty())
			url.append("&lob=" + lob);
		if (type != null && !type.isEmpty())
			url.append("&type=" + type);
		if (label != null && !label.isEmpty())
			url.append("&label=" + label);
		if(isCi22kEnabled)
			url.append("&is-ci22k-enabled=" + isCi22kEnabled);
		return String.valueOf(url);
	}

	@Override
	public String getComplianceReferenceDataUrl(String referenceItem) {
		return baseApiUrl + "/reference-data?reference-data-item=" + referenceItem;
	}

	@Override
	public String getComplianceSaveUrl() {
		return baseApiUrl + "/compliance-verbiage";
	}

	@Override
	public String getComplianceEditDataUrl(String recordId) {
		return baseApiUrl + "/compliance-verbiage/" + recordId;
	}

	@Override
	public String getCaseBuildComplianceVerbiage(String groupNo, String effDate, String platform,String pdfCase) {
		String url = null;
		try {
			if(pdfCase.equalsIgnoreCase(ComplianceCase.WITHOUTCOMPLIANCE.getValue())) {
				Boolean exceptionCase=true;
				url = baseApiUrl + "/compliance-verbiage?group-number=" + groupNo + "&effective-date=" + effDate + "&platform-name="
						+URLEncoder.encode(platform, "UTF-8").replace("+","%20")+"&exception-case="+exceptionCase;
				
			}else {
				url = baseApiUrl + "/compliance-verbiage?group-number=" + groupNo + "&effective-date=" + effDate + "&platform-name="
						+URLEncoder.encode(platform, "UTF-8").replace("+","%20");
			}
		}catch (UnsupportedEncodingException e) {
			log.error("Exception: ", e);
		}
		return url;
		
	}
	
	@Override
	public String getPdfDirectionsUrl(String situs) {
		return baseApiUrl + "/situs-certificate-language?situs-state="+situs;
	}
	
	@Override
	public String getSaveFilenetMetadataToDBUrl() {
		return baseApiUrl + "/filenet-metadata";
	}
	
	@Override
	public String getBrochureData(String lob, String series, String situs, String lang) throws UnsupportedEncodingException {		
		return baseApiUrl + "/brochure-data?situs-state=" + situs + "&lob=" + URLEncoder.encode(lob, "UTF-8").replace("+","%20") + "&series=" + series + "&language=" + lang;
	}
	
	@Override
	public String getSaveBrochureUrl() {		
		return baseApiUrl + "/brochure-data";
	}
	
	@Override
	public String getPlatformMaintenanceDetailsUrl(String platformId) throws UnsupportedEncodingException {
		return baseApiUrl + "/integration-profile?platform-id=" + URLEncoder.encode(platformId, "UTF-8").replace("+","%20");
	}
	
	@Override
	public String getSavePlatformMaintenanceDetailsUrl() {
		return baseApiUrl + "/integration-profile";
	}
	
	@Override
	public String getDeletePlatformMaintenanceDetailsUrl(String platformId) throws UnsupportedEncodingException {
		return baseApiUrl + "/integration-profile?platform-id=" + URLEncoder.encode(platformId, "UTF-8").replace("+","%20");
  }
  
  @Override
	public String getMasterAppDataUrl(String groupNumber, String effectiveDate) {
		return baseApiUrl + "/master-app-data?group-number-or-gp-id="+ groupNumber + "&effective-date="+effectiveDate;
	}
	
	@Override
	public String getMasterAppSeriesDataUrl(String situs, String groupType, String census) {
		String url = null;
		try {
			if (census != null && !census.isEmpty())
				url = baseApiUrl + "/situs-series-master-app-mapping?situs-state=" + situs +"&census-enrollment=" + census + "&group-type=" + URLEncoder.encode(groupType, "UTF-8").replace("+","%20");
			else
				url = baseApiUrl + "/situs-series-master-app-mapping?situs-state=" + situs + "&group-type=" + URLEncoder.encode(groupType, "UTF-8").replace("+","%20");
		} catch (UnsupportedEncodingException e) {
			log.error("Exception: ", e);
		}
		return url;
	}
	
	@Override
	public String getMasterAppOptionalFeatureDataUrl() {
		return baseApiUrl + "/product-optional-features";
	}
	
	@Override
	public String getMasterAppSaveUrl() {
		return baseApiUrl + "/master-app-data";
	}

	@Override
	public String executeHttpGet(CloseableHttpClient httpclient, HttpGet http) {
		String data = null;
		if (env.equalsIgnoreCase("AEM Cloud"))
			http.addHeader("Authorization", "Bearer " + getApiAccessToken());
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
		http.setConfig(config);
		
		try(final CloseableHttpResponse outputResponse = httpclient.execute(http);) {
			log.info("Api Url:" + http.getURI());
			if (outputResponse.getStatusLine() != null)
				log.info("Status Code:" + outputResponse.getStatusLine().getStatusCode());
			HttpEntity entity = outputResponse.getEntity();
			String result = EntityUtils.toString(entity);
			JsonReader jsonReader = new JsonReader(new StringReader(result));
			JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
			data = jsonObject.get("data").toString();
			outputResponse.close();
			return data;
		} catch (ClientProtocolException e) {
			log.error("Client Protocal Expection in Api Call: ", e);
		} catch (IOException e) {
			log.error("IOException in Api Call: ", e);
		}
		return data;
	}

	@Override
	public String executeHttpPut(CloseableHttpClient httpclient, HttpPut httpPut, String formData) throws UnsupportedEncodingException {
		String apiResponse = null;
		StringEntity stringEntity = new StringEntity(formData);
		log.info("Request Json: "+formData);
		if (env.equalsIgnoreCase("AEM Cloud"))
			httpPut.addHeader("Authorization", "Bearer " + getApiAccessToken());
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
		httpPut.setConfig(config);
		httpPut.getRequestLine();
		httpPut.setEntity(stringEntity);
		log.info("Api Url:" + httpPut.getURI());
		try(final CloseableHttpResponse outputResponse = httpclient.execute(httpPut);)	{
			if (outputResponse.getStatusLine() != null)
				log.info("Status Code:" + outputResponse.getStatusLine().getStatusCode());
			if (outputResponse.getStatusLine().getStatusCode() == 200 || outputResponse.getStatusLine().getStatusCode() == 201) {
				HttpEntity entity = outputResponse.getEntity();
				String result = EntityUtils.toString(entity);
				JsonReader jsonReader = new JsonReader(new StringReader(result));
				JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
				apiResponse = jsonObject.get("data").toString();
			}
			outputResponse.close();
			return apiResponse;
		} catch (IOException e) {
			log.error("IOException in Api Put call: ", e);
		}
		return apiResponse;
	}
	
	@Override
	public String executeHttpPost(CloseableHttpClient httpclient, HttpPost httpPost, String formData) throws UnsupportedEncodingException {
		String apiResponse = null;
		StringEntity stringEntity = new StringEntity(formData);
		if (env.equalsIgnoreCase("AEM Cloud"))
			httpPost.addHeader("Authorization", "Bearer " + getApiAccessToken());
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
		httpPost.setConfig(config);
		httpPost.getRequestLine();
		httpPost.setEntity(stringEntity);
		log.info("Api Url:" + httpPost.getURI());
		try(final CloseableHttpResponse outputResponse = httpclient.execute(httpPost);)	{
			if (outputResponse.getStatusLine() != null)
				log.info("Status Code:" + outputResponse.getStatusLine().getStatusCode());
			if (outputResponse.getStatusLine().getStatusCode() == 201 || outputResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = outputResponse.getEntity();
				String result = EntityUtils.toString(entity);
				JsonReader jsonReader = new JsonReader(new StringReader(result));
				JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
				apiResponse = jsonObject.get("data").toString();
			}
			outputResponse.close();
			return apiResponse;
		} catch (IOException e) {
			log.error("IOException in Api Put call: ", e);
		}
		return apiResponse;
	}
	
	@Override
	public String executeHttpDelete(CloseableHttpClient httpclient, HttpDelete httpDel) throws UnsupportedEncodingException {
		String apiResponse = "";
		
		if (env.equalsIgnoreCase("AEM Cloud"))
			httpDel.addHeader("Authorization", "Bearer " + getApiAccessToken());
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
		httpDel.setConfig(config);
		
		log.info("Api Url:" + httpDel.getURI());
		try(final CloseableHttpResponse outputResponse = httpclient.execute(httpDel);)	{
			if (outputResponse.getStatusLine() != null)
				log.info("Status Code:" + outputResponse.getStatusLine().getStatusCode());
			if (outputResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = outputResponse.getEntity();
				String result = EntityUtils.toString(entity);
				JsonReader jsonReader = new JsonReader(new StringReader(result));
				JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
				apiResponse = jsonObject.get("data").toString();
			}
			outputResponse.close();
			return apiResponse;
		} catch (IOException e) {
			log.error("IOException in Api Put call: ", e);
		}
		return apiResponse;
	}

	@Override
	public String getApiAccessToken() {
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(authUrl);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("grant_type", "client_credentials"));
		nameValuePairs.add(new BasicNameValuePair("client_id", clientId));
		nameValuePairs.add(new BasicNameValuePair("client_secret", clientSecret));

		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8));
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

		try {
			HttpResponse response = httpclient.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			log.info("OAuth Authentication status code is |" + statusLine.getStatusCode());
			String resJson = EntityUtils.toString(response.getEntity(), "UTF-8");
			JsonReader jsonReader = new JsonReader(new StringReader(resJson));
			JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
			log.info("Returning OAuth access_token");
			JsonElement accessTokenElm = jsonObject.get("access_token");
			return accessTokenElm.getAsString();
		} catch (IOException e) {
			log.error("IOException in AccessToken: ", e);
		}
		return null;
	}

}
