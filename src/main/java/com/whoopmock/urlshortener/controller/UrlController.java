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

    @PostMapping
    public UrlMapping createShortUrl(@RequestBody CreateUrlRequest postBody) {
        return this.urlService.createShortenedUrl(postBody.getOriginalUrl());
    }

    @GetMapping("/{alias}")
    public GetUrlResponse getUrl(@PathVariable String alias) {
        return this.urlService.getOriginalUrl(alias);
    }

    @PutMapping("/{alias}")
    public String updateUrl(@PathVariable String alias) {
        return "PUT hit: " + alias;
    }

    @DeleteMapping("/{alias}")
    public DeleteUrlResponse deleteUrl(@PathVariable String alias) {
        return this.urlService.deleteOriginalUrl(alias);
    }
}
