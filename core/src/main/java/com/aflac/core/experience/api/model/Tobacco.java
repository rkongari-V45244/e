package com.aflac.core.experience.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tobacco {

	private String status;
	@JsonProperty("status-determined")
	private String statusdetermined;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusdetermined() {
		return statusdetermined;
	}
	public void setStatusdetermined(String statusdetermined) {
		this.statusdetermined = statusdetermined;
	}
	
}
