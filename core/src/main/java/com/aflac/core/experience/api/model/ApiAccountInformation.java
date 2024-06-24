package com.aflac.core.experience.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ApiAccountInformation
{
    @JsonProperty("account-name")
    private String accountName;
    
    @JsonProperty("group-number")
    private String groupNumber;
    
    @JsonProperty("group-type")
    private String groupType;

	@JsonProperty("situs-state")
    private String situsstate;

    @JsonProperty("no-of-eligible-employee")
    private int noofeligibleemployee;
    
    @JsonProperty("hours-worked-per-week")
    private int hoursworkedperweek ;
    
    @JsonProperty("eligibility-waiting-period")
    private String eligibilitywaitingperiod;

    @JsonProperty("ssn-or-eeid")
    private String ssnoreeid;

    @JsonProperty("aflac-ease")
    private boolean aflacease;

    @JsonProperty("deduction-frequency")
    private List<String> deductionfrequency;

    @JsonProperty("are-demestic-partners-eligible")
    private boolean aredemesticpartnerseligible;
    private String locations;
    private String platform;

    @JsonProperty("product-sold")
    private List<String> productsold;
    
    @JsonProperty("enrollment-condition-options")
    private List<String> enrollmentConditionOptions;
    
    @JsonProperty("pdf-directions")
    private String pdfDirections;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}
	
	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getSitusstate() {
		return situsstate;
	}

	public void setSitusstate(String situsstate) {
		this.situsstate = situsstate;
	}

	public int getNoofeligibleemployee() {
		return noofeligibleemployee;
	}

	public void setNoofeligibleemployee(int noofeligibleemployee) {
		this.noofeligibleemployee = noofeligibleemployee;
	}

	public String getSsnoreeid() {
		return ssnoreeid;
	}

	public void setSsnoreeid(String ssnoreeid) {
		this.ssnoreeid = ssnoreeid;
	}

	public boolean isAflacease() {
		return aflacease;
	}

	public void setAflacease(boolean aflacease) {
		this.aflacease = aflacease;
	}

	public List<String> getDeductionfrequency() {
		return deductionfrequency;
	}

	public void setDeductionfrequency(List<String> deductionfrequency) {
		this.deductionfrequency = deductionfrequency;
	}

	public boolean isAredemesticpartnerseligible() {
		return aredemesticpartnerseligible;
	}

	public void setAredemesticpartnerseligible(boolean aredemesticpartnerseligible) {
		this.aredemesticpartnerseligible = aredemesticpartnerseligible;
	}

	public String getLocations() {
		return locations;
	}

	public void setLocations(String locations) {
		this.locations = locations;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public List<String> getProductsold() {
		return productsold;
	}

	public void setProductsold(List<String> productsold) {
		this.productsold = productsold;
	}

	public List<String> getEnrollmentConditionOptions() {
		return enrollmentConditionOptions;
	}

	public void setEnrollmentConditionOptions(List<String> enrollmentConditionOptions) {
		this.enrollmentConditionOptions = enrollmentConditionOptions;
	}

	public String getPdfDirections() {
		return pdfDirections;
	}

	public void setPdfDirections(String pdfDirections) {
		this.pdfDirections = pdfDirections;
	}

	public int getHoursworkedperweek() {
		return hoursworkedperweek;
	}

	public void setHoursworkedperweek(int hoursworkedperweek) {
		this.hoursworkedperweek = hoursworkedperweek;
	}

	public String getEligibilitywaitingperiod() {
		return eligibilitywaitingperiod;
	}

	public void setEligibilitywaitingperiod(String eligibilitywaitingperiod) {
		this.eligibilitywaitingperiod = eligibilitywaitingperiod;
	}
	
	
}