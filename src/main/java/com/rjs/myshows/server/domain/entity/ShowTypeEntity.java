package com.rjs.myshows.server.domain.entity;

import java.util.HashSet;
import java.util.Set;

import com.rjs.myshows.domain.ShowType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowTypeEntity extends BaseEntity implements ShowType {
	private String name;
	private Set<String> genres = new HashSet<>();

	public ShowTypeEntity() {
	}

	@Override
	public void addGenre(String genre) {
		genres.add(genre);
	}

	@Override
	public void removeGenre(String genre) {
		genres.remove(genre);
	}

	@Override
	public boolean hasGenre(String genre) {
		return genres.contains(genre);
	}
}
