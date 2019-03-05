package com.rjs.myshows.server.service.mdb.tmdb;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rjs.myshows.domain.dto.ShowDto;
import com.rjs.myshows.server.config.AppProperties;
import com.rjs.myshows.server.domain.entity.ShowEntity;
import com.rjs.myshows.server.domain.entity.ShowTypeEntity;
import com.rjs.myshows.server.service.ShowService;
import com.rjs.myshows.server.service.ShowTypeService;
import com.rjs.myshows.server.service.mdb.MdbService;
import com.rjs.myshows.server.service.mdb.ShowSummary;
import com.rjs.myshows.server.service.mdb.tmdb.domain.TmdbGenre;
import com.rjs.myshows.server.service.mdb.tmdb.domain.movie.*;
import com.rjs.myshows.server.service.mdb.tmdb.domain.tv.ContentRating;
import com.rjs.myshows.server.service.mdb.tmdb.domain.tv.TvDetail;
import com.rjs.myshows.server.service.mdb.tmdb.domain.tv.TvListing;
import com.rjs.myshows.server.util.UrlBuilder;
import com.rjs.myshows.util.ImageUtil;
import com.rjs.util.web.HttpUtil;
import com.rjs.util.web.RestClientService;
import com.rjs.util.web.WebServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.rjs.myshows.domain.DomainConstants.MOVIE_TYPE;
import static com.rjs.myshows.domain.DomainConstants.TV_TYPE;

@Service("mdbService")
@Profile("tmdb")
public class TmdbService extends MdbService {
	private final Logger logger = LoggerFactory.getLogger(TmdbService.class);
	private final RestClientService restClient = new RestClientService(new RestTemplate());

	private TmdbProperties tmdbProperties;
	private ObjectMapper jsonObjectMapper;
	private ShowService showService;
	private ShowTypeService showTypeService;
	private DateTimeFormatter dateFormat;

	public TmdbService(AppProperties appProperties, ImageUtil imageUtil, TmdbProperties tmdbProperties, ObjectMapper jsonObjectMapper,
					   ShowService showService, ShowTypeService showTypeService, DateTimeFormatter dateFormat) {
		super(appProperties, imageUtil);

		this.tmdbProperties = tmdbProperties;
		this.jsonObjectMapper = jsonObjectMapper;
		this.showService = showService;
		this.showTypeService = showTypeService;
		this.dateFormat = dateFormat;
	}

	@Override
	public List<ShowSummary> searchShows(String showTypeName, String title) {
		switch (showTypeName) {
			case MOVIE_TYPE:
				return searchMovies(title);

			case TV_TYPE:
				return searchTv(title);
		}

		return new ArrayList<>();
	}

	@Override
	public Optional<ShowDto> addShow(String tmdbId, String showTypeName) {
		switch (showTypeName) {
			case MOVIE_TYPE:
				return Optional.ofNullable(addMovie(tmdbId));

			case TV_TYPE:
				return Optional.ofNullable(addTv(tmdbId));
		}

		return Optional.empty();
	}

	@Override
	public Set<String> getGenres(String showTypeName) {
		String serviceUrl = UrlBuilder.create(tmdbProperties.getUrl())
			.addPath(tmdbProperties.getGenrePath())
			.addPath(tmdbProperties.getShowTypePath(showTypeName))
			.addPath(tmdbProperties.getListPath())
			.getUrl();
		String[] urlParams = {
			String.format("api_key=%s", tmdbProperties.getKey()),
			String.format("language=%s", tmdbProperties.getLocale())
		};
		String url = String.format("%s?%s", serviceUrl, String.join("&", urlParams));

		try {
			GenreListing rootMap = consumeService(url, HttpMethod.GET, GenreListing.class);
//			String genreJson = jsonObjectMapper.writeValueAsString(rootMap.get("genres"));
//			List<TmdbGenre> mdbGenres = jsonObjectMapper.readValue(genreJson, new TypeReference<ArrayList<TmdbGenre>>(){});

			if (rootMap != null && rootMap.genres != null) {
				return rootMap.genres.stream().map(g -> g.name).collect(Collectors.toSet());
			}
		}
		catch (Exception e) {
			logger.error("Error retrieving genres.", e);
		}

		return new HashSet<>();
	}

	private List<ShowSummary> searchMovies(String title) {
		String[] urlParams = {
			String.format("api_key=%s", tmdbProperties.getKey()),
			String.format("language=%s", tmdbProperties.getLocale()),
			String.format("query=%s", title)
		};

		String url = UrlBuilder.create(tmdbProperties.getUrl())
			.addPath(tmdbProperties.getSearchPath())
			.addPath(tmdbProperties.getShowTypePath(MOVIE_TYPE))
			.addPath("?")
			.addPath(String.join("&", urlParams))
			.getUrl();

		List<ShowSummary> showSummaryList = new ArrayList<>();

		try {
			MovieListing movieListing = consumeService(url, HttpMethod.GET, MovieListing.class);

			if (movieListing != null) {
				movieListing.results.forEach(m -> {
					ShowSummary showSummary = new ShowSummary();
					showSummary.id = String.valueOf(m.id);
					showSummary.showType = MOVIE_TYPE;
					showSummary.title = m.title;
					showSummary.overview = m.overview;
					showSummary.releaseDate = m.releaseDate;
					showSummary.posterPath = UrlBuilder.create(tmdbProperties.getImageUrl())
						.addPath(tmdbProperties.getImageThumbPath())
						.addPath(m.posterPath).getUrl();

					if (StringUtils.isBlank(showSummary.releaseDate)) {
						showSummary.releaseDate = "Unknown";
					}

					showSummaryList.add(showSummary);
				});
			}
		}
		catch (WebServiceException e) {
			logger.error("Error looking up movies.", e);
		}

		return showSummaryList;
	}

