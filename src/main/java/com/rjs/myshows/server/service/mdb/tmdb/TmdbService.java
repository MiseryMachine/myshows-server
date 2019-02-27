package com.rjs.myshows.server.service.mdb.tmdb;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rjs.myshows.domain.dto.ShowDto;
import com.rjs.myshows.server.config.AppProperties;
import com.rjs.myshows.server.domain.entity.ShowEntity;
import com.rjs.myshows.server.domain.entity.ShowTypeEntity;
import com.rjs.myshows.server.service.ShowService;
import com.rjs.myshows.server.service.ShowTypeService;
import com.rjs.myshows.server.service.mdb.MdbService;
import com.rjs.myshows.server.service.mdb.tmdb.domain.TmdbGenre;
import com.rjs.myshows.server.service.mdb.tmdb.domain.TmdbShow;
import com.rjs.myshows.server.service.mdb.tmdb.domain.TmdbShowDetail;
import com.rjs.myshows.server.service.mdb.tmdb.domain.TmdbShowListing;
import com.rjs.myshows.server.util.UrlBuilder;
import com.rjs.myshows.util.ImageUtil;
import com.rjs.util.web.HttpUtil;
import com.rjs.util.web.RestClientService;
import com.rjs.util.web.WebServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service("mdbService")
@Profile("tmdb")
public class TmdbService extends MdbService<TmdbShow> {
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
	public List<TmdbShow> searchShows(String showTypeName, String title) {
		List<TmdbShow> mdbShows = new ArrayList<>();
		String[] urlParams = {
			String.format("api_key=%s", tmdbProperties.getKey()),
			String.format("query=%s", title),
			String.format("language=%s", tmdbProperties.getLocale())
		};

		String url = UrlBuilder.create(tmdbProperties.getUrl())
			.addPath(tmdbProperties.getSearchPath())
			.addPath(tmdbProperties.getShowTypePath(showTypeName))
			.addPath("?")
			.addPath(String.join("&", urlParams))
			.getUrl();

		try {
			Optional<TmdbShowListing> showListing = consumeService(url, HttpMethod.GET);

			if (showListing.isPresent()) {
				mdbShows.addAll(showListing.get().results);
				mdbShows.forEach(s -> {
					s.posterPath = UrlBuilder.create(tmdbProperties.getImageUrl())
						.addPath(tmdbProperties.getImageThumbPath())
						.addPath(s.posterPath).getUrl();

					if (StringUtils.isBlank(s.releaseDate)) {
						s.releaseDate = "Unknown";
					}
				});
			}
		}
		catch (WebServiceException e) {
			logger.error("Error looking up shows.", e);
		}

		return mdbShows;
	}

	@Override
	public Optional<ShowDto> addShow(String mdbId, String showTypeName) {
		Optional<ShowEntity> showOpt = showService.findByMdbId(mdbId);

		if (showOpt.isPresent()) {
			return Optional.of(showService.convertToDto(showOpt.get()));
		}

		ShowDto show = null;
		String[] urlParams = {
			String.format("api_key=%s", tmdbProperties.getKey()),
			String.format("language=%s", tmdbProperties.getLocale())
		};

		String url = UrlBuilder.create(tmdbProperties.getUrl())
			.addPath(tmdbProperties.getSearchPath())
			.addPath(tmdbProperties.getShowTypePath(showTypeName))
			.addPath("/" + mdbId)
			.addPath("?")
			.addPath(String.join("&", urlParams))
			.getUrl();

		try {
			Optional<TmdbShowDetail> tmdbShowOpt = consumeService(url, HttpMethod.GET);

			if (tmdbShowOpt.isPresent() && tmdbShowOpt.get().id > -1) {
				TmdbShowDetail tmdbShow = tmdbShowOpt.get();
				Set<String> genres = convertGenres(tmdbShow.genres, showTypeName);
				ShowEntity showEntity = new ShowEntity();
				showEntity.setMdbId(String.valueOf(tmdbShow.id));
				showEntity.setImdbId(tmdbShow.imdbId);
				showEntity.setTitle(tmdbShow.title);
				showEntity.setTagLine(tmdbShow.tagline);
				showEntity.setDescription(tmdbShow.overview);
				showEntity.setRuntime(tmdbShow.runtime);
				showEntity.setShowType(showTypeName);
				showEntity.setGenres(genres);

				if (StringUtils.isNotBlank(tmdbShow.releaseDate)) {
					showEntity.setReleaseDate(LocalDate.parse(tmdbShow.releaseDate, dateFormat));
				}

				showEntity = showService.save(showEntity);

				downloadPosterImage(showEntity, tmdbShow.posterPath);

				show = showService.convertToDto(showEntity);
			}
		}
		catch (Exception e) {
			logger.error(String.format("Error adding show: %s.", mdbId), e);
		}

		return Optional.ofNullable(show);
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
			LinkedHashMap<String, Object> rootMap = consumeService(url, HttpMethod.GET);
			String genreJson = jsonObjectMapper.writeValueAsString(rootMap.get("genres"));
			List<TmdbGenre> mdbGenres = jsonObjectMapper.readValue(genreJson, new TypeReference<ArrayList<TmdbGenre>>(){});
			return mdbGenres.stream().map(g -> g.name).collect(Collectors.toSet());
		}
		catch (Exception e) {
			logger.error("Error retrieving genres.", e);
		}

		return new HashSet<>();
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

	private <E> E consumeService(String url, HttpMethod method) throws WebServiceException {
		HttpEntity<String> httpEntity = HttpUtil.createHttpEntity(null, "", MediaType.APPLICATION_JSON_VALUE);
		ResponseEntity<E> responseEntity = restClient.exchange(httpEntity, url, method,
			new ParameterizedTypeReference<E>(){}, new HashMap<>());

		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			throw new WebServiceException(
				String.format("Response status: %s", responseEntity.getStatusCode().getReasonPhrase()),
				responseEntity.getStatusCode());
		}

		return responseEntity.getBody();
	}
}
