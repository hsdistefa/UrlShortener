package com.urlshortener.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Class to encapsulate a shorten request
 * Includes static serializer / deserializer
 */
public class ShortenRequest {
    private final String url;

    public ShortenRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public static String toJson(ShortenRequest request) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(request);
    }

    public static ShortenRequest fromJson(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, ShortenRequest.class);
    }
}
