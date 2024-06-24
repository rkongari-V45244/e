package com.aflac.core.experience.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiMasterAppProduct {

	@JsonProperty("product-name")
	private String productName; 
	
	@JsonProperty("product-sequence")
	private int productSequence;
	
	@JsonProperty("series")
	private String series;
	
	@JsonProperty("plan-level-premium")
	private String planLevelPremium;
	
	@JsonProperty("benefit-type")
	private String benefitType;
	
	@JsonProperty("application-reason")
	private String applicationReason;
	
	@JsonProperty("existing-policy-number")
	private String existingPolicyNumber;
	
	@JsonProperty("other-application-reason")
	private String OtherApplicationReason;
	
	@JsonProperty("eligible-employee")
	private String eligibleEmployee;
	
	@JsonProperty("eligible-employee-class")
	private String eligibleEmployeeClass;
	
	@JsonProperty("eligible-spouse")
	private String eligibleSpouse;
	
	@JsonProperty("eligible-employee-except")
	private String eligibleEmployeeExcept;
	
	@JsonProperty("eligible-employee-other")
	private String eligibleEmployeeOther;
	
	@JsonProperty("product-class")
	private String productClass;
	
	@JsonProperty("plan")
	private String plan;
	
	@JsonProperty("plan-features")
	private List<String> planFeatures; 
	
	@JsonProperty("plan-features-other")
	private String planFeaturesOther;
	
	@JsonProperty("optional-features")
	private List<String> optionalFeatures;
	
	@JsonProperty("optional-features-other")
	private String optionalFeaturesOther;
	
	@JsonProperty("optional-features-text")
	private String optionalFeaturesText;
	
	@JsonProperty("effective-date")
	private String effectiveDate;
	
	@JsonProperty("rates")
	private String rates;
	
	@JsonProperty("product-contribution")
	private String productContribution;
	
	@JsonProperty("employee-premium")
	private String employeePremium;
	
	@JsonProperty("employer-premium")
	private String employerPremium;
	
	@JsonProperty("is-replacing-group-policy")
	private boolean isReplacingGroupPolicy;
	
	@JsonProperty("carrier-policy-number")
	private String policyOrCarrierNumber;
	
	@JsonProperty("income-replacement")
	private String incomeReplacement;
	
	@JsonProperty("benefit-period")
	private String benefitPeriod;
	
	@JsonProperty("elimination-period")
	private String eliminationPeriod;
	
	@JsonProperty("group-policy-state")
	private String groupPolicyState;
	
	@JsonProperty("optional-term-life-coverages")
	private String optionalTermlifeCoverages;
	
	@JsonProperty("ciac")
	private String ciac;
	
	@JsonProperty("proposed-effective-date")
	private String proposedEffectiveDate;
	
	@JsonProperty("on-date")
	private String onDate;
	
	@JsonProperty("minimum-no-of-employees")
	private String minimumNoOfEmployees;
	
	@JsonProperty("percentile-of-eligible-employees")
	private String percentileOfEligibleEmployees;
	
	@JsonProperty("premium-paid")
	private String premiumPaid;
	
	@JsonProperty("premium-payment-mode")
	private String premiumPaymentMode;
	
	@JsonProperty("employee-cost-of-insurance")
	private String employeeCostOfInsurance;
	
	@JsonProperty("actively-at-work")
	private ApiActivelyAtWork activelyAtWork;
	
	@JsonProperty("waiting-period-eligibility")
	private ApiWaitingPeriodEligibility waitingPeriodEligibility;
	
	@JsonProperty("plan-year-per-insured")
	private String planYearPerInsured;
	
	@JsonProperty("hi-enroll-employee-count")
	private Integer hiEnrollEmployeeCount;
	
	@JsonProperty("premium-due")
	private String premiumDue;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductSequence() {
		return productSequence;
	}

	public void setProductSequence(int productSequence) {
		this.productSequence = productSequence;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getPlanLevelPremium() {
		return planLevelPremium;
	}

	public void setPlanLevelPremium(String planLevelPremium) {
		this.planLevelPremium = planLevelPremium;
	}

	public String getBenefitType() {
		return benefitType;
	}

	public void setBenefitType(String benefitType) {
		this.benefitType = benefitType;
	}

	public String getApplicationReason() {
		return applicationReason;
	}

	public void setApplicationReason(String applicationReason) {
		this.applicationReason = applicationReason;
	}
	
	public String getExistingPolicyNumber() {
		return existingPolicyNumber;
	}

	public void setExistingPolicyNumber(String existingPolicyNumber) {
		this.existingPolicyNumber = existingPolicyNumber;
	}

	public String getOtherApplicationReason() {
		return OtherApplicationReason;
	}

	public void setOtherApplicationReason(String otherApplicationReason) {
		OtherApplicationReason = otherApplicationReason;
	}

	public String getEligibleEmployee() {
		return eligibleEmployee;
	}

	public void setEligibleEmployee(String eligibleEmployee) {
		this.eligibleEmployee = eligibleEmployee;
	}

	public String getEligibleEmployeeClass() {
		return eligibleEmployeeClass;
	}

	public void setEligibleEmployeeClass(String eligibleEmployeeClass) {
		this.eligibleEmployeeClass = eligibleEmployeeClass;
	}

	public String getEligibleSpouse() {
		return eligibleSpouse;
	}

	public void setEligibleSpouse(String eligibleSpouse) {
		this.eligibleSpouse = eligibleSpouse;
	}

	public String getEligibleEmployeeExcept() {
		return eligibleEmployeeExcept;
	}

	public void setEligibleEmployeeExcept(String eligibleEmployeeExcept) {
		this.eligibleEmployeeExcept = eligibleEmployeeExcept;
	}

	public String getEligibleEmployeeOther() {
		return eligibleEmployeeOther;
	}

	public void setEligibleEmployeeOther(String eligibleEmployeeOther) {
		this.eligibleEmployeeOther = eligibleEmployeeOther;
	}

	public String getProductClass() {
		return productClass;
	}

	public void setProductClass(String productClass) {
		this.productClass = productClass;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public List<String> getPlanFeatures() {
		return planFeatures;
	}

	public void setPlanFeatures(List<String> planFeatures) {
		this.planFeatures = planFeatures;
	}

	public String getPlanFeaturesOther() {
		return planFeaturesOther;
	}

	public void setPlanFeaturesOther(String planFeaturesOther) {
		this.planFeaturesOther = planFeaturesOther;
	}

	public List<String> getOptionalFeatures() {
		return optionalFeatures;
	}

	public void setOptionalFeatures(List<String> optionalFeatures) {
		this.optionalFeatures = optionalFeatures;
	}

	public String getOptionalFeaturesOther() {
		return optionalFeaturesOther;
	}

	public void setOptionalFeaturesOther(String optionalFeaturesOther) {
		this.optionalFeaturesOther = optionalFeaturesOther;
	}

	public String getOptionalFeaturesText() {
		return optionalFeaturesText;
	}

	public void setOptionalFeaturesText(String optionalFeaturesText) {
		this.optionalFeaturesText = optionalFeaturesText;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getRates() {
		return rates;
	}

	public void setRates(String rates) {
		this.rates = rates;
	}
	
	public String getProductContribution() {
		return productContribution;
	}

	public void setProductContribution(String productContribution) {
		this.productContribution = productContribution;
	}

	public String getEmployeePremium() {
		return employeePremium;
	}

	public void setEmployeePremium(String employeePremium) {
		this.employeePremium = employeePremium;
	}

	public String getEmployerPremium() {
		return employerPremium;
	}

	public void setEmployerPremium(String employerPremium) {
		this.employerPremium = employerPremium;
	}

	public boolean isReplacingGroupPolicy() {
		return isReplacingGroupPolicy;
	}

	public void setReplacingGroupPolicy(boolean isReplacingGroupPolicy) {
		this.isReplacingGroupPolicy = isReplacingGroupPolicy;
	}

	public String getPolicyOrCarrierNumber() {
		return policyOrCarrierNumber;
	}

	public void setPolicyOrCarrierNumber(String policyOrCarrierNumber) {
		this.policyOrCarrierNumber = policyOrCarrierNumber;
	}

	public String getIncomeReplacement() {
		return incomeReplacement;
	}

	public void setIncomeReplacement(String incomeReplacement) {
		this.incomeReplacement = incomeReplacement;
	}

	public String getBenefitPeriod() {
		return benefitPeriod;
	}

	public void setBenefitPeriod(String benefitPeriod) {
		this.benefitPeriod = benefitPeriod;
	}

	public String getEliminationPeriod() {
		return eliminationPeriod;
	}

	public void setEliminationPeriod(String eliminationPeriod) {
		this.eliminationPeriod = eliminationPeriod;
	}

	public String getGroupPolicyState() {
		return groupPolicyState;
	}

	public void setGroupPolicyState(String groupPolicyState) {
		this.groupPolicyState = groupPolicyState;
	}

	public String getOptionalTermlifeCoverages() {
		return optionalTermlifeCoverages;
	}

	public void setOptionalTermlifeCoverages(String optionalTermlifeCoverages) {
		this.optionalTermlifeCoverages = optionalTermlifeCoverages;
	}

	public String getCiac() {
		return ciac;
	}

	public void setCiac(String ciac) {
		this.ciac = ciac;
	}

	public String getProposedEffectiveDate() {
		return proposedEffectiveDate;
	}

	public void setProposedEffectiveDate(String proposedEffectiveDate) {
		this.proposedEffectiveDate = proposedEffectiveDate;
	}

	public String getOnDate() {
		return onDate;
	}

	public void setOnDate(String onDate) {
		this.onDate = onDate;
	}

	public String getMinimumNoOfEmployees() {
		return minimumNoOfEmployees;
	}

	public void setMinimumNoOfEmployees(String minimumNoOfEmployees) {
		this.minimumNoOfEmployees = minimumNoOfEmployees;
	}

	public String getPercentileOfEligibleEmployees() {
		return percentileOfEligibleEmployees;
	}

	public void setPercentileOfEligibleEmployees(String percentileOfEligibleEmployees) {
		this.percentileOfEligibleEmployees = percentileOfEligibleEmployees;
	}

	public String getPremiumPaid() {
		return premiumPaid;
	}

	public void setPremiumPaid(String premiumPaid) {
		this.premiumPaid = premiumPaid;
	}

	public String getPremiumPaymentMode() {
		return premiumPaymentMode;
	}

	public void setPremiumPaymentMode(String premiumPaymentMode) {
		this.premiumPaymentMode = premiumPaymentMode;
	}

	public String getEmployeeCostOfInsurance() {
		return employeeCostOfInsurance;
	}

	public void setEmployeeCostOfInsurance(String employeeCostOfInsurance) {
		this.employeeCostOfInsurance = employeeCostOfInsurance;
	}

	public ApiActivelyAtWork getActivelyAtWork() {
		return activelyAtWork;
	}

	public void setActivelyAtWork(ApiActivelyAtWork activelyAtWork) {
		this.activelyAtWork = activelyAtWork;
	}

	public ApiWaitingPeriodEligibility getWaitingPeriodEligibility() {
		return waitingPeriodEligibility;
	}

	public void setWaitingPeriodEligibility(ApiWaitingPeriodEligibility waitingPeriodEligibility) {
		this.waitingPeriodEligibility = waitingPeriodEligibility;
	}

	public String getPlanYearPerInsured() {
		return planYearPerInsured;
	}

	public void setPlanYearPerInsured(String planYearPerInsured) {
		this.planYearPerInsured = planYearPerInsured;
	}

	public Integer getHiEnrollEmployeeCount() {
		return hiEnrollEmployeeCount;
	}

	public void setHiEnrollEmployeeCount(Integer hiEnrollEmployeeCount) {
		this.hiEnrollEmployeeCount = hiEnrollEmployeeCount;
	}

	public String getPremiumDue() {
		return premiumDue;
	}

	public void setPremiumDue(String premiumDue) {
		this.premiumDue = premiumDue;
	}
	
}
