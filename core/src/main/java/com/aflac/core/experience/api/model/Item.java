package com.aflac.core.experience.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Item
 */
public class Item {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("value")
	private String value = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
}
