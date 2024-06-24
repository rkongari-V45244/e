package com.aflac.core.experience.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuditUserActivity {
	
	@JsonProperty("user-id")
	private String userId;
	@JsonProperty("application-id")
	private String applicationId;
	@JsonProperty("application-name")
	private String applicationName;
	@JsonProperty("query-date-time")
	private String queryDateTime;
	@JsonProperty("created-date-time")
	private String createdDateTime;
	@JsonProperty("turnaround-time")
	private String turnaroundTime;
	@JsonProperty("application-data")
	private String applicationData;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getQueryDateTime() {
		return queryDateTime;
	}
	public void setQueryDateTime(String queryDateTime) {
		this.queryDateTime = queryDateTime;
	}
	public String getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public String getTurnaroundTime() {
		return turnaroundTime;
	}
	public void setTurnaroundTime(String turnaroundTime) {
		this.turnaroundTime = turnaroundTime;
	}
	public String getApplicationData() {
		return applicationData;
	}
	public void setApplicationData(String applicationData) {
		this.applicationData = applicationData;
	}
				
}
