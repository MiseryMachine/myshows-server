package com.rjs.myshows.server.domain.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.rjs.myshows.domain.Show;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Document(collection = "show")
@Entity
@Table(name = "show")
public class ShowEntity extends JpaBaseEntity implements Show {
	@Column(name = "mdb_id", unique = true, nullable = false)
	private String mdbId;
	@Column(name = "imdb_id")
	private String imdbId;
	@Column(name = "title", nullable = false, length = 80)
	private String title;
	@Column(name = "show_rating", nullable = false, length = 10)
	private String showRating;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "show_content", joinColumns = @JoinColumn(name = "show_id"))
	@Column(name = "content")
	private Set<String> contents = new HashSet<>();

	@Column(name = "tag_line")
	private String tagLine;
	@Column(name = "description", length = 2000)
	private String description;
	@Column(name = "release_date")
	private LocalDate releaseDate;
	@Column(name = "release_date_text", length = 40)
	private String releaseDateText;
	@Column(name = "runtime")
	private Integer runtime;
	@Column(name = "show_type", nullable = false)
	private String showType;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "show_genre", joinColumns = @JoinColumn(name = "show_id"))
	@Column(name = "genre")
	private Set<String> genres = new HashSet<>();

	@Column(name = "media_format", nullable = false)
	private String mediaFormat;
	@Column(name = "personal_notes", length = 2000)
	private String personalNotes;
	@Column(name = "star_rating")
	private int starRating = 0;

	public ShowEntity() {
	}
}