	private ShowDto addMovie(String tmdbId) {
		Optional<ShowEntity> showOpt = showService.findByMdbId(tmdbId);

		if (showOpt.isPresent()) {
			return showService.convertToDto(showOpt.get());
		}

		ShowDto show = null;
		String[] urlParams = {
			String.format("api_key=%s", tmdbProperties.getKey()),
			String.format("language=%s", tmdbProperties.getLocale()),
			"append_to_response=release_dates"
		};

		String url = UrlBuilder.create(tmdbProperties.getUrl())
//			.addPath(tmdbProperties.getSearchPath())
			.addPath(tmdbProperties.getShowTypePath(MOVIE_TYPE))
			.addPath("/" + tmdbId)
			.addPath("?")
			.addPath(String.join("&", urlParams))
			.getUrl();

		try {
			MovieDetail movieDetail = consumeService(url, HttpMethod.GET, MovieDetail.class);

			if (movieDetail != null && movieDetail.id > -1) {
				Set<String> genres = convertGenres(movieDetail.genres, MOVIE_TYPE);
				ShowEntity showEntity = new ShowEntity();
				showEntity.setMediaFormat("BLU-RAY");
				showEntity.setMdbId(String.valueOf(movieDetail.id));
				showEntity.setImdbId(movieDetail.imdbId);
				showEntity.setTitle(movieDetail.title);
				showEntity.setTagLine(movieDetail.tagline);
				showEntity.setDescription(movieDetail.overview);
				showEntity.setRuntime(movieDetail.runtime);
				showEntity.setShowType(MOVIE_TYPE);
				showEntity.setGenres(genres);

				if (StringUtils.isNotBlank(movieDetail.releaseDate)) {
					showEntity.setReleaseDate(LocalDate.parse(movieDetail.releaseDate, dateFormat));
					showEntity.setReleaseDateText(movieDetail.releaseDate);
				}
				else {
					showEntity.setReleaseDateText("N/A");
				}

				// A little convoluted to get the movie rating.
				if (movieDetail.releaseDates != null && movieDetail.releaseDates.results != null && !movieDetail.releaseDates.results.isEmpty()) {
					List<MovieRelease> releases = movieDetail.releaseDates.results;

					for (MovieRelease release : releases) {
						if ("US".equals(release.isoName)) {
							MovieReleaseDate releaseDate = release.releases.stream().filter(r -> r.type == 3).findFirst().orElse(null);
							showEntity.setShowRating(releaseDate != null ? releaseDate.rating : "N/A");

//							break;
						}
					}
				}

				showEntity = showService.save(showEntity);

				downloadPosterImage(showEntity, movieDetail.posterPath);

				show = showService.convertToDto(showEntity);
			}
		}
		catch (Exception e) {
			logger.error(String.format("Error adding movie: %s.", tmdbId), e);
		}

		return show;
	}

	private List<ShowSummary> searchTv(String title) {
		String[] urlParams = {
			String.format("api_key=%s", tmdbProperties.getKey()),
			String.format("language=%s", tmdbProperties.getLocale()),
			String.format("query=%s", title)
		};

		String url = UrlBuilder.create(tmdbProperties.getUrl())
			.addPath(tmdbProperties.getSearchPath())
			.addPath(tmdbProperties.getShowTypePath(TV_TYPE))
			.addPath("?")
			.addPath(String.join("&", urlParams))
			.getUrl();

		List<ShowSummary> showSummaryList = new ArrayList<>();

		try {
			TvListing showListing = consumeService(url, HttpMethod.GET, TvListing.class);

			if (showListing != null && showListing.results != null) {
				showListing.results.forEach(s -> {
					ShowSummary showSummary = new ShowSummary();
					showSummary.id = String.valueOf(s.id);
					showSummary.showType = TV_TYPE;
					showSummary.title = s.title;
					showSummary.overview = s.overview;
					showSummary.releaseDate = s.airDate;
					showSummary.posterPath = UrlBuilder.create(tmdbProperties.getImageUrl())
						.addPath(tmdbProperties.getImageThumbPath())
						.addPath(s.posterPath).getUrl();

					if (StringUtils.isBlank(showSummary.releaseDate)) {
						showSummary.releaseDate = "Unknown";
					}

					showSummaryList.add(showSummary);
				});
			}
		}
		catch (WebServiceException e) {
			logger.error("Error looking up TV shows.", e);
		}

		return showSummaryList;
	}

