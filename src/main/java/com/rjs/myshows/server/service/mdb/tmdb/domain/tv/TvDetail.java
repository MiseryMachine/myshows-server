package com.rjs.myshows.server.service.mdb.tmdb.domain.tv;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rjs.myshows.server.service.mdb.tmdb.domain.TmdbGenre;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TvDetail extends TvSummary {
	public ArrayList<TmdbGenre> genres = new ArrayList<>();
	@JsonProperty("content_ratings")
	public ContentRatingResults contentRatingResults;
}
