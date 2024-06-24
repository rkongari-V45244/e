package com.aflac.core.util;

public enum DocumentStatus {
	CANCELLED("CANCELLED"),
	ALREADY_SIGNED("ALREADY_SIGNED"),
	OUT_FOR_SIGNATURE("OUT_FOR_SIGNATURE"),
	SIGNED("SIGNED"),
	VOIDED("VOIDED");
	
	private String value;
	
	DocumentStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
