package com.aflac.core.services.DTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
public class Properties{
    @JsonProperty("Property") 
    public ArrayList<Property> property;
}