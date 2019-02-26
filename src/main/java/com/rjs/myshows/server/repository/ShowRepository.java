package com.rjs.myshows.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.rjs.myshows.server.domain.entity.ShowEntity;

public interface ShowRepository extends JpaRepository<ShowEntity, Long>, JpaSpecificationExecutor<ShowEntity> {
	Optional<ShowEntity> findByMdbId(String mdbId);
	Optional<ShowEntity> findByImdbId(String imdbId);

	List<ShowEntity> findByTitleLike(String title);
	List<ShowEntity> findByTitleLike(String title, Sort sort);
	List<ShowEntity> findByStarRating(int starRating);
	List<ShowEntity> findByStarRating(int starRating, Sort sort);
}
