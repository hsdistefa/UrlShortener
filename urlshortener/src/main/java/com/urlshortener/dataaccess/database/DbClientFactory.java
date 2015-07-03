package com.urlshortener.dataaccess.database;

import com.urlshortener.config.Config;
import com.urlshortener.dataaccess.database.dynamodb.DynamoDbClient;


/**
 * Factory creates clients based on the specific persistent store
 */
public class DbClientFactory {

    private final Config config;

    public DbClientFactory(Config config) {
        this.config = config;
    }

    public DbClient createDbClient() {
        // TODO: switch on config
        return new DynamoDbClient();
    }
}
