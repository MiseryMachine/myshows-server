package com.rjs.myshows.server.config;

import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.rjs.myshows.util.ImageUtil;
import org.modelmapper.ModelMapper;

@Configuration
public class MyShowsConfig {
	public MyShowsConfig() {
	}

	@Bean
	public ImageUtil imageUtil() {
		return new ImageUtil();
	}

	@Bean
	public DateTimeFormatter dateFormat(AppProperties appProperties) {
		return DateTimeFormatter.ofPattern(appProperties.getDatePattern());
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
