package com.rjs.myshows.server.service.mdb;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.rjs.myshows.domain.dto.ShowDto;
import com.rjs.myshows.server.config.AppProperties;
import com.rjs.myshows.util.ImageUtil;

public abstract class MdbService<SHOW> {
	protected AppProperties appProperties;
	protected ImageUtil imageUtil;

	protected MdbService(AppProperties appProperties, ImageUtil imageUtil) {
		this.appProperties = appProperties;
		this.imageUtil = imageUtil;
	}

	public abstract List<SHOW> searchShows(String showTypeName, String title);
	public abstract Optional<ShowDto> addShow(String mdbId, String showTypeName);
	public abstract Set<String> getGenres(String showTypeName);

	public String getLocalImagePath(String showId) {
		return String.format("%s/shows/%s/images", appProperties.getLocalFilePath(), showId);
	}
}
