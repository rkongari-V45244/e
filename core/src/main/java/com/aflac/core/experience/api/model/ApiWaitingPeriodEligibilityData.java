package com.aflac.core.experience.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiWaitingPeriodEligibilityData {

	@JsonProperty("class-name")
    private String className;
	
	@JsonProperty("waiting-period")
    private String waitingPeriod;
	
	@JsonProperty("number-Of-days")
    private String numberOfDays;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getWaitingPeriod() {
		return waitingPeriod;
	}

	public void setWaitingPeriod(String waitingPeriod) {
		this.waitingPeriod = waitingPeriod;
	}

	public String getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(String numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	
	
}
