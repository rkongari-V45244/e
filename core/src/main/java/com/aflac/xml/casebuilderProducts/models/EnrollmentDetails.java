package com.aflac.xml.casebuilderProducts.models;

import java.util.List;

import com.aflac.core.experience.api.model.ApiEnrollmentPlatform;

public class EnrollmentDetails {
	
	private String enrollmentRecord;
	private List<String> enrollmentPlatform;
	private List<ApiEnrollmentPlatform> enrollmentDetails;
	
	public String getEnrollmentRecord() {
		return enrollmentRecord;
	}
	public void setEnrollmentRecord(String enrollmentRecord) {
		this.enrollmentRecord = enrollmentRecord;
	}
	public List<String> getEnrollmentPlatform() {
		return enrollmentPlatform;
	}
	public void setEnrollmentPlatform(List<String> enrollmentPlatform) {
		this.enrollmentPlatform = enrollmentPlatform;
	}
	public List<ApiEnrollmentPlatform> getEnrollmentDetails() {
		return enrollmentDetails;
	}
	public void setEnrollmentDetails(List<ApiEnrollmentPlatform> enrollmentDetails) {
		this.enrollmentDetails = enrollmentDetails;
	}
		
}
