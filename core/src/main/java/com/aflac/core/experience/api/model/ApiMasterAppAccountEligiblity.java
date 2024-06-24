package com.aflac.core.experience.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiMasterAppAccountEligiblity {
	
	@JsonProperty("group-number-or-gp-id")
	private String groupNumberOrGpId;
	
	@JsonProperty("effective-date")
	private String effectiveDate;
	
	@JsonProperty("organization-name")
	private String organizationName;
	
	@JsonProperty("city-of-hq")
	private String cityOfHq;
	
	@JsonProperty("subsidiaries-affiliates")
	private String subsidiariesAffiliates;
	
	@JsonProperty("cert-holders")
	private String certHolders;
	
	@JsonProperty("hour-per-week-full-time")
	private String hourPerWeekFullTime;
	
	@JsonProperty("hour-per-week-part-time")
	private String hourPerWeekpartTime;
	
	@JsonProperty("first-of-the-month")
	private String firstOftheMonth;
	
	@JsonProperty("full-time-or-part-time")
    private String fullTimeOrPartTime;
	
	@JsonProperty("full-time-eligible-coverage-duration")
    private String fullTimeEligibleCoverageDuration;
	
	@JsonProperty("part-time-eligible-coverage-duration")
    private String partTimeEligibleCoverageDuration;
	
	@JsonProperty("full-time-eligible-coverage-month")
    private String fullTimeEligibleCoverageMonth;
	
	@JsonProperty("part-time-eligible-coverage-month")
    private String partTimeEligibleCoverageMonth;
	
	@JsonProperty("enroll-employee-count")
	private int enrollEmployeeCount;
	
	@JsonProperty("eligible-employee-count")
	private int eligibleEmployeeCount;
	
	@JsonProperty("other-requirement")
	private String otherRequirement;
	
	@JsonProperty("purpose-of-application")
	private String purposeOfApplication;
	
	@JsonProperty("classes-added-or-removed")
	private String classesAddedOrRemoved;
	
	@JsonProperty("policy-numbers")
	private String policyNumbers;
	
	@JsonProperty("employer-tax-number")
	private String employerTaxNumber;
	
	@JsonProperty("applicant")
	private String applicant;
	
	@JsonProperty("applicant-other")
	private String applicantOther;

	@JsonProperty("sic-code")
	private String sicCode;
	
	@JsonProperty("address")
	private String address;
	
	@JsonProperty("number-or-street")
	private String numberOrStreet;
	
	@JsonProperty("city")
	private String city;
	
	@JsonProperty("state")
	private String state;
	
	@JsonProperty("zip")
	private String zip;
	
	@JsonProperty("physical-address")
	private String physicalAddress;
	
	@JsonProperty("physical-number-or-street")
	private String physicalNumberOrStreet;
	
	@JsonProperty("physical-city")
	private String physicalCity;
	
	@JsonProperty("physical-state")
	private String physicalState;
	
	@JsonProperty("physical-zip")
	private String physicalZip;
	
	@JsonProperty("authorized-person-name")
	private String authorizedPersonName;
	
	@JsonProperty("authorized-person-title")
	private String authorizedPersonTitle;
	
	@JsonProperty("contact-name")
	private String contactName;
	
	@JsonProperty("contact-title")
	private String contactTitle;
	
	@JsonProperty("contact-phone")
	private String contactPhone;
	
	@JsonProperty("contact-email")
	private String contactEmail;
	
	@JsonProperty("erisa-plan")
	private String erisaPlan;
	
	@JsonProperty("plan-number")
	private String planNumber;
	
	@JsonProperty("church-plan")
	private String churchPlan;
	
	@JsonProperty("sad-to-be-covered")
	private String sadToBeCovered;
	
	@JsonProperty("sad-names")
	private String sadNames;
	
	public String getGroupNumberOrGpId() {
		return groupNumberOrGpId;
	}

	public void setGroupNumberOrGpId(String groupNumberOrGpId) {
		this.groupNumberOrGpId = groupNumberOrGpId;
	}
	
	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getCity() {
		return cityOfHq;
	}

	public void setCity(String cityOfHq) {
		this.cityOfHq = cityOfHq;
	}

	public String getSubsidiariesAffiliates() {
		return subsidiariesAffiliates;
	}

	public void setSubsidiariesAffiliates(String subsidiariesAffiliates) {
		this.subsidiariesAffiliates = subsidiariesAffiliates;
	}

	public String getCertHolders() {
		return certHolders;
	}

	public void setCertHolders(String certHolders) {
		this.certHolders = certHolders;
	}

	public String getHourPerWeekFullTime() {
		return hourPerWeekFullTime;
	}

	public void setHourPerWeekFullTime(String hourPerWeekFullTime) {
		this.hourPerWeekFullTime = hourPerWeekFullTime;
	}

	public String getHourPerWeekpartTime() {
		return hourPerWeekpartTime;
	}

	public void setHourPerWeekpartTime(String hourPerWeekpartTime) {
		this.hourPerWeekpartTime = hourPerWeekpartTime;
	}

	public int getEnrollEmployeeCount() {
		return enrollEmployeeCount;
	}

	public void setEnrollEmployeeCount(int enrollEmployeeCount) {
		this.enrollEmployeeCount = enrollEmployeeCount;
	}

	public int getEligibleEmployeeCount() {
		return eligibleEmployeeCount;
	}

	public void setEligibleEmployeeCount(int eligibleEmployeeCount) {
		this.eligibleEmployeeCount = eligibleEmployeeCount;
	}

	public String getOtherRequirement() {
		return otherRequirement;
	}

	public void setOtherRequirement(String otherRequirement) {
		this.otherRequirement = otherRequirement;
	}

	public String getCityOfHq() {
		return cityOfHq;
	}

	public void setCityOfHq(String cityOfHq) {
		this.cityOfHq = cityOfHq;
	}

	public String getPurposeOfApplication() {
		return purposeOfApplication;
	}

	public void setPurposeOfApplication(String purposeOfApplication) {
		this.purposeOfApplication = purposeOfApplication;
	}

	public String getClassesAddedOrRemoved() {
		return classesAddedOrRemoved;
	}

	public void setClassesAddedOrRemoved(String classesAddedOrRemoved) {
		this.classesAddedOrRemoved = classesAddedOrRemoved;
	}

	public String getEmployerTaxNumber() {
		return employerTaxNumber;
	}

	public void setEmployerTaxNumber(String employerTaxNumber) {
		this.employerTaxNumber = employerTaxNumber;
	}

	public String getPolicyNumbers() {
		return policyNumbers;
	}

	public void setPolicyNumbers(String policyNumbers) {
		this.policyNumbers = policyNumbers;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public String getApplicantOther() {
		return applicantOther;
	}

	public void setApplicantOther(String applicantOther) {
		this.applicantOther = applicantOther;
	}

	public String getSicCode() {
		return sicCode;
	}

	public void setSicCode(String sicCode) {
		this.sicCode = sicCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNumberOrStreet() {
		return numberOrStreet;
	}

	public void setNumberOrStreet(String numberOrStreet) {
		this.numberOrStreet = numberOrStreet;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhysicalAddress() {
		return physicalAddress;
	}

	public void setPhysicalAddress(String physicalAddress) {
		this.physicalAddress = physicalAddress;
	}

	public String getPhysicalNumberOrStreet() {
		return physicalNumberOrStreet;
	}

	public void setPhysicalNumberOrStreet(String physicalNumberOrStreet) {
		this.physicalNumberOrStreet = physicalNumberOrStreet;
	}

	public String getPhysicalCity() {
		return physicalCity;
	}

	public void setPhysicalCity(String physicalCity) {
		this.physicalCity = physicalCity;
	}

	public String getPhysicalState() {
		return physicalState;
	}

	public void setPhysicalState(String physicalState) {
		this.physicalState = physicalState;
	}

	public String getPhysicalZip() {
		return physicalZip;
	}

	public void setPhysicalZip(String physicalZip) {
		this.physicalZip = physicalZip;
	}

	public String getAuthorizedPersonName() {
		return authorizedPersonName;
	}

	public void setAuthorizedPersonName(String authorizedPersonName) {
		this.authorizedPersonName = authorizedPersonName;
	}

	public String getAuthorizedPersonTitle() {
		return authorizedPersonTitle;
	}

	public void setAuthorizedPersonTitle(String authorizedPersonTitle) {
		this.authorizedPersonTitle = authorizedPersonTitle;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactTitle() {
		return contactTitle;
	}

	public void setContactTitle(String contactTitle) {
		this.contactTitle = contactTitle;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getErisaPlan() {
		return erisaPlan;
	}

	public void setErisaPlan(String erisaPlan) {
		this.erisaPlan = erisaPlan;
	}

	public String getPlanNumber() {
		return planNumber;
	}

	public void setPlanNumber(String planNumber) {
		this.planNumber = planNumber;
	}
	

	public String getChurchPlan() {
		return churchPlan;
	}

	public void setChurchPlan(String churchPlan) {
		this.churchPlan = churchPlan;
	}

	public String getSadToBeCovered() {
		return sadToBeCovered;
	}

	public void setSadToBeCovered(String sadToBeCovered) {
		this.sadToBeCovered = sadToBeCovered;
	}

	public String getSadNames() {
		return sadNames;
	}

	public void setSadNames(String sadNames) {
		this.sadNames = sadNames;
	}

	public String getFirstOftheMonth() {
		return firstOftheMonth;
	}

	public void setFirstOftheMonth(String firstOftheMonth) {
		this.firstOftheMonth = firstOftheMonth;
	}

	public String getFullTimeOrPartTime() {
		return fullTimeOrPartTime;
	}

	public void setFullTimeOrPartTime(String fullTimeOrPartTime) {
		this.fullTimeOrPartTime = fullTimeOrPartTime;
	}

	public String getFullTimeEligibleCoverageDuration() {
		return fullTimeEligibleCoverageDuration;
	}

	public void setFullTimeEligibleCoverageDuration(String fullTimeEligibleCoverageDuration) {
		this.fullTimeEligibleCoverageDuration = fullTimeEligibleCoverageDuration;
	}

	public String getPartTimeEligibleCoverageDuration() {
		return partTimeEligibleCoverageDuration;
	}

	public void setPartTimeEligibleCoverageDuration(String partTimeEligibleCoverageDuration) {
		this.partTimeEligibleCoverageDuration = partTimeEligibleCoverageDuration;
	}

	public String getFullTimeEligibleCoverageMonth() {
		return fullTimeEligibleCoverageMonth;
	}

	public void setFullTimeEligibleCoverageMonth(String fullTimeEligibleCoverageMonth) {
		this.fullTimeEligibleCoverageMonth = fullTimeEligibleCoverageMonth;
	}

	public String getPartTimeEligibleCoverageMonth() {
		return partTimeEligibleCoverageMonth;
	}

	public void setPartTimeEligibleCoverageMonth(String partTimeEligibleCoverageMonth) {
		this.partTimeEligibleCoverageMonth = partTimeEligibleCoverageMonth;
	}
}
