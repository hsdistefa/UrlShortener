package com.urlshortener.dataaccess.database;

import com.urlshortener.config.Config;
import com.urlshortener.dataaccess.model.UrlMappingData;


/**
 * Class that defines data access operations
 */
public abstract class DbClient {

    private final Config config;

    public DbClient(Config config) {
        this.config = config;
    }

    /**
     * Stores an (alias url -> original url) mapping
     * Does not overwrite
     */
    public abstract void createUrlMapping(UrlMappingData data);

    /**
     * Given a original url, retrieve mapping data
     */
    public abstract UrlMappingData getMappingForOriginalUrl(String originalUrl);

    /**
     * Given an alias url, retrieve mapping data
     */
    public abstract UrlMappingData getMappingForAliasUrl(String aliasUrl);
}