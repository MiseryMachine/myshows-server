package com.rjs.myshows.server.service.mdb.tmdb.domain;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TmdbShowListing {
	public int page = 0;
	public ArrayList<TmdbShow> results = new ArrayList<>();
}
