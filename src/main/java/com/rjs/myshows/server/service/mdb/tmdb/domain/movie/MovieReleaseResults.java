package com.rjs.myshows.server.service.mdb.tmdb.domain.movie;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieReleaseResults {
	@JsonProperty("results")
	public ArrayList<MovieRelease> results = new ArrayList<>();
}
