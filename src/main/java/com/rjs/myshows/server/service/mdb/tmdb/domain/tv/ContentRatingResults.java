package com.rjs.myshows.server.service.mdb.tmdb.domain.tv;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentRatingResults {
	@JsonProperty("results")
	public ArrayList<ContentRating> results = new ArrayList<>();
}
