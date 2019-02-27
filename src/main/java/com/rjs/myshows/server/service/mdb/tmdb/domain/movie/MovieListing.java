package com.rjs.myshows.server.service.mdb.tmdb.domain.movie;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieListing {
	public int page = 0;
	public ArrayList<MovieSummary> results = new ArrayList<>();
}
