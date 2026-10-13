package com.whoopmock.urlshortener.service;

import com.whoopmock.urlshortener.dto.*;
import com.whoopmock.urlshortener.UrlMapping;
import com.whoopmock.urlshortener.UrlRepository;

import java.security.SecureRandom;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Optional<UrlMapping> optionalUrl = urlRepository.findByAlias(alias);
        if (optionalUrl.isPresent()) {
            UrlMapping urlMapping = optionalUrl.get();
            return urlMapping;
        }

        return null;
    }

    public UrlMapping createShortenedUrl(String originalUrl) {
        for (int i = 0; i < RETRY_COUNT; i++) {
            String alias = generateAlias();
            UrlMapping check = checkIfAliasInDb(alias);

            if (check == null) {
                UrlMapping newUrl = new UrlMapping();
                newUrl.setUuid(UUID.randomUUID());
                newUrl.setAlias(alias);
                newUrl.setOriginalUrl(originalUrl);
                newUrl.setCreatedAt(LocalDateTime.now());

                // insert url into db
                this.urlRepository.save(newUrl);

                return newUrl;
            }
        }

        System.out.println("Failed to create alias for " + originalUrl);
        return null;
    }

    public GetUrlResponse getOriginalUrl(String alias) {
        UrlMapping urlMapping = checkIfAliasInDb(alias);
        GetUrlResponse urlResponse = new GetUrlResponse();

        if (urlMapping == null) {
            return null;
        }

        urlResponse.setOriginalUrl(urlMapping.getOriginalUrl());

        // handle db processing - potensh second thread?
        urlMapping.setLastAccessedAt(LocalDateTime.now());
        urlMapping.setTotalClicks(urlMapping.getTotalClicks() + 1);
        this.urlRepository.save(urlMapping);

        return urlResponse;
    }
}
