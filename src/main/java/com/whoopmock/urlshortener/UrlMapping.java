package com.whoopmock.urlshortener;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity // Tells Spring to create a SQL table for this class
@Table(name = "urls") // Names the table "urls" instead of "UrlMapping"
public class UrlMapping {

    @Id // Marks this as the Primary Key
    private UUID uuid;

    @Column(nullable = false, unique = true) // The short alias must be unique
    private String alias;

    @Column(nullable = false, length = 2048) // URLs can be long, so increase the varchar length
    private String originalUrl;

    @Column(nullable = false)
    private long totalClicks = 0;

    private LocalDateTime createdAt;
    private LocalDateTime lastAccessedAt;

    // Generate Getters and Setters
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public long getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(long totalClicks) {
        this.totalClicks = totalClicks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void setLastAccessedAt(LocalDateTime lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }
}