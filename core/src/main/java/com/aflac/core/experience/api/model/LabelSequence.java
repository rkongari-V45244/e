package com.aflac.core.experience.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LabelSequence {

	private String id;
	@JsonProperty("items")
	private List<Item> sequence;

	public List<Item> getSequence() {
		return sequence;
	}

	public void setSequence(List<Item> sequence) {
		this.sequence = sequence;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
