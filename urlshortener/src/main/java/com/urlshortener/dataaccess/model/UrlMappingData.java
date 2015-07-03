package com.urlshortener.dataaccess.model;


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
}
