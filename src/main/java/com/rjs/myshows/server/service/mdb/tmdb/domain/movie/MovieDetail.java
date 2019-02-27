package com.rjs.myshows.server.service.mdb.tmdb.domain.movie;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rjs.myshows.server.service.mdb.tmdb.domain.TmdbGenre;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDetail extends MovieSummary {
	public ArrayList<TmdbGenre> genres = new ArrayList<>();
	public String imdbId= "";
	public int runtime = -1;
	public String tagline = "";
	@JsonProperty("release_dates")
	public MovieReleaseResults releaseDates;
}
