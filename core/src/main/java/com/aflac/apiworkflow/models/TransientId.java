package com.aflac.apiworkflow.models;

public class TransientId {

	private String transientDocumentId;
	
	public TransientId() {
		super();
	}
	
	public TransientId(String transientDocumentId) {
		super();
		this.transientDocumentId = transientDocumentId;
	}

	public String getTransientDocumentId() {
		return transientDocumentId;
	}

	public void setTransientDocumentId(String transientDocumentId) {
		this.transientDocumentId = transientDocumentId;
	}
	
}