	private ShowDto addTv(String tmdbId) {
		Optional<ShowEntity> showOpt = showService.findByMdbId(tmdbId);

		if (showOpt.isPresent()) {
			return showService.convertToDto(showOpt.get());
		}

		ShowDto show = null;
		String[] urlParams = {
			String.format("api_key=%s", tmdbProperties.getKey()),
			String.format("language=%s", tmdbProperties.getLocale()),
			"append_to_response=content_ratings"
		};

		String url = UrlBuilder.create(tmdbProperties.getUrl())
			.addPath(tmdbProperties.getSearchPath())
			.addPath(tmdbProperties.getShowTypePath(TV_TYPE))
			.addPath("/" + tmdbId)
			.addPath("?")
			.addPath(String.join("&", urlParams))
			.getUrl();

		try {
			TvDetail tvDetail = consumeService(url, HttpMethod.GET, TvDetail.class);

			if (tvDetail != null && tvDetail.id > -1) {
				Set<String> genres = convertGenres(tvDetail.genres, TV_TYPE);
				ShowEntity showEntity = new ShowEntity();
				showEntity.setMdbId(String.valueOf(tvDetail.id));
				showEntity.setImdbId(null);
				showEntity.setTitle(tvDetail.title);
				showEntity.setTagLine(null);
				showEntity.setDescription(tvDetail.overview);
				showEntity.setRuntime(0);
				showEntity.setShowType(TV_TYPE);
				showEntity.setGenres(genres);

				if (StringUtils.isNotBlank(tvDetail.airDate)) {
					showEntity.setReleaseDate(LocalDate.parse(tvDetail.airDate, dateFormat));
				}

				// A little convoluted to get the TV rating.
				if (tvDetail.contentRatingResults != null && tvDetail.contentRatingResults.results != null && !tvDetail.contentRatingResults.results.isEmpty()) {
					List<ContentRating> contentRatings = tvDetail.contentRatingResults.results;

					for (ContentRating contentRating : contentRatings) {
						if ("US".equals(contentRating.isoName)) {
							showEntity.setShowRating(contentRating.rating != null ? contentRating.rating : "N/A");

							break;
						}
					}
				}

				showEntity = showService.save(showEntity);

				downloadPosterImage(showEntity, tvDetail.posterPath);

				show = showService.convertToDto(showEntity);
			}
		}
		catch (Exception e) {
			logger.error(String.format("Error adding TV show: %s.", tmdbId), e);
		}

		return show;
	}

	private void downloadPosterImage(ShowEntity show, String tmdbPosterPath) {
		if (show.getId() == null) {
			throw new IllegalStateException("Show must be saved prior to adding poster image.");
		}

		String localImgPath = getLocalImagePath(String.valueOf(show.getId()));

		try {
			URL posterUrl = new URL(tmdbProperties.getImageUrl() + tmdbProperties.getImageNormalPath() + tmdbPosterPath);
			imageUtil.saveImage(posterUrl, localImgPath, "poster");
		}
		catch (MalformedURLException e) {
			logger.error("Unable to download show poster.", e);
		}

		try {
			URL posterUrl = new URL(tmdbProperties.getImageUrl() + tmdbProperties.getImageThumbPath() + tmdbPosterPath);
			imageUtil.saveImage(posterUrl, localImgPath, "poster_thumb");
		}
		catch (MalformedURLException e) {
			logger.error("Unable to download show thumbnail poster.", e);
		}
	}

	private Set<String> convertGenres(Collection<TmdbGenre> mdbGenres, String showTypeName) {
		if (mdbGenres == null || mdbGenres.isEmpty()) {
			return new LinkedHashSet<>();
		}

		ShowTypeEntity showType = showTypeService.findByName(showTypeName).orElse(new ShowTypeEntity());

		if (showType.getId() == null) {
			// initialize new show type
			showType.setName(showTypeName);
//			showType = showTypeService.save(showType);
		}

		Set<String> curGenres = showType.getGenres();
		int initialSize = curGenres.size();
		Set<String> genres = mdbGenres.stream().map(g -> g.name).collect(Collectors.toSet());
		curGenres.addAll(genres);

		if (initialSize != curGenres.size()) {
			showTypeService.save(showType);
		}

		return genres;
	}

	private <E> E consumeService(String url, HttpMethod method, Class<E> responseClass) throws WebServiceException {
		HttpEntity<String> httpEntity = HttpUtil.createHttpEntity(null, "", MediaType.APPLICATION_JSON_VALUE);
//		ResponseEntity<E> responseEntity = restClient.exchange(httpEntity, url, method,
//			new ParameterizedTypeReference<E>(){}, new HashMap<>());
		ResponseEntity<E> responseEntity = restClient.exchange(httpEntity, url, method,
			responseClass, new HashMap<>());

		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			throw new WebServiceException(
				String.format("Response status: %s", responseEntity.getStatusCode().getReasonPhrase()),
				responseEntity.getStatusCode());
		}

		return responseEntity.getBody();
	}
}
