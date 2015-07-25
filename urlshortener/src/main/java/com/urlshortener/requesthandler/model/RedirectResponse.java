package com.urlshortener.requesthandler.model;

import com.urlshortener.dataaccess.model.UrlMappingData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Class to encapsulate the result of a successful redirect to an aliased URL
 */
public class RedirectResponse {
    public final String originalUrl;

    public RedirectResponse(UrlMappingData urlMappingData) {
        this.originalUrl = urlMappingData.originalUrl;
    }

    /**
     * For unit testing
     */
    public RedirectResponse(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public static String toJson(RedirectResponse response) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(response);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("originalUrl", originalUrl)
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
        RedirectResponse other = (RedirectResponse) o;
        return new EqualsBuilder()
            .append(this.originalUrl, other.originalUrl)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 59)
            .append(originalUrl)
            .toHashCode();
    }
}
