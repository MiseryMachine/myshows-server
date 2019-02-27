package com.rjs.myshows.server.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;

import org.springframework.data.jpa.domain.Specification;

public class IsMemberSpecification<E> implements Specification<E> {
	private final String key;
	private final Collection values;

	public IsMemberSpecification(String key, Collection values) {
		this.key = key;
		this.values = values;
	}

	@Override
	public Predicate toPredicate(Root<E> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
		Predicate[] predicates = new Predicate[values.size()];
		int idx = 0;

		for (Object value : values) {
			predicates[idx++] = criteriaBuilder.isMember(value, root.get(key));
		}

		return criteriaBuilder.and(predicates);
	}
}
