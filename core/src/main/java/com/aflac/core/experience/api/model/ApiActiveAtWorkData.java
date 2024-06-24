package com.aflac.core.experience.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("active-at-work-data")
public class ApiActiveAtWorkData {

	@JsonProperty("class-name")
	private String className;
	
	@JsonProperty("actively-at-work-hours")
    private Integer activelyAtWorkHours;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getActivelyAtWorkHours() {
		return activelyAtWorkHours;
	}

	public void setActivelyAtWorkHours(Integer activelyAtWorkHours) {
		this.activelyAtWorkHours = activelyAtWorkHours;
	}
	
	
}
