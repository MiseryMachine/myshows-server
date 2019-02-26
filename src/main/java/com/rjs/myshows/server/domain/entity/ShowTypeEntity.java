package com.rjs.myshows.server.domain.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import com.rjs.myshows.domain.ShowType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Document(collection = "show-type")
@Entity
@Table(name = "show_type")
public class ShowTypeEntity extends JpaBaseEntity implements ShowType {
	@Column(unique = true, nullable = false)
	private String name;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "show_type_genre", joinColumns = @JoinColumn(name = "show_type_id"))
	@Column(name = "genre")
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
