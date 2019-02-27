package com.rjs.myshows.server.service.mdb.tmdb.domain.tv;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TvListing {
	public int page = 0;
	public ArrayList<TvSummary> results = new ArrayList<>();
}
