package com.rjs.myshows.server.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.rjs.myshows.server.domain.entity.JpaBaseEntity;

public class TextLikeSpecification<E extends JpaBaseEntity> implements Specification<E> {
	private final String key;
	private final String text;

	public TextLikeSpecification(String key, String text) {
		this.key = key;
		this.text = text;
	}

	@Override
	public Predicate toPredicate(Root<E> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
		return criteriaBuilder.like(root.get(key), "%" + text + "%");
	}
}
