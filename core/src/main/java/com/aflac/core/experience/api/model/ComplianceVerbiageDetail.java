package com.aflac.core.experience.api.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ComplianceVerbiageDetail {
	@JsonProperty("compliance-verbiage-list")
	private List<ComplianceDetail> complianceVerbiageList = null;

	public List<ComplianceDetail> getComplianceVerbiageList() {
		return complianceVerbiageList;
	}

	public void setComplianceVerbiageList(List<ComplianceDetail> complianceVerbiageList) {
		this.complianceVerbiageList = complianceVerbiageList;
	}

}
