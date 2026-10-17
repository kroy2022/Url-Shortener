package com.whoopmock.urlshortener;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.whoopmock.urlshortener.controller.UrlController;
import com.whoopmock.urlshortener.service.UrlService;
import com.whoopmock.urlshortener.dto.CreateUrlRequest;
import com.whoopmock.urlshortener.dto.CreateUrlResponse;
import com.whoopmock.urlshortener.dto.DeleteUrlResponse;
import com.whoopmock.urlshortener.dto.GetUrlResponse;
import com.whoopmock.urlshortener.dto.GetUrlStats;
import com.whoopmock.urlshortener.exceptions.UrlNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

@SpringBootTest
class UrlshortenerControllerTests {

	/* Objects */

	@Autowired
	private UrlController urlController;

	@Autowired
	private UrlService urlService;

	public Random rand = new Random();

	/* Variables */

	public final String BASE_URL = "https://localhost:8080/api/v1/urls/";

	private final String[] ORIGINAL_URLS = {
			"https://en.wikipedia.org/wiki/History_of_artificial_intelligence",
			"https://www.nationalgeographic.com/science/article/how-does-climate-change-affect-the-environment",
			"https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/CompletableFuture.html",
			"https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise/allSettled",
			"https://www.example.com/products/electronics/smartphones/latest-models?category=technology&sort=price&order=ascending&page=25&filter=available"
	};

	/* Helper Functions */

	public CreateUrlResponse createUrl(String originalUrl) {
		CreateUrlRequest request = new CreateUrlRequest();
		request.setOriginalUrl(originalUrl);

		return urlController.createShortUrlController(request);
	}

	public GetUrlResponse getUrl(String alias) {
		return urlController.getUrlController(alias);
	}

	/* Tests */

	@Test
	void testCreateAndGetShortUrl() {
		System.out.println("--------------\ntestCreateAndGetShortUrl\n");
		for (int i = 0; i < ORIGINAL_URLS.length; i++) {
			// Create the url
			CreateUrlResponse createResponse = createUrl(ORIGINAL_URLS[i]);
			assertNotNull(createResponse);

			// Get the alias from the response
			String alias = createResponse.getShortUrl().split(BASE_URL)[1];
			assertNotNull(alias);

			// Get the original url with alias
			GetUrlResponse getResponse = getUrl(alias);
			assertNotNull(getResponse);
			System.out
					.println(i + ") Expected Url: " + ORIGINAL_URLS[i] + "Actual Url: " + getResponse.getOriginalUrl());
			assertEquals(ORIGINAL_URLS[i], getResponse.getOriginalUrl(), "Verify original url");
		}
	}

	@Test
	void testCreateAndGetStatsForUrl() {
		System.out.println("--------------\ntestCreateAndGetStatsForUrl\n");
		String originalUrl = ORIGINAL_URLS[0];

		// Create the url
		CreateUrlResponse createResponse = createUrl(originalUrl);
		assertNotNull(createResponse);

		String alias = createResponse.getShortUrl().split(BASE_URL)[1];

		// Get url 10 times to increment stats
		int expectedClicks = rand.nextInt(50);
		for (int i = 0; i < expectedClicks; i++) {
			getUrl(alias);
		}

		// Validate stats
		GetUrlStats urlStats = urlController.getUrlStatsController(alias);
		assertNotNull(urlStats);
		System.out.println("Expected Clicks: " + expectedClicks + " - Actual Clicks: " + urlStats.getTotalClicks());
		assertEquals(expectedClicks, urlStats.getTotalClicks(), "Verify url total clicks");
	}

	@Test
	void testCreateAndDeleteUrl() {
		System.out.println("--------------\ntestCreateAndDeleteUrl\n");
		String originalUrl = ORIGINAL_URLS[0];

		// Create the url
		CreateUrlResponse createResponse = createUrl(originalUrl);
		assertNotNull(createResponse);

		String alias = createResponse.getShortUrl().split(BASE_URL)[1];

		// Delete the url
		DeleteUrlResponse deleteResponse = urlController.deleteUrlController(alias);
		assertNotNull(deleteResponse);

		// Get the url (expect exception)
		assertThrows(
			UrlNotFoundException.class,
			() -> urlService.getOriginalUrl(alias)
		);
	}
}