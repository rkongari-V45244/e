package com.aflac.core.experience.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationDetails {

	@JsonProperty("are-locations-specified")
	private boolean areLocationsSpecified;
	@JsonProperty("location-detail")
	private List<LocationDetail> locationDetail;
	
	public boolean isAreLocationsSpecified() {
		return areLocationsSpecified;
	}
	public void setAreLocationsSpecified(boolean areLocationsSpecified) {
		this.areLocationsSpecified = areLocationsSpecified;
	}
	public List<LocationDetail> getLocationDetail() {
		return locationDetail;
	}
	public void setLocationDetail(List<LocationDetail> locationDetail) {
		this.locationDetail = locationDetail;
	}
	
}
