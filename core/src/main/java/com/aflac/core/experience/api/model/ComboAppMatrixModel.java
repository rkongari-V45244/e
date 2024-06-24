package com.aflac.core.experience.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComboAppMatrixModel {
	
	@JsonProperty("situs-state")
	private String situsState;

	@JsonProperty("form-id")
	private String formId;

	public List<Products> products;

	public String getSitusState() {
		return situsState;
	}

	public void setSitusState(String situsState) {
		this.situsState = situsState;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public List<Products> getProducts() {
		return products;
	}

	public void setProducts(List<Products> products) {
		this.products = products;
	}

}
