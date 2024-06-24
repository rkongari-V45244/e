package com.aflac.core.experience.api.model;

import java.util.List;

import com.aflac.xml.masterApp.models.MasterAppReviewTableData;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MasterAppReviewTableApi {

	@JsonProperty("master-app-review-table-data")
	private List<MasterAppReviewTableDataApi> masterAppReviewTableData;

	public List<MasterAppReviewTableDataApi> getMasterAppReviewTableData() {
		return masterAppReviewTableData;
	}

	public void setMasterAppReviewTableData(List<MasterAppReviewTableDataApi> masterAppReviewTableData) {
		this.masterAppReviewTableData = masterAppReviewTableData;
	}
	
	
}
