package com.aflac.core.experience.api.model;

import java.util.List;

public class MasterAppSeriesResponseModel {
	
	private List<String> formids;
	private List<String> eligibleSeries;
	private MasterAppSeriesData masterAppSeriesData;
	
	public List<String> getFormids() {
		return formids;
	}
	public void setFormids(List<String> formids) {
		this.formids = formids;
	}
	public List<String> getEligibleSeries() {
		return eligibleSeries;
	}
	public void setEligibleSeries(List<String> eligibleSeries) {
		this.eligibleSeries = eligibleSeries;
	}
	public MasterAppSeriesData getMasterAppSeriesData() {
		return masterAppSeriesData;
	}
	public void setMasterAppSeriesData(MasterAppSeriesData masterAppSeriesData) {
		this.masterAppSeriesData = masterAppSeriesData;
	}
	
	
	

}
