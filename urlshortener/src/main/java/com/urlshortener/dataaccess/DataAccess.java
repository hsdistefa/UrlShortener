package com.urlshortener.dataaccess;

import com.urlshortener.Stage;
import com.urlshortener.config.Config;
import com.urlshortener.dataaccess.database.DatabaseClientFactory;
import com.urlshortener.dataaccess.database.DatabaseDdlClient;
import com.urlshortener.dataaccess.database.DatabaseDmlClient;
import com.urlshortener.dataaccess.database.DatabaseDmlClientPool;
import com.urlshortener.dataaccess.model.UrlMappingData;


/**
 * Class to access cache / database
 */
public class DataAccess {

    private final Config config;

    private final DatabaseDdlClient databaseDdlClient;
    private final DatabaseDmlClientPool databaseDmlClientPool;

    public DataAccess(Config config, Stage stage) {
        this.config = config;

        DatabaseClientFactory dbFactory = DatabaseClientFactory.create(config,
                                                                       stage);
        this.databaseDdlClient = dbFactory.createDdlClient();
        this.databaseDmlClientPool = new DatabaseDmlClientPool(config, dbFactory);
    }

    /**
     * Bootstraps the persistent store
     * Fails if already bootstrapped
     */
    public void bootstrapPersistentStore() {

        // TODO: bootstrap cache

        // bootstrap database tables
        databaseDdlClient.bootstrapTables();
    }

    /**
     * Verifies the persistent store is bootstrapped
     */
    public void verifyPersistentStore() {

        // TODO: verify cache

        // verify database tables
        databaseDdlClient.verifyTables();
    }

    /**
     * Store a url mapping in the persistent store
     */
    public void createUrlMapping(UrlMappingData data) {

        // go straight to the db
        DatabaseDmlClient client = databaseDmlClientPool.getClient();
        try {
            client.createUrlMapping(data);
        } finally {
            databaseDmlClientPool.returnClient(client);
        }
    }

    /**
     * Given a original url, retrieve mapping data
     */
    public UrlMappingData getMappingForOriginalUrl(String originalUrl)
            throws ClientPoolExhaustedException {

        // TODO: check the cache for entry

        // check the db for entry
        DatabaseDmlClient client = databaseDmlClientPool.getClient();
        try {
            return client.getMappingForOriginalUrl(originalUrl);
        } finally {
            databaseDmlClientPool.returnClient(client);
        }
    }

    /**
     * Given an alias url, retrieve mapping data
     */
    public UrlMappingData getMappingForAliasUrl(String aliasUrl)
            throws ClientPoolExhaustedException {

        // TODO: check the cache for entry

        // check the db for entry
        DatabaseDmlClient client = databaseDmlClientPool.getClient();
        try {
            return client.getMappingForAliasUrl(aliasUrl);
        } finally {
            databaseDmlClientPool.returnClient(client);
        }
    }

    /**
     * Release all resources
     */
    public void close() {
        databaseDdlClient.close();
        databaseDmlClientPool.close();
    }
}
