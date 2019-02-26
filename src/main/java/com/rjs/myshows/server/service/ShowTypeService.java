package com.rjs.myshows.server.service;

import java.lang.reflect.Type;
import java.util.*;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rjs.myshows.domain.dto.ShowTypeDto;
import com.rjs.myshows.server.domain.entity.ShowTypeEntity;
import com.rjs.myshows.server.repository.ShowTypeRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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

	public Optional<ShowTypeEntity> findById(Long id) {
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

	public List<ShowTypeEntity> saveAll(Collection<ShowTypeEntity> showTypes) {
		return showTypeRepository.saveAll(showTypes);
	}

	@Override
	public ShowTypeDto convertToDto(ShowTypeEntity showTypeEntity) {
		return showTypeEntity != null ? modelMapper.map(showTypeEntity, ShowTypeDto.class) : null;
	}

	@Override
	public List<ShowTypeDto> convertToDto(List<ShowTypeEntity> showTypeEntities) {
		if (showTypeEntities == null || showTypeEntities.isEmpty()) {
			return new ArrayList<>();
		}

		Type dtoListType = new TypeToken<List<ShowTypeDto>>() {}.getType();

		return modelMapper.map(showTypeEntities, dtoListType);
	}

	@Override
	public ShowTypeEntity convertToEntity(ShowTypeDto showTypeDto) {
		if (showTypeDto == null) {
			return null;
		}

		ShowTypeEntity showTypeEntity = new ShowTypeEntity();

		if (showTypeDto.getId() != null) {
			Optional<ShowTypeEntity> opt = findById(showTypeDto.getId());

			if (opt.isPresent()) {
				showTypeEntity = opt.get();
			}
			else {
				logger.warn(String.format("Show type entity with id %d does not exist in datasource.", showTypeDto.getId()));

				return null;
			}
		}

		modelMapper.map(showTypeDto, showTypeEntity);

		return showTypeEntity;
	}

	@Override
	public List<ShowTypeEntity> convertToEntity(List<ShowTypeDto> showTypeDtos) {
		if (showTypeDtos == null || showTypeDtos.isEmpty()) {
			return new ArrayList<>();
		}

		final List<ShowTypeEntity> showTypeEntities = new ArrayList<>();

		showTypeDtos.forEach(dto -> {
			ShowTypeEntity showTypeEntity = convertToEntity(dto);

			if (showTypeEntity != null) {
				showTypeEntities.add(showTypeEntity);
			}
		});

		return showTypeEntities;
	}
}
