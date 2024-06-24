package com.aflac.apiworkflow.models;

import java.util.List;

public class DocumentCreationInfo {

	private List<TransientId> fileInfos;
	private String name;
	private String message;
	private List<Recipient> recipients;
	private String signatureFlow;
	private String signatureType;
	private List<String> ccs;
	private Integer daysUntilSigningDeadline;
	private String reminderFrequency;
	
	public List<TransientId> getFileInfos() {
		return fileInfos;
	}
	public void setFileInfos(List<TransientId> fileInfos) {
		this.fileInfos = fileInfos;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<Recipient> getRecipients() {
		return recipients;
	}
	public void setRecipients(List<Recipient> recipients) {
		this.recipients = recipients;
	}
	public String getSignatureFlow() {
		return signatureFlow;
	}
	public void setSignatureFlow(String signatureFlow) {
		this.signatureFlow = signatureFlow;
	}
	public String getSignatureType() {
		return signatureType;
	}
	public void setSignatureType(String signatureType) {
		this.signatureType = signatureType;
	}
	public List<String> getCcs() {
		return ccs;
	}
	public void setCcs(List<String> ccs) {
		this.ccs = ccs;
	}
	public Integer getDaysUntilSigningDeadline() {
		return daysUntilSigningDeadline;
	}
	public void setDaysUntilSigningDeadline(Integer daysUntilSigningDeadline) {
		this.daysUntilSigningDeadline = daysUntilSigningDeadline;
	}
	public String getReminderFrequency() {
		return reminderFrequency;
	}
	public void setReminderFrequency(String reminderFrequency) {
		this.reminderFrequency = reminderFrequency;
	}
}
