package com.aflac.core.experience.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Products {

	@JsonProperty("id")
	private String id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("series-details")
	public List<SeriesDetails> seriesDetails;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SeriesDetails> getSeriesDetails() {
		return seriesDetails;
	}

	public void setSeriesDetails(List<SeriesDetails> seriesDetails) {
		this.seriesDetails = seriesDetails;
	}
	
	

}
