package com.aflac.core.osgi.service;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.annotation.versioning.ProviderType;

import com.aflac.core.experience.api.model.ApiMasterAppData;
import com.aflac.core.experience.api.model.AuditUserActivity;
import com.aflac.core.experience.api.model.MasterAppSeriesData;
import com.aflac.core.experience.api.model.MasterAppSeriesResponseModel;
import com.aflac.core.experience.api.model.SaveCasebuildResponse;
import com.aflac.xml.masterApp.models.MasterApp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@ProviderType
public interface MasterAppService {
	
	public MasterApp getMasterAppData(String groupNumber, String effectiveDate);

	public MasterAppSeriesResponseModel getMasterAppSeriesData(String situs, String groupType, String census);

	public MasterApp addProducts(String addedProducts);

	public String getOptionalFeatures();

	public SaveCasebuildResponse saveMasterAppData(ApiMasterAppData apiMasterAppData, AuditUserActivity audit, String zone);

	public MasterApp updateMasterAppProducts(String masterAppData, String addedProducts, String deletedProducts) throws JsonMappingException, JsonProcessingException;
	
	public void downloadPdf(String formData, String formId, SlingHttpServletRequest request, 
			SlingHttpServletResponse response, MasterAppSeriesData masterAppSeriesData);

	public void downloadFileFromFileNet(String docId, SlingHttpServletResponse response);
}
