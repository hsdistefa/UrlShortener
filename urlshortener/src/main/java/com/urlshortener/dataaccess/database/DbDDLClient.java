package com.urlshortener.dataaccess.database;

import com.urlshortener.config.Config;


/**
 * Class that defines DDL operations
 */
public abstract class DbDDLClient {

    private final Config config;

    public DbDDLClient(Config config) {
        this.config = config;
    }

    /**
     * Bootstraps the database tables
     * Fails if the tables are already bootstrapped
     */
    public abstract void bootstrapTables();

    /**
     * Verifies the database tables are boostrapped
     */
    public abstract void verifyTables();
}
