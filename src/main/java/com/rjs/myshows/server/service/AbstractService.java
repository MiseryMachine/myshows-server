package com.rjs.myshows.server.service;

import java.util.List;

import org.modelmapper.ModelMapper;

public abstract class AbstractService<E, DTO> {
	protected ModelMapper modelMapper;

	protected AbstractService(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public abstract DTO convertToDto(E entity);
	public abstract List<DTO> convertToDto(List<E> entities);

	public abstract E convertToEntity(DTO dto);
	public abstract List<E> convertToEntity(List<DTO> dtos);
}
