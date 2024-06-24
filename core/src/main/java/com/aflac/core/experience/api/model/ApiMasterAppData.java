package com.aflac.core.experience.api.model;

import java.util.List;

import com.aflac.xml.masterApp.models.MasterAppReviewTable;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiMasterAppData {
	
	@JsonProperty("master-app-entity-id")
	private String masterAppEntityId;
	
	@JsonProperty("version")
	private int version;
	
	@JsonProperty("situs-state")
	private String situsState;
	
	@JsonProperty("group-type")
	private String groupType;
	
	@JsonProperty("census-enrollement")
	private String censusEnrollment;
	
	@JsonProperty("eligible-series")
	private String eligibleSeries;
	
	@JsonProperty("form-ids")
	private String formIds;
	
	@JsonProperty("for-status-update")
	private Boolean forStatusUpdate;
	
	@JsonProperty("signature-status")
	private String signatureStatus;
	
	@JsonProperty("filenet-document-id")
	private String filenetDocumentId;
	
	@JsonProperty("masterapp-sent-datetime")
	private String masterappSentDatetime;
	
	@JsonProperty("masterapp-received-datetime")
	private String masterappReceivedDatetime;
	
	@JsonProperty("account-eligibility")
	private ApiMasterAppAccountEligiblity masterAppAccountEligiblity;
	
	@JsonProperty("product")
	private List<ApiMasterAppProduct> masterAppProducts;
	
	@JsonProperty("master-app-review-table")
	private MasterAppReviewTableApi masterAppReviewTable;
	
	@JsonProperty("data-status")
	private String dataStatus;
	
	@JsonProperty("first-signer-email-id")
	private String firstSignerEmailId;
	
	@JsonProperty("second-signer-email-id")
	private String secondSignerEmailId;

	public String getMasterAppEntityId() {
		return masterAppEntityId;
	}

	public void setMasterAppEntityId(String masterAppEntityId) {
		this.masterAppEntityId = masterAppEntityId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getSitusState() {
		return situsState;
	}

	public void setSitusState(String situsState) {
		this.situsState = situsState;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getCensusEnrollment() {
		return censusEnrollment;
	}

	public void setCensusEnrollment(String censusEnrollment) {
		this.censusEnrollment = censusEnrollment;
	}

	public String getEligibleSeries() {
		return eligibleSeries;
	}

	public void setEligibleSeries(String eligibleSeries) {
		this.eligibleSeries = eligibleSeries;
	}

	public String getFormIds() {
		return formIds;
	}

	public void setFormIds(String formIds) {
		this.formIds = formIds;
	}
	
	public Boolean getForStatusUpdate() {
		return forStatusUpdate;
	}

	public void setForStatusUpdate(Boolean forStatusUpdate) {
		this.forStatusUpdate = forStatusUpdate;
	}

	public String getSignatureStatus() {
		return signatureStatus;
	}

	public void setSignatureStatus(String signatureStatus) {
		this.signatureStatus = signatureStatus;
	}

	public String getFilenetDocumentId() {
		return filenetDocumentId;
	}

	public void setFilenetDocumentId(String filenetDocumentId) {
		this.filenetDocumentId = filenetDocumentId;
	}

	public String getMasterappSentDatetime() {
		return masterappSentDatetime;
	}

	public void setMasterappSentDatetime(String masterappSentDatetime) {
		this.masterappSentDatetime = masterappSentDatetime;
	}

	public String getMasterappReceivedDatetime() {
		return masterappReceivedDatetime;
	}

	public void setMasterappReceivedDatetime(String masterappReceivedDatetime) {
		this.masterappReceivedDatetime = masterappReceivedDatetime;
	}

	public ApiMasterAppAccountEligiblity getMasterAppAccountEligiblity() {
		return masterAppAccountEligiblity;
	}

	public void setMasterAppAccountEligiblity(ApiMasterAppAccountEligiblity masterAppAccountEligiblity) {
		this.masterAppAccountEligiblity = masterAppAccountEligiblity;
	}

	public List<ApiMasterAppProduct> getMasterAppProducts() {
		return masterAppProducts;
	}

	public void setMasterAppProducts(List<ApiMasterAppProduct> masterAppProducts) {
		this.masterAppProducts = masterAppProducts;
	}

	public MasterAppReviewTableApi getMasterAppReviewTable() {
		return masterAppReviewTable;
	}

	public void setMasterAppReviewTable(MasterAppReviewTableApi masterAppReviewTable) {
		this.masterAppReviewTable = masterAppReviewTable;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String getFirstSignerEmailId() {
		return firstSignerEmailId;
	}

	public void setFirstSignerEmailId(String firstSignerEmailId) {
		this.firstSignerEmailId = firstSignerEmailId;
	}

	public String getSecondSignerEmailId() {
		return secondSignerEmailId;
	}

	public void setSecondSignerEmailId(String secondSignerEmailId) {
		this.secondSignerEmailId = secondSignerEmailId;
	}
	
}
