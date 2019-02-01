package com.rjs.myshows.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rjs.myshows.server.domain.entity.ShowEntity;

public interface ShowRepository extends MongoRepository<ShowEntity, String> {
	Optional<ShowEntity> findByMdbId(String mdbId);
	Optional<ShowEntity> findByImdbId(String imdbId);

	List<ShowEntity> findByTitleLike(String title);
	List<ShowEntity> findByStarRating(int starRating);
}
