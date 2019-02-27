package com.rjs.myshows.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rjs.myshows.server.service.ShowService;
import com.rjs.myshows.server.service.ShowTypeService;
import com.rjs.myshows.server.service.mdb.MdbService;

@RestController
@RequestMapping("/admin/mdb")
public class MdbController {
	private MdbService mdbService;
	private ShowService showService;
	private ShowTypeService showTypeService;

	public MdbController(MdbService mdbService, ShowService showService, ShowTypeService showTypeService) {
		this.mdbService = mdbService;
		this.showService = showService;
		this.showTypeService = showTypeService;
	}
}
