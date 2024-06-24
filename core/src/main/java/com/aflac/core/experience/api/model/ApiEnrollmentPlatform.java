package com.aflac.core.experience.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiEnrollmentPlatform {

	@JsonProperty("platform")
	private String platform;
	@JsonProperty("spouse-tobacco-rate-status")
	private boolean spouseTobaccoStatus;
	@JsonProperty("contact-integration")
	private boolean contactIntegration;
	@JsonProperty("rate-calculation-method")
	private String rateCalculationMethod;
	@JsonProperty("rate-basis")
	private String rateBasis;
	@JsonProperty("test-file-naming")
    private String testFileNaming;
    @JsonProperty("production-file-naming")
    private String productionFileNaming;
	
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public boolean isSpouseTobaccoStatus() {
		return spouseTobaccoStatus;
	}
	public void setSpouseTobaccoStatus(boolean spouseTobaccoStatus) {
		this.spouseTobaccoStatus = spouseTobaccoStatus;
	}
	public boolean isContactIntegration() {
		return contactIntegration;
	}
	public void setContactIntegration(boolean contactIntegration) {
		this.contactIntegration = contactIntegration;
	}
	public String getRateCalculationMethod() {
		return rateCalculationMethod;
	}
	public void setRateCalculationMethod(String rateCalculationMethod) {
		this.rateCalculationMethod = rateCalculationMethod;
	}
	public String getRateBasis() {
		return rateBasis;
	}
	public void setRateBasis(String rateBasis) {
		this.rateBasis = rateBasis;
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
	
	
}
