package com.whoopmock.urlshortener.dto;

import java.time.LocalDateTime;

public class DeleteUrlResponse {
    private String originalUrl;
    private LocalDateTime timestamp;

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
