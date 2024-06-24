package com.aflac.apiworkflow.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AgreementDocumentId {

	@JsonProperty("name")
	private String name;
	@JsonProperty("documentId")
	private String documentId;
	@JsonProperty("mimeType")
	private String mimeType;
		
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
}
