package com.rjs.myshows.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("my-shows")
@Getter
@Setter
public class AppProperties {
	private String datePattern;
	private String localFilePath;

	public AppProperties() {
	}
}
