package com.urlshortener.dataaccess.database;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;


/**
 * Class that defines DDL operations
 */
public interface DbDDLClient {

    /**
     * Bootstraps the database tables
     * Fails if the tables are already bootstrapped
     */
    public abstract void bootstrapTables();

    /**
     * Verifies the database tables are boostrapped
     */
    public abstract void verifyTables();

    /**
     * Destroys the database tables
     * Not for general prod use
     */
    public abstract void destroyTables();

    /**
     * Releases all resources
     */
    public abstract void close();
}
