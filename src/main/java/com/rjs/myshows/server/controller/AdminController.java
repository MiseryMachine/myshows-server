package com.rjs.myshows.server.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rjs.myshows.domain.dto.ShowTypeDto;
import com.rjs.myshows.server.domain.entity.ShowTypeEntity;
import com.rjs.myshows.server.service.ShowTypeService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	private ShowTypeService showTypeService;

	public AdminController(ShowTypeService showTypeService) {
		this.showTypeService = showTypeService;
	}

	@PostMapping("/load-show-types")
	public List<ShowTypeDto> loadShowTypes(@RequestBody List<ShowTypeDto> showTypeDtos) {
		if (showTypeDtos == null || showTypeDtos.isEmpty()) {
			return new ArrayList<>();
		}

		List<ShowTypeEntity> showTypeEntities = showTypeService.convertToEntity(showTypeDtos);
		showTypeEntities = showTypeService.saveAll(showTypeEntities);
		List<ShowTypeDto> retList = showTypeService.convertToDto(showTypeEntities);

		return retList;
	}
}
