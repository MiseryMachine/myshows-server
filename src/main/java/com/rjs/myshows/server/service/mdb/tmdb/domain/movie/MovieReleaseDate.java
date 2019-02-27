package com.rjs.myshows.server.service.mdb.tmdb.domain.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieReleaseDate {
	@JsonProperty("certification")
	public String rating;
	@JsonProperty("iso_639_1")
	public String isoName;
	@JsonProperty("release_date")
	public String date;
	@JsonProperty("type")
	public int type;
}
