package com.urlshortener.dataaccess.database.dynamodb;

import com.urlshortener.config.Config;
import com.urlshortener.dataaccess.database.DbClient;
import com.urlshortener.dataaccess.model.UrlMappingData;


/**
 * DynamoDB implementation for data access operations
 */
public class DynamoDbClient extends DbClient {

    public DynamoDbClient(Config config) {
        super(config);
    }

    /**
     * Stores an (alias url -> original url) mapping
     * Does not overwrite
     */
    @Override
    public void createUrlMapping(UrlMappingData data) {
        // TODO
    }

    /**
     * Given a original url, retrieve mapping data
     */
    @Override
    public UrlMappingData getMappingForOriginalUrl(String originalUrl) {
        // TODO
        return null;
    }

    /**
     * Given an alias url, retrieve mapping data
     */
    @Override
    public UrlMappingData getMappingForAliasUrl(String aliasUrl) {
        // TODO
        return null;
    }
}
