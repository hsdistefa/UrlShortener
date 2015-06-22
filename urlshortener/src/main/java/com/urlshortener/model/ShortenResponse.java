package com.urlshortener.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Class to encapsulate the result of a successful shorten request
 * Includes static serializer / deserializer
 */
public class ShortenResponse {
    private final String originalUrl;
    private final String shortenedUrl;

    public ShortenResponse(String originalUrl, String shortenedUrl) {
        this.originalUrl = originalUrl;
        this.shortenedUrl = shortenedUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }

    public static String toJson(ShortenResponse response) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(response);
    }

    public static ShortenResponse fromJson(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, ShortenResponse.class);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("originalUrl", originalUrl)
            .append("shortenedUrl", shortenedUrl)
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }

        ShortenResponse other = (ShortenResponse) o;
        return new EqualsBuilder()
            .append(this.originalUrl, other.originalUrl)
            .append(this.shortenedUrl, other.shortenedUrl)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 41)
            .append(originalUrl)
            .append(shortenedUrl)
            .toHashCode();
    }
}
