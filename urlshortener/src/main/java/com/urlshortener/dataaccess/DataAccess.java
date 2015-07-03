package com.urlshortener.dataaccess;

import com.urlshortener.config.Config;
import com.urlshortener.dataaccess.database.DbClient;
import com.urlshortener.dataaccess.database.DbClientPool;
import com.urlshortener.dataaccess.model.UrlMappingData;


/**
 * Class to access cache / database
 */ 
public class DataAccess {

    private final Config config;
    private final DbClientPool dbClientPool;

    public DataAccess(Config config) {
        this.config = config;
        this.dbClientPool = new DbClientPool(config);
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
}
