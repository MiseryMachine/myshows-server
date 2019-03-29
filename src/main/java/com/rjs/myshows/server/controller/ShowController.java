package com.rjs.myshows.server.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rjs.myshows.server.domain.ShowFilter;
import com.rjs.myshows.server.domain.dto.ShowDto;
import com.rjs.myshows.server.domain.entity.ShowEntity;
import com.rjs.myshows.server.domain.resource.ShowResource;
import com.rjs.myshows.server.domain.resource.ShowResourceAssembler;
import com.rjs.myshows.server.service.ShowService;

@RestController
@RequestMapping("/show")
public class ShowController {
	private final String linkHeaderFmt = "<%s>; rel=\"%s\"";
	private ShowService showService;
	private EntityLinks links;
	private ShowResourceAssembler showResourceAssembler;

	public ShowController(ShowService showService, EntityLinks links, ShowResourceAssembler showResourceAssembler) {
		this.showService = showService;
		this.links = links;
		this.showResourceAssembler = showResourceAssembler;
	}

	@GetMapping("/{id}")
	public ResponseEntity<ShowDto> get(@PathVariable("id") Long id) {
		Optional<ShowEntity> showEntity = showService.findById(id);

		if (showEntity.isPresent()) {
			ShowDto showDto = showService.convertToDto(showEntity.get());

			return ResponseEntity.ok(showDto);
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ShowDto());
	}

	@PostMapping("/search")
	public ResponseEntity<?> searchShowsPaged(@RequestBody ShowFilter showFilter,
											  @PageableDefault(size = 25, sort = "title") Pageable pageable,
											  PagedResourcesAssembler<ShowEntity> pagedAssembler) {
		Page<ShowEntity> showPage = showService.filterShows(showFilter, pageable);
		PagedResources<ShowResource> pagedResources = pagedAssembler.toResource(showPage, showResourceAssembler);
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Link", createLinkHeader(pagedResources));

		pagedResources.add(ControllerLinkBuilder.linkTo(ShowController.class).slash("/search").withSelfRel());
		return new ResponseEntity<>(pagedResources, HttpStatus.OK);
	}

	private String createLinkHeader(PagedResources<ShowResource> pagedResources) {
		final StringBuilder sb = new StringBuilder();
		sb.append(String.format(linkHeaderFmt, pagedResources.getLinks("first").get(0).getHref(), "first"));
		sb.append(", ");
		sb.append(String.format(linkHeaderFmt, pagedResources.getLinks("next").get(0).getHref(), "next"));

		return sb.toString();
	}
}
