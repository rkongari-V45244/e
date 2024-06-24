package com.aflac.apiworkflow.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileNetWFModel {

	@JsonProperty("Type")
	private String type;
	@JsonProperty("DocumentID")
	private String documentId;
	@JsonProperty("Title")
	private String title;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
