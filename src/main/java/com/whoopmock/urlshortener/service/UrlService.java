package com.whoopmock.urlshortener.service;

import com.whoopmock.urlshortener.dto.*;
import com.whoopmock.urlshortener.UrlMapping;
import com.whoopmock.urlshortener.UrlRepository;
import com.whoopmock.urlshortener.exceptions.*;

import java.security.SecureRandom;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UrlService {
    public final String BASE_URL = "https://localhost:8080/api/v1/urls/";
    public final int RETRY_COUNT = 3;
    private final UrlRepository urlRepository;

    @Autowired
    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public String generateAlias() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();

        StringBuilder alias = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            alias.append(characters.charAt(index));
        }

        return alias.toString();
    }

    public UrlMapping checkIfAliasInDb(String alias) {
        Optional<UrlMapping> optionalUrl = this.urlRepository.findByAlias(alias);
        if (optionalUrl.isPresent()) {
            UrlMapping urlMapping = optionalUrl.get();
            return urlMapping;
        }

        return null;
    }

    public CreateUrlResponse createShortenedUrl(String originalUrl) {
        for (int i = 0; i < RETRY_COUNT; i++) {
            String alias = generateAlias();
            UrlMapping check = checkIfAliasInDb(alias);

            if (check == null) {
                CreateUrlResponse createUrl = new CreateUrlResponse();
                LocalDateTime timestamp = LocalDateTime.now();

                // insert url into db
                UrlMapping newUrl = new UrlMapping();
                newUrl.setUuid(UUID.randomUUID());
                newUrl.setAlias(alias);
                newUrl.setOriginalUrl(originalUrl);
                newUrl.setCreatedAt(timestamp);
                this.urlRepository.save(newUrl);

                createUrl.setShortUrl(BASE_URL + alias);
                createUrl.setOriginalUrl(originalUrl);
                createUrl.setTimestamp(timestamp);

                return createUrl;
            }
        }

        throw new FailedToCreateUrlException("Failed to create alias for " + originalUrl);
    }

    public GetUrlResponse getOriginalUrl(String alias) {
        UrlMapping urlMapping = checkIfAliasInDb(alias);

        if (urlMapping == null) {
            throw new UrlNotFoundException("Could not find url for alias: " + alias);
        }

        GetUrlResponse urlResponse = new GetUrlResponse();
        urlResponse.setOriginalUrl(urlMapping.getOriginalUrl());

        // handle db processing - potensh second thread?
        urlMapping.setLastAccessedAt(LocalDateTime.now());
        urlMapping.setTotalClicks(urlMapping.getTotalClicks() + 1);
        this.urlRepository.save(urlMapping);

        return urlResponse;
    }

    public GetUrlStats getUrlStats(String alias) {
        UrlMapping urlMapping = checkIfAliasInDb(alias);

        if (urlMapping == null) {
            throw new UrlNotFoundException("Could not find url to get stats from alias: " + alias);
        }

        GetUrlStats urlStats = new GetUrlStats();
        urlStats.setShortUrl(BASE_URL + urlMapping.getAlias());
        urlStats.setOriginalUrl(urlMapping.getOriginalUrl());
        urlStats.setTotalClicks(urlMapping.getTotalClicks());
        urlStats.setCreatedAt(urlMapping.getCreatedAt());
        urlStats.setLastAccessedAt(urlMapping.getLastAccessedAt());

        return urlStats;
    }

    @Transactional
    public DeleteUrlResponse deleteOriginalUrl(String alias) {
        UrlMapping urlMapping = checkIfAliasInDb(alias);

        if (urlMapping == null) {
            throw new UrlNotFoundException("Could not find url to delete from alias: " + alias);
        }

        DeleteUrlResponse deleteUrlResponse = new DeleteUrlResponse();
        deleteUrlResponse.setOriginalUrl(urlMapping.getOriginalUrl());
        deleteUrlResponse.setTimestamp(LocalDateTime.now());

        // handle db processing
        this.urlRepository.deleteByAlias(alias);

        return deleteUrlResponse;
    }
}
