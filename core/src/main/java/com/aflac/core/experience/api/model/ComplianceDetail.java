package com.aflac.core.experience.api.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ComplianceDetail {

	@JsonProperty("record-id")
	private String recordId = null;

	@JsonProperty("compliance-text")
	private String complianceText = null;

	@JsonProperty("verbiage-type")
	private Item verbiageType = null;

	@JsonProperty("state")
	private List<Item> state = null;

	@JsonProperty("lob")
	private List<Item> lob = null;

	@JsonProperty("label")
	private Item label = null;

	@JsonProperty("active-status")
	private Boolean activeStatus = null;
	
	@JsonProperty("product-id")
	private String productId;

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getComplianceText() {
		return complianceText;
	}

	public void setComplianceText(String complianceText) {
		this.complianceText = complianceText;
	}

	public Item getVerbiageType() {
		return verbiageType;
	}

	public void setVerbiageType(Item verbiageType) {
		this.verbiageType = verbiageType;
	}

	public List<Item> getState() {
		return state;
	}

	public void setState(List<Item> state) {
		this.state = state;
	}

	public List<Item> getLob() {
		return lob;
	}

	public void setLob(List<Item> lob) {
		this.lob = lob;
	}

	public Item getLabel() {
		return label;
	}

	public void setLabel(Item label) {
		this.label = label;
	}

	public Boolean getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	
}
