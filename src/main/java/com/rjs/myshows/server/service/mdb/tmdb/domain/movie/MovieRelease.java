package com.rjs.myshows.server.service.mdb.tmdb.domain.movie;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieRelease {
	@JsonProperty("iso_3166_1")
	public String isoName;
	@JsonProperty("releases")
	public ArrayList<MovieReleaseDate> releases = new ArrayList<>();
}
