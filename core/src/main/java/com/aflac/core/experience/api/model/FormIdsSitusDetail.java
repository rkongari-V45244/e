package com.aflac.core.experience.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FormIdsSitusDetail {
	@JsonProperty("form-id")
	private String formId = null;

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	
	
	
}
