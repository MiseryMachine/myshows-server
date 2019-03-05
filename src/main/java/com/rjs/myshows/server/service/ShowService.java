package com.rjs.myshows.server.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.rjs.myshows.domain.ShowFilter;
import com.rjs.myshows.domain.dto.ShowDto;
import com.rjs.myshows.server.domain.entity.ShowEntity;
import com.rjs.myshows.server.repository.ShowRepository;
import com.rjs.myshows.server.repository.specification.IsMemberSpecification;
import com.rjs.myshows.server.repository.specification.TextLikeSpecification;
import com.rjs.myshows.server.repository.specification.ValueEqualsSpecification;
import com.rjs.myshows.server.repository.specification.ValueInSpecification;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service("showService")
public class ShowService extends AbstractService<ShowEntity, ShowDto> {
	private final Logger logger = LoggerFactory.getLogger(ShowService.class);

	private ShowRepository showRepository;

	public ShowService(ModelMapper modelMapper, ShowRepository showRepository) {
		super(modelMapper);
		this.showRepository = showRepository;
	}

	public Optional<ShowEntity> findById(Long id) {
		return showRepository.findById(id);
	}

	public Optional<ShowEntity> findByMdbId(String mdbId) {
		return showRepository.findByMdbId(mdbId);
	}

	public Optional<ShowEntity> findByImdbId(String imdbId) {
		return showRepository.findByImdbId(imdbId);
	}

	public List<ShowEntity> findByTitleExact(String title) {
		return showRepository.findByTitle(title);
	}

	public List<ShowEntity> findByTitleLike(String title) {
		return showRepository.findByTitleLike("%" + title + "%", Sort.by("title").ascending());
	}

	public List<ShowEntity> findByStarRating(int starRating) {
		return showRepository.findByStarRating(starRating, Sort.by("title").ascending());
	}

	public List<ShowEntity> filterShows(ShowFilter showFilter) {
		if (showFilter == null) {
			return new ArrayList<>();
		}

		Specification<ShowEntity> showSpecification = null;

		if (StringUtils.isNotBlank(showFilter.getTitle())) {
			// Add the title filter
			showSpecification = new TextLikeSpecification<>("title", showFilter.getTitle());
		}

		if (StringUtils.isNotBlank(showFilter.getShowTypeName())) {
			// Add the show type filter
			ValueEqualsSpecification<ShowEntity> showTypeSpec = new ValueEqualsSpecification<>("showType",
				showFilter.getShowTypeName());

			if (showSpecification != null) {
				showSpecification = showSpecification.and(showTypeSpec);
			}
			else {
				showSpecification = showTypeSpec;
			}
		}

		if (showFilter.getGenres() != null && !showFilter.getGenres().isEmpty()) {
			// Add the genre filter
			IsMemberSpecification<ShowEntity> genreSpec = new IsMemberSpecification<>("genres",
				showFilter.getGenres());

			if (showSpecification != null) {
				showSpecification = showSpecification.and(genreSpec);
			}
			else {
				showSpecification = genreSpec;
			}
		}

		if (showFilter.getShowRatings() != null && !showFilter.getShowRatings().isEmpty()) {
			// Add the show rating filter
			ValueInSpecification<ShowEntity, String> showRatingSpec = new ValueInSpecification<>("showRating",
				showFilter.getShowRatings());

			if (showSpecification != null) {
				showSpecification = showSpecification.and(showRatingSpec);
			}
			else {
				showSpecification = showRatingSpec;
			}
		}

		if (showFilter.getMediaFormats() != null && !showFilter.getMediaFormats().isEmpty()) {
			// Add the media format filter
			ValueInSpecification<ShowEntity, String> mediaFormatSpec = new ValueInSpecification<>("mediaFormat",
				showFilter.getMediaFormats());

			if (showSpecification != null) {
				showSpecification = showSpecification.and(mediaFormatSpec);
			}
			else {
				showSpecification = mediaFormatSpec;
			}
		}

		return showSpecification != null ?
			showRepository.findAll(showSpecification, Sort.by("title").ascending()) :
			new ArrayList<>();
	}

	public ShowEntity save(ShowEntity show) {
		return showRepository.save(show);
	}

	public void delete(Long showId) {
		showRepository.deleteById(showId);
	}

	@Override
	public ShowDto convertToDto(ShowEntity showEntity) {
		return showEntity != null ? modelMapper.map(showEntity, ShowDto.class) : null;
	}

	@Override
	public List<ShowDto> convertToDto(List<ShowEntity> showEntities) {
		if (showEntities == null || showEntities.isEmpty()) {
			return new ArrayList<>();
		}

		Type dtoListType = new TypeToken<List<ShowDto>>() {}.getType();

		return modelMapper.map(showEntities, dtoListType);
	}

	@Override
	public ShowEntity convertToEntity(ShowDto showDto) {
		if (showDto == null) {
			return null;
		}

		ShowEntity showEntity = new ShowEntity();

		if (showDto.getId() != null) {
			Optional<ShowEntity> opt = findById(showDto.getId());

			if (opt.isPresent()) {
				showEntity = opt.get();
			}
			else {
				logger.warn(String.format("Show entity with id %d does not exist in datasource.", showDto.getId()));

				return null;
			}
		}

		modelMapper.map(showDto, showEntity);

		return showEntity;
	}

	@Override
	public List<ShowEntity> convertToEntity(List<ShowDto> showDtos) {
		if (showDtos == null || showDtos.isEmpty()) {
			return new ArrayList<>();
		}


		final List<ShowEntity> showEntities = new ArrayList<>();

		showDtos.forEach(dto -> {
			ShowEntity showEntity = convertToEntity(dto);

			if (showEntity != null) {
				showEntities.add(showEntity);
			}
		});

		return showEntities;
	}
}
