package com.aflac.core.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ExperienceApiConfig {

	public String getCaseBuildAccountsUrl(String groupNo, String effDate, String platform);
	
	public String getCaseBuildSaveUrl();
	
	public String getAuditSaveUrl();
	
	public String getFormIdUrl(String situs, String series,String groupType, Boolean giAmount);
	
	public String getIssueAgesUrl(String situs, String series,String term, String lob);
	
	public String getAgeBasedOnUrl(String platform, String product,String ageCalculation);
	
	public String getCaseBuildPlaformsUrl();
	
	public String getComboAppDataUrl(String situs, String formId);
	
	public String getComboAppProductsUrl();
	
	public String getComboAppSaveUrl();
	
	public String getComboAppFormIdsUrl(String situs);
	
	public String getCaseBuildComplianceVerbiage(String groupNo, String effDate, String platform, String pdfCase);
	
	public String getComplianceDataUrl(String situsState, String lob, String type, String label, Boolean isCi22kEnabled);
	
	public String getComplianceReferenceDataUrl(String referenceItem);
	
	public String executeHttpGet(CloseableHttpClient httpclient, HttpGet http) throws IOException;

	public String getComplianceSaveUrl();

	public String executeHttpPut(CloseableHttpClient httpclient, HttpPut httpPut, String formData) throws IOException;

	public String executeHttpPost(CloseableHttpClient httpclient, HttpPost httpPost, String formData) throws UnsupportedEncodingException;
	
	public String executeHttpDelete(CloseableHttpClient httpclient, HttpDelete httpDel) throws UnsupportedEncodingException;
	
	public String getComplianceEditDataUrl(String recordId);
	
	public String getApiAccessToken();

	public String getPdfDirectionsUrl(String situs);
	
	public String getSaveFilenetMetadataToDBUrl();
	
	public String getBrochureData(String lob, String series, String situs, String lang) throws UnsupportedEncodingException;
	
	public String getSaveBrochureUrl();

	public String getPlatformMaintenanceDetailsUrl(String platformId) throws UnsupportedEncodingException;

	public String getSavePlatformMaintenanceDetailsUrl();
	
	public String getDeletePlatformMaintenanceDetailsUrl(String platformId) throws UnsupportedEncodingException;

	public String getMasterAppDataUrl(String groupNumber, String effectiveDate);

	public String getMasterAppSeriesDataUrl(String situs, String groupType, String census);

	public String getMasterAppOptionalFeatureDataUrl();

	public String getMasterAppSaveUrl();

}
