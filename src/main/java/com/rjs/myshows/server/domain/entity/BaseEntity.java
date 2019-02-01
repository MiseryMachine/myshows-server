package com.rjs.myshows.server.domain.entity;

import org.springframework.data.annotation.Id;

import com.rjs.myshows.domain.BaseElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseEntity implements BaseElement {
	@Id
	protected String id;

	protected BaseEntity() {
	}
}
