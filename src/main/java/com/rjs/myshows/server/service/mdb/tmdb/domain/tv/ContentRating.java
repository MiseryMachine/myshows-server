package com.rjs.myshows.server.service.mdb.tmdb.domain.tv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentRating {
	@JsonProperty("iso_3166_1")
	public String isoName;
	@JsonProperty("rating")
	public String rating;
}
