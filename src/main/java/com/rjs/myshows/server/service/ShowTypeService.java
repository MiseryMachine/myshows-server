package com.rjs.myshows.server.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rjs.myshows.domain.dto.ShowTypeDto;
import com.rjs.myshows.server.domain.entity.ShowTypeEntity;
import com.rjs.myshows.server.repository.ShowTypeRepository;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service("showTypeService")
public class ShowTypeService extends AbstractService<ShowTypeEntity, ShowTypeDto> {
	private final Logger logger = LoggerFactory.getLogger(ShowTypeService.class);

	private ShowTypeRepository showTypeRepository;

	public ShowTypeService(ModelMapper modelMapper, ShowTypeRepository showTypeRepository) {
		super(modelMapper);
		this.showTypeRepository = showTypeRepository;
	}

	public Optional<ShowTypeEntity> findById(String id) {
		return showTypeRepository.findById(id);
	}

	public Optional<ShowTypeEntity> findByName(String name) {
		return showTypeRepository.findByName(name);
	}

	public List<ShowTypeEntity> findAll() {
		return showTypeRepository.findAll(Sort.by("name").ascending());
	}

	public Map<String, ShowTypeEntity> findAllMapped() {
		return findAll().stream()
			.collect(
				LinkedHashMap::new,
				(map, e) -> map.put(e.getName(), e),
				Map::putAll);
	}

	public ShowTypeEntity save(ShowTypeEntity showType) {
		return showTypeRepository.save(showType);
	}

	@Override
	public ShowTypeDto convertToDto(ShowTypeEntity entity) {
		return modelMapper.map(entity, ShowTypeDto.class);
	}

	@Override
	public ShowTypeEntity convertToEntity(ShowTypeDto showTypeDto) {
		ShowTypeEntity showType;

		if (StringUtils.isBlank(showTypeDto.getId())) {
			showType = new ShowTypeEntity();
		}
		else {
			Optional<ShowTypeEntity> opt = findById(showTypeDto.getId());

			if (opt.isPresent()) {
				showType = opt.get();
			}
			else {
				return null;
			}
		}

		modelMapper.map(showTypeDto, showType);
		return showType;
	}
}
