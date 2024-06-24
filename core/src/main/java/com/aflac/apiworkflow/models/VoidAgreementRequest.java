package com.aflac.apiworkflow.models;

public class VoidAgreementRequest {

	private String value;
	private String comment;
	private Boolean notifySigner;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Boolean getNotifySigner() {
		return notifySigner;
	}
	public void setNotifySigner(Boolean notifySigner) {
		this.notifySigner = notifySigner;
	}
	
	
}
