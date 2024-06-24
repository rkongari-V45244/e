package com.aflac.core.util;

enum Products {

	ACCOUNT_INFORMATION("Account Information"),
	ACCIDENT("Accident"),
	BENEXTEND("Benextend"),
	CRITICAL_ILLNESS("Critical Illness"),
	HOSPITAL_INDEMNITY("Hospital Indemnity"),
	WORKSITE_DISABILITY("Disability"),
	WHOLE_LIFE("Whole Life"),
	TERM_LIFE("Term Life"),
	TERM_TO_120("Term to 120");
	
	private String value;
	
	private Products(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}

enum Labels {

	QUESTION("Eligibility Question"),
	AFFIRMATION("Affirmation"),
	HEALTH_QUESTION("Health Question"),
	DISCLOSURE("Disclosures");
	
	private String value;
	
	private Labels(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}

