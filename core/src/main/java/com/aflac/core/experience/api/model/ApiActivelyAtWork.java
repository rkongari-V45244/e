package com.aflac.core.experience.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiActivelyAtWork {

	@JsonProperty("active-at-work-data")
	private List<ApiActiveAtWorkData> activeAtWorkData;

	public List<ApiActiveAtWorkData> getActiveAtWorkData() {
		return activeAtWorkData;
	}

	public void setActiveAtWorkData(List<ApiActiveAtWorkData> activeAtWorkData) {
		this.activeAtWorkData = activeAtWorkData;
	}
	
	
}
