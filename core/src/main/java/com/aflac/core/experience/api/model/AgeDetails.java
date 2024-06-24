package com.aflac.core.experience.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AgeDetails {

	@JsonProperty("age-based-on")
	private String agebasedon;
	@JsonProperty("age-calculated")
	private String agecalculated;
	@JsonProperty("age-calculation")
	private String agecalculation;
	
	public String getAgebasedon() {
		return agebasedon;
	}
	public void setAgebasedon(String agebasedon) {
		this.agebasedon = agebasedon;
	}
	public String getAgecalculated() {
		return agecalculated;
	}
	public void setAgecalculated(String agecalculated) {
		this.agecalculated = agecalculated;
	}
	public String getAgecalculation() {
		return agecalculation;
	}
	public void setAgecalculation(String agecalculation) {
		this.agecalculation = agecalculation;
	}
		
}
