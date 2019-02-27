package com.rjs.myshows.server.service.mdb.tmdb.domain;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TmdbShowDetail extends TmdbShow {
	public ArrayList<TmdbGenre> genres = new ArrayList<>();
	public String imdbId= "";
	public int runtime = -1;
	public String tagline = "";
}
