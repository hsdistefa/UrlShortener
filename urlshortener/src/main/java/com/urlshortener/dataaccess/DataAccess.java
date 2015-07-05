package com.urlshortener.dataaccess;

import com.urlshortener.Stage;
import com.urlshortener.config.Config;
import com.urlshortener.dataaccess.database.DbClient;
import com.urlshortener.dataaccess.database.DbClientFactory;
import com.urlshortener.dataaccess.database.DbClientPool;
import com.urlshortener.dataaccess.database.DbDDLClient;
import com.urlshortener.dataaccess.model.UrlMappingData;


/**
 * Class to access cache / database
 */ 
public class DataAccess {

    private final Config config;

    private final DbDDLClient dbDDLClient;
    private final DbClientPool dbClientPool;

    public DataAccess(Config config, Stage stage) {
        this.config = config;

        DbClientFactory dbClientFactory = DbClientFactory.createDbClientFactory(
                                                              config,
                                                              stage);
        this.dbDDLClient = dbClientFactory.createDDLClient();
        this.dbClientPool = new DbClientPool(config, dbClientFactory);
    }

    /**
     * Bootstraps the persistent store
     * Fails if already bootstrapped
     */
    public void bootstrapPersistentStore() {

        // TODO: bootstrap cache

        // bootstrap database tables
        dbDDLClient.bootstrapTables();
    }

    /**
     * Verifies the persistent store is bootstrapped
     */
    public void verifyPersistentStore() {

        // TODO: verify cache

        // verify database tables
        dbDDLClient.verifyTables();
    }

    /**
     * Store a url mapping in the persistent store
     */
    public void createUrlMapping(UrlMappingData data) {

        // go straight to the db
        DbClient client = dbClientPool.getClient();
        try {
            client.createUrlMapping(data);
        } finally {
            dbClientPool.returnClient(client);
        }
    }

    /**
     * Given a original url, retrieve mapping data
     */
    public UrlMappingData getMappingForOriginalUrl(String originalUrl)
            throws ClientPoolExhaustedException {

        // TODO: check the cache for entry

        // check the db for entry
        DbClient client = dbClientPool.getClient();
        try {
            return client.getMappingForOriginalUrl(originalUrl);
        } finally {
            dbClientPool.returnClient(client);
        }
    }

    /**
     * Given an alias url, retrieve mapping data
     */
    public UrlMappingData getMappingForAliasUrl(String aliasUrl)
            throws ClientPoolExhaustedException {

        // TODO: check the cache for entry

        // check the db for entry
        DbClient client = dbClientPool.getClient();
        try {
            return client.getMappingForAliasUrl(aliasUrl);
        } finally {
            dbClientPool.returnClient(client);
        }
    }

    /**
     * Release all resources
     */
    public void close() {
        dbDDLClient.close();
        dbClientPool.close();
    }
}
