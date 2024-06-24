package com.aflac.core.experience.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiEnrollmentDetails {

	@JsonProperty("enrollment-platforms")
	private List<ApiEnrollmentPlatform> enrollmentPlatform;
	
	public List<ApiEnrollmentPlatform> getEnrollmentPlatform() {
		return enrollmentPlatform;
	}
	public void setEnrollmentPlatform(List<ApiEnrollmentPlatform> enrollmentPlatform) {
		this.enrollmentPlatform = enrollmentPlatform;
	}
	
}
