package com.urlshortener.requesthandler.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Class to encapsulate a shorten request
 * Includes static serializer / deserializer
 */
public class ShortenRequest {
    public final String url;

    public ShortenRequest(String url) {
        this.url = url;
    }

    public static String toJson(ShortenRequest request) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(request);
    }

    public static ShortenRequest fromJson(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, ShortenRequest.class);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("url", url)
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

        ShortenRequest other = (ShortenRequest) o;
        return new EqualsBuilder()
            .append(this.url, other.url)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 29)
            .append(url)
            .toHashCode();
    }
}
