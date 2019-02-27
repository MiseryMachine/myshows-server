package com.rjs.myshows.server.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rjs.myshows.domain.dto.ShowDto;
import com.rjs.myshows.domain.dto.ShowTypeDto;
import com.rjs.myshows.server.domain.entity.ShowEntity;
import com.rjs.myshows.server.domain.entity.ShowTypeEntity;
import com.rjs.myshows.server.service.ShowService;
import com.rjs.myshows.server.service.ShowTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Profile({"development", "testing"})
@Configuration
public class InitDataConfig {
	private final Logger logger = LoggerFactory.getLogger(InitDataConfig.class);

	private ObjectMapper jsonObjectMapper;
	private ShowService showService;
	private ShowTypeService showTypeService;

	public InitDataConfig(ObjectMapper jsonObjectMapper, ShowService showService, ShowTypeService showTypeService) {
		this.jsonObjectMapper = jsonObjectMapper;
		this.showService = showService;
		this.showTypeService = showTypeService;
	}

	@Bean
	public String initData() {
		FileSystemResourceLoader resourceLoader = new FileSystemResourceLoader();
		Resource dataResource = resourceLoader.getResource("classpath:show-data.json");

		try {
			try (InputStream is = dataResource.getInputStream()) {
				JsonNode jsonNode = jsonObjectMapper.readTree(is);

				if (jsonNode.has("user")) {
					// TODO: add user support
				}

				if (jsonNode.has("showTypes")) {
					String jsonString = jsonNode.get("showTypes").toString();
					initShowTypes(jsonObjectMapper.readValue(jsonString, ShowTypeDto[].class));
				}

				if (jsonNode.has("shows")) {
					String jsonString = jsonNode.get("shows").toString();
					initShows(jsonObjectMapper.readValue(jsonString, ShowDto[].class));
				}
			}
		}
		catch (IOException e) {
			logger.error("Error reading data file.", e);
		}

		return "data initialized";
	}

	private void initShowTypes(ShowTypeDto[] showTypeDtos) {
		for (ShowTypeDto showTypeDto : showTypeDtos) {
			ShowTypeEntity existing = showTypeService.findByName(showTypeDto.getName()).orElse(new ShowTypeEntity());

			if (existing.getId() == null) {
				existing.setName(showTypeDto.getName());
			}

			existing.setGenres(showTypeDto.getGenres());
			existing.setRatings(showTypeDto.getRatings());

			showTypeService.save(existing);
			logger.info(String.format("Show type saved: %s", showTypeDto.getName()));
		}
	}

	private void initShows(ShowDto[] showDtos) {
		for (ShowDto showDto : showDtos) {
			List<ShowEntity> existingList = showService.findByTitleExact(showDto.getTitle());

			if (existingList.isEmpty()) {
				ShowEntity show = showService.convertToEntity(showDto);
				show = showService.save(show);
				logger.info(String.format("Show created: %s", show.getTitle()));
			}
			else {
				logger.info(String.format("At least one show was found having the title of %s", showDto.getTitle()));
			}
		}
	}
}
