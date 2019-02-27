package com.rjs.myshows.server.service.mdb.tmdb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TmdbGenre {
	public int id = -1;
	public String name = "";
}
