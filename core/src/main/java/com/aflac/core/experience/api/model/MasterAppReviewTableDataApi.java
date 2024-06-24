package com.aflac.core.experience.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("master-app-review-table-data")
public class MasterAppReviewTableDataApi {
	
	@JsonProperty("form-id")
    private String formId;
	
	@JsonProperty("api-form-id")
	private String apiFormId;
    
	@JsonProperty("products")
    private String products;
    
	@JsonProperty("signature-status")
    private String signStatus;
	
	@JsonProperty("signature-checkbox")
	private String signatureCheckbox;
	
	@JsonProperty("masterapp-sent-datetime")
	private String masterappSentDatetime;
	
	@JsonProperty("masterapp-received-datetime")
    private String masterappReceivedDatetime;
    
	@JsonProperty("filenet-document-id")
    private String filenetDocumentId;
	
	@JsonProperty("agreement-id")
	private String agreementId;

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public String getApiFormId() {
		return apiFormId;
	}

	public void setApiFormId(String apiFormId) {
		this.apiFormId = apiFormId;
	}

	public String getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}

	public String getSignatureCheckbox() {
		return signatureCheckbox;
	}

	public void setSignatureCheckbox(String signatureCheckbox) {
		this.signatureCheckbox = signatureCheckbox;
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

	public String getFilenetDocumentId() {
		return filenetDocumentId;
	}

	public void setFilenetDocumentId(String filenetDocumentId) {
		this.filenetDocumentId = filenetDocumentId;
	}

	public String getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}
	
	

}
