package com.urlshortener.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


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
}
