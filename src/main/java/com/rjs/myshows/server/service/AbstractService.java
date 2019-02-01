package com.rjs.myshows.server.service;

import org.modelmapper.ModelMapper;

public abstract class AbstractService<E, DTO> {
	protected ModelMapper modelMapper;

	protected AbstractService(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public abstract DTO convertToDto(E entity);
	public abstract E convertToEntity(DTO dto);
}
