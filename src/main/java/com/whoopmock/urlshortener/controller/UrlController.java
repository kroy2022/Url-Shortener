package com.whoopmock.urlshortener.controller;

import org.springframework.web.bind.annotation.*;

import com.whoopmock.urlshortener.UrlMapping;
import com.whoopmock.urlshortener.service.UrlService;
import com.whoopmock.urlshortener.dto.*;
/*
urlshortener> .\mvnw.cmd spring-boot:run
*/

@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    // change to not be urlmapping ??
    @PostMapping
    public CreateUrlResponse createShortUrlController(@RequestBody CreateUrlRequest postBody) {
        return this.urlService.createShortenedUrl(postBody.getOriginalUrl());
    }

    @GetMapping("/{alias}")
    public GetUrlResponse getUrlController(@PathVariable String alias) {
        return this.urlService.getOriginalUrl(alias);
    }

    @GetMapping("/{alias}/stats")
    public GetUrlStats getUrlStatsController(@PathVariable String alias) {
        return this.urlService.getUrlStats(alias);
    }

    @DeleteMapping("/{alias}")
    public DeleteUrlResponse deleteUrlController(@PathVariable String alias) {
        return this.urlService.deleteOriginalUrl(alias);
    }
}
