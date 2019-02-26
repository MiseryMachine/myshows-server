package com.rjs.myshows.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rjs.myshows.domain.ShowFilter;
import com.rjs.myshows.domain.dto.ShowDto;
import com.rjs.myshows.server.domain.entity.ShowEntity;
import com.rjs.myshows.server.service.ShowService;

@RestController
@RequestMapping("/show")
public class ShowController {
	private ShowService showService;

	public ShowController(ShowService showService) {
		this.showService = showService;
	}

	@PostMapping("/search")
	public List<ShowDto> searchShows(@RequestBody ShowFilter showFilter) {
		List<ShowEntity> shows = showService.filterShows(showFilter);

		return showService.convertToDto(shows);
	}
}
