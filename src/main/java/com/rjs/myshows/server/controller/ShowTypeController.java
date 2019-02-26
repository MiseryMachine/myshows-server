package com.rjs.myshows.server.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rjs.myshows.server.domain.entity.ShowTypeEntity;
import com.rjs.myshows.server.service.ShowTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController("showTypeController")
@RequestMapping("/show-type")
public class ShowTypeController {
	private final Logger logger = LoggerFactory.getLogger(ShowTypeController.class);

	private ShowTypeService showTypeService;

	public ShowTypeController(ShowTypeService showTypeService) {
		this.showTypeService = showTypeService;
	}

/*
	@PostMapping
	public ShowType saveShowType(@RequestBody ShowTypeDto showTypeDto) {
		ShowTypeEntity showType = showTypeService.convertToEntity(showTypeDto);

		showType = showTypeService.save(showType);

		return showTypeService.convertToDto(showType);
	}
*/

	@GetMapping("/all")
	public Map<String, ShowTypeEntity> getShowTypes() {
		return showTypeService.findAllMapped();
	}
}
