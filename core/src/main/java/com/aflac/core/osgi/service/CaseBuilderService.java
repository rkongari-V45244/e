package com.aflac.core.osgi.service;

import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.annotation.versioning.ProviderType;

import com.aflac.core.experience.api.model.AuditUserActivity;
import com.aflac.core.experience.api.model.ComplianceVerbiageDetail;
import com.aflac.core.experience.api.model.SaveCasebuildResponse;
import com.aflac.xml.casebuilderProducts.models.CaseBuilderForm;
import com.aflac.xml.casebuilderProducts.models.EnrollmentDetails;
import com.aflac.xml.casebuilderProducts.models.Products;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@ProviderType
public interface CaseBuilderService {

	public CaseBuilderForm getCaseBuilderForm(String groupNo, String effDate, String platform);
	
	public SaveCasebuildResponse saveCaseBuilderForm(String formData, AuditUserActivity audit, String zone);
	
	public EnrollmentDetails getEnrollmentDetails();
	
	public ComplianceVerbiageDetail getComplianceVerbiage(String groupNo, String effDate, String platform, String pdfCase);

	public Products getAvailableProducts(SlingHttpServletRequest request);
	
	public String getFormId(String situs, String series, String groupType, Boolean giAmount);
	
	public String getIssueAges(String situs, String series,String termPeriod, String lob);
	
	public String getAgeBasedOn(String lob, String platform,String ageCalculation);

	public String getPdfDirections(String situs);

	public CaseBuilderForm updateProducts(String caseBuildData, String addedProducts, String deletedProducts) throws JsonMappingException, JsonProcessingException;

	public boolean checkExistingCase(String groupNo, String effDate, String platform);
}
