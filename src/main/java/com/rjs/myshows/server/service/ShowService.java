package com.rjs.myshows.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rjs.myshows.domain.dto.ShowDto;
import com.rjs.myshows.server.domain.entity.ShowEntity;
import com.rjs.myshows.server.repository.ShowRepository;
import org.modelmapper.ModelMapper;
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

	public Optional<ShowEntity> findById(String id) {
		return showRepository.findById(id);
	}

	public Optional<ShowEntity> findByMdbId(String mdbId) {
		return showRepository.findByMdbId(mdbId);
	}

	public Optional<ShowEntity> findByImdbId(String imdbId) {
		return showRepository.findByImdbId(imdbId);
	}

	public List<ShowEntity> findByTitleLike(String title) {
		return showRepository.findByTitleLike("%" + title + "%");
	}

	public List<ShowEntity> findByStarRating(int starRating) {
		return showRepository.findByStarRating(starRating);
	}

	public ShowEntity save(ShowEntity show) {
		return showRepository.save(show);
	}

	@Override
	public ShowDto convertToDto(ShowEntity entity) {
		return modelMapper.map(entity, ShowDto.class);
	}

	@Override
	public ShowEntity convertToEntity(ShowDto showDto) {
		Optional<ShowEntity> opt = findById(showDto.getId());

		if (opt.isPresent()) {
			ShowEntity showEntity = opt.get();
			modelMapper.map(showDto, showEntity);

			return showEntity;
		}

		return null;
	}
}
