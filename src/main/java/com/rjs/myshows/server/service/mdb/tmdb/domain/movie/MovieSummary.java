package com.rjs.myshows.server.service.mdb.tmdb.domain.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieSummary {
	public int id = -1;
	public String title = "";
	public String overview = "";
	@JsonProperty("poster_path")
	public String posterPath = "";
	@JsonProperty("release_date")
	public String releaseDate = "";
}
