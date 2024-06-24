package com.aflac.core.util;

public enum ComplianceCase {
	WITHOUTCOMPLIANCE("withoutCompliance"),
	WITHCOMPLIANCE("withCompliance"),
	ONLYCOMPLIANCE("onlyCompliance"),
	CASEBUIDGUIDE("casebuidGuide");
	
	private String value;
	
	ComplianceCase(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
