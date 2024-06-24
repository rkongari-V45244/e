package com.aflac.core.experience.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewHireDetails {

	@JsonProperty("self-service")
    private boolean selfservice;

    @JsonProperty("one-to-one")
    private boolean onetoone;

    @JsonProperty("call-center")
    private boolean callcenter;
	
	public boolean isSelfservice() {
		return selfservice;
	}
	public void setSelfservice(boolean selfservice) {
		this.selfservice = selfservice;
	}
	public boolean isOnetoone() {
		return onetoone;
	}
	public void setOnetoone(boolean onetoone) {
		this.onetoone = onetoone;
	}
	public boolean isCallcenter() {
		return callcenter;
	}
	public void setCallcenter(boolean callcenter) {
		this.callcenter = callcenter;
	}
	
	
}
