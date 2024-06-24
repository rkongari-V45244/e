package com.aflac.core.experience.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiProducts {
	@JsonProperty("product-name")
	private String productname;
	@JsonProperty("product-id")
	private String productId;
	@JsonProperty("plan-name")
	private String planname; 
	@JsonProperty("plan-level")
    private String planlevel;
	@JsonProperty("series")
    private int series;
	@JsonProperty("coverage-level")
    private String coveragelevel; 
	@JsonProperty("application-number")
    private String applicationnumber; 
	@JsonProperty("brochure-type")
    private String brochuretype;
	@JsonProperty("tax-type")
    private String taxtype; 
	@JsonProperty("di-hours-worked-per-week")
	private Integer diHoursWorkedPerWeek;
    @JsonProperty("employee-issue-age")
    private String employeeissueage; 
    @JsonProperty("spouse-issue-age")
    private String spouseissueage; 
    @JsonProperty("child-issue-age")
    private String childissueage;
    @JsonProperty("employee-termination-age")
    private String employeeTerminationAge;
    @JsonProperty("spouse-termination-age")
    private String spouseTerminationAge;
    @JsonProperty("child-termination-age")
    private String childTerminationAge;
	@JsonProperty("face-amount-offered")
	private FaceAmountOffered faceamountoffered;
	@JsonProperty("optional-rider")
	private Boolean optionalrider;
	@JsonProperty("progressive-rider")
	private Boolean progressiverider;
	@JsonProperty("tobacco")
	private Tobacco tobacco;
	@JsonProperty("age-details")
	private AgeDetails agedetails;
	@JsonProperty("participation-requirement")
	private String participationrequirement;
	@JsonProperty("rider")
	private String rider;
	@JsonProperty("accelerated-death-benefit-offered")
	private String acceleratedDeathBenefitOffered;
	@JsonProperty("new-hired-date")
	private String newhireddate;
	@JsonProperty("eligibility-statements")
	private List<String> eligibilitystatements;
    @JsonProperty("eligibility-question-or-affirmation-header")
	private String eligibilityquestionoraffirmationheader;
    @JsonProperty("hundred-percent-guaranteed")
    private Boolean hundredPercentageGuranteed;
    @JsonProperty("eligible-employee-guaranteed-issue")
    private String eligibleEmployeeGuaranteedIssue;
    @JsonProperty("health-question-required")
    private boolean healthquestionrequired;
    @JsonProperty("initial-enrollment")
    private String initialenrollment;
    @JsonProperty("benefit-type")
    private String benefittype;
    @JsonProperty("benefit-period")
    private String benefitperiod;
    @JsonProperty("elimination-period")
    private String eliminationperiod;
    private String term;
    @JsonProperty("platform-driven")
    private String platformdriven;
    @JsonProperty("vendor-id")
    private String vendorId;
    @JsonProperty("provisioned-gi-amount")
    private String giAmount;
    @JsonProperty("max-amount-offered")
    private String maxAmt;
    @JsonProperty("other-instructions")
    private String otherInstructions;
    @JsonProperty("is-child-coverage-offered")
    private Boolean isChildCoverageOffered;
    @JsonProperty("is-child-term-life-rider-offered")
    private Boolean isChildTermlifeRiderOffered;
    @JsonProperty("is-unique-increments-utilized")
    private Boolean isUniqueIncrementsUtilized;
    @JsonProperty("child-coverage-offered")
    private String childCoverageOffered;
    @JsonProperty("benefit-amount-description")
    private String benefitAmountDescription;
    @JsonProperty("is-product-aro")
    protected Boolean isProductAro;
    @JsonProperty("issue-age-type")
    private String issueAgeType;
    @JsonProperty("product-description")
    private String productDescription;
    	
    public String getMaxAmt() {
        return maxAmt;
    }
    public void setMaxAmt(String maxAmt) {
        this.maxAmt = maxAmt;
    }
    public String getVendorId() {
        return vendorId;
    }
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }
    public String getGiAmount() {
        return giAmount;
    }
    public void setGiAmount(String giAmount) {
        this.giAmount = giAmount;
    }
    public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public boolean isHealthquestionrequired() {
		return healthquestionrequired;
	}
	public void setHealthquestionrequired(boolean healthquestionrequired) {
		this.healthquestionrequired = healthquestionrequired;
	}
	public String getInitialenrollment() {
		return initialenrollment;
	}
	public void setInitialenrollment(String initialenrollment) {
		this.initialenrollment = initialenrollment;
	}
	public String getBenefittype() {
		return benefittype;
	}
	public void setBenefittype(String benefittype) {
		this.benefittype = benefittype;
	}
	public String getBenefitperiod() {
		return benefitperiod;
	}
	public void setBenefitperiod(String benefitperiod) {
		this.benefitperiod = benefitperiod;
	}
	public String getEliminationperiod() {
		return eliminationperiod;
	}
	public void setEliminationperiod(String eliminationperiod) {
		this.eliminationperiod = eliminationperiod;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getPlatformdriven() {
		return platformdriven;
	}
	public void setPlatformdriven(String platformdriven) {
		this.platformdriven = platformdriven;
	}
	public List<String> getEligibilitystatements() {
        return eligibilitystatements;
    }
    public void setEligibilitystatements(List<String> eligibilitystatements) {
        this.eligibilitystatements = eligibilitystatements;
    }
	public String getPlanname() {
		return planname;
	}
	public void setPlanname(String planname) {
		this.planname = planname;
	}
	public String getPlanlevel() {
		return planlevel;
	}
	public void setPlanlevel(String planlevel) {
		this.planlevel = planlevel;
	}
	public int getSeries() {
		return series;
	}
	public void setSeries(int series) {
		this.series = series;
	}
	public String getCoveragelevel() {
		return coveragelevel;
	}
	public void setCoveragelevel(String coveragelevel) {
		this.coveragelevel = coveragelevel;
	}
	public String getApplicationnumber() {
		return applicationnumber;
	}
	public void setApplicationnumber(String applicationnumber) {
		this.applicationnumber = applicationnumber;
	}
	public String getBrochuretype() {
		return brochuretype;
	}
	public void setBrochuretype(String brochuretype) {
		this.brochuretype = brochuretype;
	}
	public String getTaxtype() {
		return taxtype;
	}
	public void setTaxtype(String taxtype) {
		this.taxtype = taxtype;
	}
	public String getEmployeeissueage() {
		return employeeissueage;
	}
	public void setEmployeeissueage(String employeeissueage) {
		this.employeeissueage = employeeissueage;
	}
	public String getSpouseissueage() {
		return spouseissueage;
	}
	public void setSpouseissueage(String spouseissueage) {
		this.spouseissueage = spouseissueage;
	}
	public String getChildissueage() {
		return childissueage;
	}
	public void setChildissueage(String childissueage) {
		this.childissueage = childissueage;
	}
	public FaceAmountOffered getFaceamountoffered() {
		return faceamountoffered;
	}
	public void setFaceamountoffered(FaceAmountOffered faceamountoffered) {
		this.faceamountoffered = faceamountoffered;
	}
	public Boolean isOptionalrider() {
		return optionalrider;
	}
	public void setOptionalrider(Boolean optionalrider) {
		this.optionalrider = optionalrider;
	}
	public Boolean isProgressiverider() {
		return progressiverider;
	}
	public void setProgressiverider(Boolean progressiverider) {
		this.progressiverider = progressiverider;
	}
	public Tobacco getTobacco() {
		return tobacco;
	}
	public void setTobacco(Tobacco tobacco) {
		this.tobacco = tobacco;
	}
	public AgeDetails getAgedetails() {
		return agedetails;
	}
	public void setAgedetails(AgeDetails agedetails) {
		this.agedetails = agedetails;
	}
	public String isParticipationrequirement() {
		return participationrequirement;
	}
	public void setParticipationrequirement(String participationrequirement) {
		this.participationrequirement = participationrequirement;
	}
	public String getNewhireddate() {
		return newhireddate;
	}
	public void setNewhireddate(String newhireddate) {
		this.newhireddate = newhireddate;
	}
	public String getEligibilityquestionoraffirmationheader() {
		return eligibilityquestionoraffirmationheader;
	}
	public void setEligibilityquestionoraffirmationheader(String eligibilityquestionoraffirmationheader) {
		this.eligibilityquestionoraffirmationheader = eligibilityquestionoraffirmationheader;
	}
	public Boolean getHundredPercentageGuranteed() {
		return hundredPercentageGuranteed;
	}
	public void setHundredPercentageGuranteed(Boolean hundredPercentageGuranteed) {
		this.hundredPercentageGuranteed = hundredPercentageGuranteed;
	}
	public String getRider() {
		return rider;
	}
	public void setRider(String rider) {
		this.rider = rider;
	}
	public String getAcceleratedDeathBenefitOffered() {
		return acceleratedDeathBenefitOffered;
	}
	public void setAcceleratedDeathBenefitOffered(String acceleratedDeathBenefitOffered) {
		this.acceleratedDeathBenefitOffered = acceleratedDeathBenefitOffered;
	}
	public String getOtherInstructions() {
		return otherInstructions;
	}
	public void setOtherInstructions(String otherInstructions) {
		this.otherInstructions = otherInstructions;
	}
	public Boolean getIsChildCoverageOffered() {
		return isChildCoverageOffered;
	}
	public void setIsChildCoverageOffered(Boolean isChildCoverageOffered) {
		this.isChildCoverageOffered = isChildCoverageOffered;
	}
	public Boolean getIsChildTermlifeRiderOffered() {
		return isChildTermlifeRiderOffered;
	}
	public void setIsChildTermlifeRiderOffered(Boolean isChildTermlifeRiderOffered) {
		this.isChildTermlifeRiderOffered = isChildTermlifeRiderOffered;
	}
	public Boolean getIsUniqueIncrementsUtilized() {
		return isUniqueIncrementsUtilized;
	}
	public void setIsUniqueIncrementsUtilized(Boolean isUniqueIncrementsUtilized) {
		this.isUniqueIncrementsUtilized = isUniqueIncrementsUtilized;
	}
	public String getChildCoverageOffered() {
		return childCoverageOffered;
	}
	public void setChildCoverageOffered(String childCoverageOffered) {
		this.childCoverageOffered = childCoverageOffered;
	}
	public String getBenefitAmountDescription() {
		return benefitAmountDescription;
	}
	public void setBenefitAmountDescription(String benefitAmountDescription) {
		this.benefitAmountDescription = benefitAmountDescription;
	}
	public Boolean getIsProductAro() {
		return isProductAro;
	}
	public void setIsProductAro(Boolean isProductAro) {
		this.isProductAro = isProductAro;
	}
	public String getIssueAgeType() {
		return issueAgeType;
	}
	public void setIssueAgeType(String issueAgeType) {
		this.issueAgeType = issueAgeType;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public String getEmployeeTerminationAge() {
		return employeeTerminationAge;
	}
	public void setEmployeeTerminationAge(String employeeTerminationAge) {
		this.employeeTerminationAge = employeeTerminationAge;
	}
	public String getSpouseTerminationAge() {
		return spouseTerminationAge;
	}
	public void setSpouseTerminationAge(String spouseTerminationAge) {
		this.spouseTerminationAge = spouseTerminationAge;
	}
	public String getChildTerminationAge() {
		return childTerminationAge;
	}
	public void setChildTerminationAge(String childTerminationAge) {
		this.childTerminationAge = childTerminationAge;
	}
	public Integer getDiHoursWorkedPerWeek() {
		return diHoursWorkedPerWeek;
	}
	public void setDiHoursWorkedPerWeek(Integer diHoursWorkedPerWeek) {
		this.diHoursWorkedPerWeek = diHoursWorkedPerWeek;
	}
	public String getEligibleEmployeeGuaranteedIssue() {
		return eligibleEmployeeGuaranteedIssue;
	}
	public void setEligibleEmployeeGuaranteedIssue(String eligibleEmployeeGuaranteedIssue) {
		this.eligibleEmployeeGuaranteedIssue = eligibleEmployeeGuaranteedIssue;
	}
	
	
	
}
