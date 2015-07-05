package com.urlshortener.dataaccess.database.dynamodb;

import com.urlshortener.dataaccess.database.DbClient;
import com.urlshortener.dataaccess.model.UrlMappingData;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;


/**
 * DynamoDB implementation for data access operations
 */
public class DynamoDbClient implements DbClient {

    private final Table mappingTable;

    public DynamoDbClient(DynamoDB dynamoDb) {
        mappingTable = dynamoDb.getTable(DynamoDbConstants.MAPPING_TABLE_NAME);
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

    /**
     * Release all resources
     */
    @Override
    public void close() {
        // TODO
    }
}
