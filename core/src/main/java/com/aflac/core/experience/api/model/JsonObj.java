package com.aflac.core.experience.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonObj {
    
    @JsonProperty("metadata")
    public String metaData;

	@JsonProperty("data")
	public ApiCaseBuilderForm caseBuild;

    public ApiCaseBuilderForm getCaseBuild() {
        return caseBuild;
    }

    public void setCaseBuild(ApiCaseBuilderForm caseBuild) {
        this.caseBuild = caseBuild;
    }
	
	
}
