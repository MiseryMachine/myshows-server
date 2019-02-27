package com.rjs.myshows.server.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;

import org.springframework.data.jpa.domain.Specification;

public class ValueInSpecification<E, V> implements Specification<E> {
	private final String key;
	private final Collection<V> values;

	public ValueInSpecification(String key, Collection<V> values) {
		this.key = key;
		this.values = values;
	}

	@Override
	public Predicate toPredicate(Root<E> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//		return criteriaBuilder.in(root.get(key)).in(values);
		return root.get(key).in(values);
	}
}
