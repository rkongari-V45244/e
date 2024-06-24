package com.aflac.core.experience.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AmountOffered {

	
	private Integer increments;
	
	@JsonProperty("max-amount")
	private Integer maxamount;
	
	@JsonProperty("min-amount")
	private Integer minamount;
	
	@JsonProperty("guaranteed-issue-maximum")
	private Integer guaranteedAmt;
	
		
	public Integer getGuaranteedAmt() {
		return guaranteedAmt;
	}
	public void setGuaranteedAmt(Integer guaranteedAmt) {
		this.guaranteedAmt = guaranteedAmt;
	}
	public Integer getMinamount() {
		return minamount;
	}
	public void setMinamount(Integer minamount) {
		this.minamount = minamount;
	}
	
	public Integer getIncrements() {
		return increments;
	}
	public void setIncrements(Integer increments) {
		this.increments = increments;
	}
	public Integer getMaxamount() {
		return maxamount;
	}
	public void setMaxamount(Integer maxamount) {
		this.maxamount = maxamount;
	}
	
}
