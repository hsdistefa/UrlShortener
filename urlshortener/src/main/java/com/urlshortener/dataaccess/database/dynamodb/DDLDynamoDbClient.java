package com.urlshortener.dataaccess.database.dynamodb;

import com.urlshortener.config.Config;
import com.urlshortener.dataaccess.database.DbDDLClient;


/**
 * DynamoDB implementation for DDL operations
 */
public class DDLDynamoDbClient extends DbDDLClient {

    public DDLDynamoDbClient(Config config) {
        super(config);
    }

    /**
     * Bootstraps the database tables
     * Fails if the tables are already bootstrapped
     */
    @Override
    public void bootstrapTables() {
        // TODO
    }

    /**
     * Verifies the database tables are bootstrapped
     */
    @Override
    public void verifyTables() {
        // TODO
    }
}
