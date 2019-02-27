package com.rjs.myshows.server.service.mdb.tmdb.domain.tv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TvSummary {
	public int id = -1;
	@JsonProperty("name")
	public String title = "";
	public String overview = "";
	@JsonProperty("poster_path")
	public String posterPath = "";
	@JsonProperty("first_air_date")
	public String airDate = "";
}
