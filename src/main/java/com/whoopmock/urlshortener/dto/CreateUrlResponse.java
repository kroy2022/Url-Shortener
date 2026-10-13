package com.whoopmock.urlshortener.dto;

import java.time.LocalDateTime;

public class CreateUrlResponse {
    private String shortUrl;
    private String originalUrl;
    private LocalDateTime timestamp;

    public String getShortUrl() {
        return this.shortUrl;
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
