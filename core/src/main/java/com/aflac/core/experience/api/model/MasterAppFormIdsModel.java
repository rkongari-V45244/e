package com.aflac.core.experience.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MasterAppFormIdsModel {

	@JsonProperty("form-id")
	private String formId;
	
	private String apiFormId;

	@JsonProperty("product-series")
	private List<String> series;

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}
	
	public String getApiFormId() {
		return apiFormId;
	}

	public void setApiFormId(String formId) {
		this.apiFormId = formId;
	}

	public List<String> getSeries() {
		return series;
	}

	public void setSeries(List<String> series) {
		this.series = series;
	}

}
