package com.urlshortener.requesthandler.model;

import com.urlshortener.dataaccess.model.UrlMappingData;

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
    public final String originalUrl;
    public final String aliasUrl;

    public ShortenResponse(UrlMappingData urlMappingData) {
        this.originalUrl = urlMappingData.originalUrl;
        this.aliasUrl = urlMappingData.aliasUrl;
    }

    /**
     * For unit testing
     */
    public ShortenResponse(String originalUrl, String aliasUrl) {
        this.originalUrl = originalUrl;
        this.aliasUrl = aliasUrl;
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
            .append("aliasUrl", aliasUrl)
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
            .append(this.aliasUrl, other.aliasUrl)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 41)
            .append(originalUrl)
            .append(aliasUrl)
            .toHashCode();
    }
}
