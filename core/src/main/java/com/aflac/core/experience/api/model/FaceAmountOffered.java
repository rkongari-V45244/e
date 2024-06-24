package com.aflac.core.experience.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FaceAmountOffered {

	@JsonProperty("empoyee-face-amount-offered")
	private AmountOffered empoyeefaceamountoffered;
	@JsonProperty("spouse-face-amount-offered")
	private AmountOffered spousefaceamountoffered;
	@JsonProperty("child-face-amount-offered")
	private AmountOffered childfaceamountoffered;
    @JsonProperty("tdi-state-amount-offered")
    private AmountOffered tdistateamountoffered;
    @JsonProperty("child-individual-policy-amount-offered")
    private AmountOffered childIndividualAmountOffered;
    @JsonProperty("child-term-rider-amount-offered")
    private AmountOffered childTermAmountoffered;
    @JsonProperty("ee-benefit-amount-percentage")
    private Integer benefitAmt;
	
    
	public Integer getBenefitAmt() {
        return benefitAmt;
    }
    public void setBenefitAmt(Integer benefitAmt) {
        this.benefitAmt = benefitAmt;
    }
    public AmountOffered getEmpoyeefaceamountoffered() {
		return empoyeefaceamountoffered;
	}
	public void setEmpoyeefaceamountoffered(AmountOffered empoyeefaceamountoffered) {
		this.empoyeefaceamountoffered = empoyeefaceamountoffered;
	}
	public AmountOffered getSpousefaceamountoffered() {
		return spousefaceamountoffered;
	}
	public void setSpousefaceamountoffered(AmountOffered spousefaceamountoffered) {
		this.spousefaceamountoffered = spousefaceamountoffered;
	}
	public AmountOffered getChildfaceamountoffered() {
		return childfaceamountoffered;
	}
	public void setChildfaceamountoffered(AmountOffered childfaceamountoffered) {
		this.childfaceamountoffered = childfaceamountoffered;
	}
	public AmountOffered getTdistateamountoffered() {
		return tdistateamountoffered;
	}
	public void setTdistateamountoffered(AmountOffered tdistateamountoffered) {
		this.tdistateamountoffered = tdistateamountoffered;
	}
	public AmountOffered getChildIndividualAmountOffered() {
		return childIndividualAmountOffered;
	}
	public void setChildIndividualAmountOffered(AmountOffered childIndividualAmountOffered) {
		this.childIndividualAmountOffered = childIndividualAmountOffered;
	}
	public AmountOffered getChildTermAmountoffered() {
		return childTermAmountoffered;
	}
	public void setChildTermAmountoffered(AmountOffered childTermAmountoffered) {
		this.childTermAmountoffered = childTermAmountoffered;
	}
	
}
