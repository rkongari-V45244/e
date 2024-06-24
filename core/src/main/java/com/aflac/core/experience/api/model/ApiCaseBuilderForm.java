package com.aflac.core.experience.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiCaseBuilderForm {
	
	@JsonProperty("account-information")
	private ApiAccountInformation accountinformation;

    @JsonProperty("coverage-billing-effective-date")
    private String coveragebillingeffectivedate;

    @JsonProperty("open-enrollment-details")
    private OpenEnrollmentDetails openenrollmentdetails;
    
    @JsonProperty("site-test-info-due-date")	
    private String sitetestinfoduedate;

    @JsonProperty("new-hire-details")
    private NewHireDetails newhiredetails;

    @JsonProperty("implementation-manager")
    private Manager implementationManager;
    
    @JsonProperty("client-manager")
    private Manager clientManager;
    
    @JsonProperty("partner-platform-manager")
    private Manager partnerManager;
    
    @JsonProperty("test-file-naming")
    private String testFileNaming;
    
    @JsonProperty("production-file-naming")
    private String productionFileNaming;
    
    @JsonProperty("production-file-due-date")
    private String productionFileDueDate;
    
    @JsonProperty("test-file-due-date")
    private String testFileDueDate;
    
    @JsonProperty("location-details")
    private LocationDetails locationDetails;
    
    @JsonProperty("new-hire")
    private String newHire;
    
    @JsonProperty("is-ci22k-enabled")
	private Boolean isCi22KEnabled;
    
    private List<ApiProducts> products;
    
    @JsonProperty("version")
    private int version;
    
    @JsonProperty("is-coverage-effective-date-changed")
    private boolean isCoverageEffectiveDateChanged;
	
    
	public String getNewHire() {
		return newHire;
	}
	public void setNewHire(String newHire) {
		this.newHire = newHire;
	}
	public LocationDetails getLocationDetails() {
		return locationDetails;
	}
	public void setLocationDetails(LocationDetails locationDetails) {
		this.locationDetails = locationDetails;
	}
	public ApiAccountInformation getAccountinformation() {
		return accountinformation;
	}
	public void setAccountinformation(ApiAccountInformation accountinformation) {
		this.accountinformation = accountinformation;
	}
	public String getCoveragebillingeffectivedate() {
		return coveragebillingeffectivedate;
	}
	public void setCoveragebillingeffectivedate(String coveragebillingeffectivedate) {
		this.coveragebillingeffectivedate = coveragebillingeffectivedate;
	}
	public OpenEnrollmentDetails getOpenenrollmentdetails() {
		return openenrollmentdetails;
	}
	public void setOpenenrollmentdetails(OpenEnrollmentDetails openenrollmentdetails) {
		this.openenrollmentdetails = openenrollmentdetails;
	}
	public String getSitetestinfoduedate() {
		return sitetestinfoduedate;
	}
	public void setSitetestinfoduedate(String sitetestinfoduedate) {
		this.sitetestinfoduedate = sitetestinfoduedate;
	}
	public NewHireDetails getNewhiredetails() {
		return newhiredetails;
	}
	public void setNewhiredetails(NewHireDetails newhiredetails) {
		this.newhiredetails = newhiredetails;
	}
	public Manager getClientManager() {
		return clientManager;
	}
	public void setClientManager(Manager clientmanager) {
		this.clientManager = clientmanager;
	}
	public Manager getImplementationManager() {
		return implementationManager;
	}
	public void setImplementationManager(Manager implementationManager) {
		this.implementationManager = implementationManager;
	}
	public Manager getPartnerManager() {
		return partnerManager;
	}
	public void setPartnerManager(Manager partnerManager) {
		this.partnerManager = partnerManager;
	}
	public List<ApiProducts> getProducts() {
		return products;
	}
	public void setProducts(List<ApiProducts> products) {
		this.products = products;
	}
	public String getTestFileNaming() {
		return testFileNaming;
	}
	public void setTestFileNaming(String testFileNaming) {
		this.testFileNaming = testFileNaming;
	}
	public String getProductionFileNaming() {
		return productionFileNaming;
	}
	public void setProductionFileNaming(String productionFileNaming) {
		this.productionFileNaming = productionFileNaming;
	}
	public String getProductionFileDueDate() {
		return productionFileDueDate;
	}
	public void setProductionFileDueDate(String productionFileDueDate) {
		this.productionFileDueDate = productionFileDueDate;
	}
	public String getTestFileDueDate() {
		return testFileDueDate;
	}
	public void setTestFileDueDate(String testFileDueDate) {
		this.testFileDueDate = testFileDueDate;
	}
	public Boolean getIsCi22KEnabled() {
		return isCi22KEnabled;
	}
	public void setIsCi22KEnabled(Boolean isCi22KEnabled) {
		this.isCi22KEnabled = isCi22KEnabled;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public boolean isCoverageEffectiveDateChanged() {
		return isCoverageEffectiveDateChanged;
	}
	public void setCoverageEffectiveDateChanged(boolean isCoverageEffectiveDateChanged) {
		this.isCoverageEffectiveDateChanged = isCoverageEffectiveDateChanged;
	}
	
	
}
