package com.whoopmock.urlshortener.dto;

public class CreateUrlRequest {
    private String originalUrl;

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
}
