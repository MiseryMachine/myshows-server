package com.rjs.myshows.server.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rjs.myshows.server.domain.entity.ShowTypeEntity;

public interface ShowTypeRepository extends MongoRepository<ShowTypeEntity, String> {
	Optional<ShowTypeEntity> findByName(String name);
}
