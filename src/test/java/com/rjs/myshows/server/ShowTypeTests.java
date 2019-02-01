package com.rjs.myshows.server;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.rjs.myshows.domain.dto.ShowTypeDto;
import com.rjs.myshows.server.config.MyShowsConfig;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MyShowsConfig.class}, loader = AnnotationConfigContextLoader.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "server.port=5000")
public class ShowTypeTests {
	@Autowired
	private TestRestTemplate restTemplate;
//	@LocalServerPort
//	private String port;

	@Test
	public void addShowType() {
//		String url = String.format("http://localhost:%s/myshows-server/show-type", port);
		String url = "http://localhost:5000/show-type";
		ShowTypeDto showType = new ShowTypeDto();
		showType.setName("Movie");
		showType.addGenre("Action");
		showType.addGenre("Adventure");
		showType.addGenre("Drama");
		showType.addGenre("Romance");

//		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ShowTypeDto> response = restTemplate.exchange(url,
			HttpMethod.POST, new HttpEntity<>(showType, createHeaders()), ShowTypeDto.class);
		assertEquals("Request success", 200, response.getStatusCodeValue());
	}

	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();

		String auth = "restUser:1971Duster";
		byte[] encoded = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
		String authHeader = "Basic " + new String(encoded);
		headers.add("Authorization", authHeader);

		return headers;
	}
}
