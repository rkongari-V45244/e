package com.aflac.core.experience.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlatformMaintenance {

	@JsonProperty("platform-id")
	private String platformId;
	@JsonProperty("vendor-id")
	private String vendorId;
	@JsonProperty("crm-platform-naming")
	private String crmPlatformNaming;
	@JsonProperty("platform-name")
	private String platformName;
	@JsonProperty("company-name")
	private String companyName;
	@JsonProperty("worksite-gi-offer-only")
	private String worksiteGiOfferOnly;
	@JsonProperty("worksite-accident")
	private String worksiteAccident;
	@JsonProperty("worksite-critical-illness")
	private String worksiteCriticalIllness;
	@JsonProperty("worksite-hospital-indemnity")
	private String worksiteHospitalIndemnity;
	@JsonProperty("worksite-term-life")
	private String worksiteTermLife;
	@JsonProperty("worksite-whole-life")
	private String worksiteWholeLife;
	@JsonProperty("worksite-std")
	private String worksiteStd;
	@JsonProperty("drms-gi-offfer-only")
	private String drmsGiOffferOnly;
	@JsonProperty("drms-std")
	private String drmsStd;
	@JsonProperty("drms-ltd")
	private String drmsLtd;
	@JsonProperty("drms-term-life")
	private String drmsTermLife;
	@JsonProperty("enrollment-methods-supported")
	private String enrollmentMethodsSupported;
	@JsonProperty("able-to-rate-based-on")
	private String ableToRateBasedOn;
	@JsonProperty("age-rate-format")
	private String ageRateFormat;
	@JsonProperty("rate-per")
	private String ratePer;
	@JsonProperty("capture-tobacco-status")
	private String captureTobaccoStatus;
	@JsonProperty("rate-independently-spouse-age-tobacco")
	private String rateIndependentlySpouseAgeTobacco;
	@JsonProperty("questions-affirmation")
	private String questionsAffirmation;
	@JsonProperty("rate-calculation-method")
	private String rateCalculationMethod;
	@JsonProperty("able-to-ask-health-underwriting-questions")
	private String ableToAskHealthUnderWritingQuestions;
	@JsonProperty("able-to-ask-eligibility-questions")
	private String ableToAskEligibilityQuestions;
	@JsonProperty("able-to-ask-replacement-questions")
	private String ableToAskReplacementQuestions;
	@JsonProperty("able-to-handle-ci-di-buyup")
	private String ableToHandleCiDiBuyup;
	@JsonProperty("edi-file-format")
	private String ediFileFormat;
	
	public String getPlatformid() {
		return platformId;
	}
	public void setPlatformid(String platformid) {
		this.platformId = platformid;
	}
	public String getVendorid() {
		return vendorId;
	}
	public void setVendorid(String vendorid) {
		this.vendorId = vendorid;
	}
	public String getCrmplatformnaming() {
		return crmPlatformNaming;
	}
	public void setCrmplatformnaming(String crmplatformnaming) {
		this.crmPlatformNaming = crmplatformnaming;
	}
	public String getPlatformname() {
		return platformName;
	}
	public void setPlatformname(String platformname) {
		this.platformName = platformname;
	}
	public String getCompanyname() {
		return companyName;
	}
	public void setCompanyname(String companyname) {
		this.companyName = companyname;
	}
	public String getWorksitegiofferonly() {
		return worksiteGiOfferOnly;
	}
	public void setWorksitegiofferonly(String worksitegiofferonly) {
		this.worksiteGiOfferOnly = worksitegiofferonly;
	}
	public String getWorksiteaccident() {
		return worksiteAccident;
	}
	public void setWorksiteaccident(String worksiteaccident) {
		this.worksiteAccident = worksiteaccident;
	}
	public String getWorksitecriticalillness() {
		return worksiteCriticalIllness;
	}
	public void setWorksitecriticalillness(String worksitecriticalillness) {
		this.worksiteCriticalIllness = worksitecriticalillness;
	}
	public String getWorksitehospitalindemnity() {
		return worksiteHospitalIndemnity;
	}
	public void setWorksitehospitalindemnity(String worksitehospitalindemnity) {
		this.worksiteHospitalIndemnity = worksitehospitalindemnity;
	}
	public String getWorksitetermlife() {
		return worksiteTermLife;
	}
	public void setWorksitetermlife(String worksitetermlife) {
		this.worksiteTermLife = worksitetermlife;
	}
	public String getWorksitewholelife() {
		return worksiteWholeLife;
	}
	public void setWorksitewholelife(String worksitewholelife) {
		this.worksiteWholeLife = worksitewholelife;
	}
	public String getWorksitestd() {
		return worksiteStd;
	}
	public void setWorksitestd(String worksitestd) {
		this.worksiteStd = worksitestd;
	}
	public String getDrmsgioffferonly() {
		return drmsGiOffferOnly;
	}
	public void setDrmsgioffferonly(String drmsgioffferonly) {
		this.drmsGiOffferOnly = drmsgioffferonly;
	}
	public String getDrmsstd() {
		return drmsStd;
	}
	public void setDrmsstd(String drmsstd) {
		this.drmsStd = drmsstd;
	}
	public String getDrmsltd() {
		return drmsLtd;
	}
	public void setDrmsltd(String drmsltd) {
		this.drmsLtd = drmsltd;
	}
	public String getDrmstermlife() {
		return drmsTermLife;
	}
	public void setDrmstermlife(String drmstermlife) {
		this.drmsTermLife = drmstermlife;
	}
	public String getEnrollmentmethodssupported() {
		return enrollmentMethodsSupported;
	}
	public void setEnrollmentmethodssupported(String enrollmentmethodssupported) {
		this.enrollmentMethodsSupported = enrollmentmethodssupported;
	}
	public String getAbletoratebasedon() {
		return ableToRateBasedOn;
	}
	public void setAbletoratebasedon(String abletoratebasedon) {
		this.ableToRateBasedOn = abletoratebasedon;
	}
	public String getAgerateformat() {
		return ageRateFormat;
	}
	public void setAgerateformat(String agerateformat) {
		this.ageRateFormat = agerateformat;
	}
	public String getRateper() {
		return ratePer;
	}
	public void setRateper(String rateper) {
		this.ratePer = rateper;
	}
	public String getCapturetobaccostatus() {
		return captureTobaccoStatus;
	}
	public void setCapturetobaccostatus(String capturetobaccostatus) {
		this.captureTobaccoStatus = capturetobaccostatus;
	}
	public String getRateindependentlyspouseagetobacco() {
		return rateIndependentlySpouseAgeTobacco;
	}
	public void setRateindependentlyspouseagetobacco(String rateindependentlyspouseagetobacco) {
		this.rateIndependentlySpouseAgeTobacco = rateindependentlyspouseagetobacco;
	}
	public String getQuestionsaffirmation() {
		return questionsAffirmation;
	}
	public void setQuestionsaffirmation(String questionsaffirmation) {
		this.questionsAffirmation = questionsaffirmation;
	}
	public String getRatecalculationmethod() {
		return rateCalculationMethod;
	}
	public void setRatecalculationmethod(String ratecalculationmethod) {
		this.rateCalculationMethod = ratecalculationmethod;
	}
	public String getAbletoaskhealthunderwritingquestions() {
		return ableToAskHealthUnderWritingQuestions;
	}
	public void setAbletoaskhealthunderwritingquestions(String abletoaskhealthunderwritingquestions) {
		this.ableToAskHealthUnderWritingQuestions = abletoaskhealthunderwritingquestions;
	}
	public String getAbletoaskeligibilityquestions() {
		return ableToAskEligibilityQuestions;
	}
	public void setAbletoaskeligibilityquestions(String abletoaskeligibilityquestions) {
		this.ableToAskEligibilityQuestions = abletoaskeligibilityquestions;
	}
	public String getAbletoaskreplacementquestions() {
		return ableToAskReplacementQuestions;
	}
	public void setAbletoaskreplacementquestions(String abletoaskreplacementquestions) {
		this.ableToAskReplacementQuestions = abletoaskreplacementquestions;
	}
	public String getAbletohandlecidibuyup() {
		return ableToHandleCiDiBuyup;
	}
	public void setAbletohandlecidibuyup(String abletohandlecidibuyup) {
		this.ableToHandleCiDiBuyup = abletohandlecidibuyup;
	}
	public String getEdifileformat() {
		return ediFileFormat;
	}
	public void setEdifileformat(String edifileformat) {
		this.ediFileFormat = edifileformat;
	}
	
}
