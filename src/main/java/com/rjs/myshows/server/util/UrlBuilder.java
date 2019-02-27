package com.rjs.myshows.server.util;

import lombok.Getter;

@Getter
public class UrlBuilder {
	private String url;

	private UrlBuilder(String url) {
		this.url = url;
	}

	public static UrlBuilder create(String url) {
		return new UrlBuilder(url);
	}

	public UrlBuilder addPath(String path) {
		url += path;

		return this;
	}
}
