package com.aflac.core.experience.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiWaitingPeriodEligibility {

	@JsonProperty("waiting-period-eligibility-data")
	private List<ApiWaitingPeriodEligibilityData> waitingPeriodEligibilityData;

	public List<ApiWaitingPeriodEligibilityData> getWaitingPeriodEligibilityData() {
		return waitingPeriodEligibilityData;
	}

	public void setWaitingPeriodEligibilityData(List<ApiWaitingPeriodEligibilityData> waitingPeriodEligibilityData) {
		this.waitingPeriodEligibilityData = waitingPeriodEligibilityData;
	}
	
	
}
