package com.urlshortener.dataaccess.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Encapsulates the data stored in the persistent layer
 */
public class UrlMappingData {

    public final String originalUrl;
    public final String aliasUrl;
    public final long creationTime;

    public UrlMappingData(String originalUrl,
                          String aliasUrl,
                          long creationTime) {
        this.originalUrl = originalUrl;
        this.aliasUrl = aliasUrl;
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("originalUrl", originalUrl)
            .append("aliasUrl", aliasUrl)
            .append("creationTime", creationTime)
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

        UrlMappingData other = (UrlMappingData) o;
        return new EqualsBuilder()
            .append(this.originalUrl, other.originalUrl)
            .append(this.aliasUrl, other.aliasUrl)
            .append(this.creationTime, other.creationTime)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(21, 63)
            .append(originalUrl)
            .append(aliasUrl)
            .append(creationTime)
            .toHashCode();
    }
}
