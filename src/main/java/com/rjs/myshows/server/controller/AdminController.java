package com.rjs.myshows.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.web.bind.annotation.*;

import com.rjs.myshows.domain.dto.ShowDto;
import com.rjs.myshows.domain.dto.ShowTypeDto;
import com.rjs.myshows.server.domain.entity.ShowEntity;
import com.rjs.myshows.server.domain.entity.ShowTypeEntity;
import com.rjs.myshows.server.service.ShowService;
import com.rjs.myshows.server.service.ShowTypeService;
import com.rjs.myshows.server.service.mdb.MdbService;
import com.rjs.myshows.server.service.mdb.ShowSummary;

@RestController
@RequestMapping("/admin")
public class AdminController {
	private MdbService mdbService;
	private ShowService showService;
	private ShowTypeService showTypeService;

	public AdminController(MdbService mdbService, ShowService showService, ShowTypeService showTypeService) {
		this.mdbService = mdbService;
		this.showService = showService;
		this.showTypeService = showTypeService;
	}

	@GetMapping("/mdb/search/{showTypeName}/{title}")
	public List<ShowSummary> search(@PathVariable("showTypeName") String showTypeName, @PathVariable("title") String title) {
		return mdbService.searchShows(showTypeName, title);
	}

	@GetMapping("/mdb/search-genres/{showTypeName}")
	public Set<String> searchGenres(@PathVariable("showTypeName") String showTypeName) {
		return mdbService.getGenres(showTypeName);
	}

	@PostMapping("/show/add")
	public ShowDto addShow(@RequestParam String tmdbId, @RequestParam String showTypeName) {
		Optional<ShowDto> show = mdbService.addShow(tmdbId, showTypeName);

		return show.orElse(null);
	}

	@PostMapping("/show/update")
	public ShowDto updateShow(@RequestBody ShowDto showDto) {
		ShowEntity showEntity = showService.convertToEntity(showDto);
		showEntity = showService.save(showEntity);

		return showService.convertToDto(showEntity);
	}

	@DeleteMapping("/show/delete/{showId}")
	public String deleteShow(@PathVariable("showId") Long showId) {
		showService.delete(showId);

		return "SUCCESS";
	}

	@PostMapping("/show-type/load")
	public List<ShowTypeDto> loadShowTypes(@RequestBody List<ShowTypeDto> showTypeDtos) {
		if (showTypeDtos == null || showTypeDtos.isEmpty()) {
			return new ArrayList<>();
		}

		List<ShowTypeEntity> showTypeEntities = showTypeService.convertToEntity(showTypeDtos);
		showTypeEntities = showTypeService.saveAll(showTypeEntities);
		List<ShowTypeDto> retList = showTypeService.convertToDto(showTypeEntities);

		return retList;
	}

	@PostMapping("/show-type/update")
	public ShowTypeDto updateShowType(ShowTypeDto showTypeDto) {
		ShowTypeEntity showTypeEntity = showTypeService.convertToEntity(showTypeDto);

		showTypeEntity = showTypeService.save(showTypeEntity);

		return showTypeService.convertToDto(showTypeEntity);
	}
}
