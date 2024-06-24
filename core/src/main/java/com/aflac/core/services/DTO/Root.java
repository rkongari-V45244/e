package com.aflac.core.services.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Root{
    @JsonProperty("Document") 
    public Document document;
}
