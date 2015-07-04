package com.urlshortener.dataaccess.database;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.urlshortener.config.Config;
import com.urlshortener.config.ConfigKey;
import com.urlshortener.dataaccess.ClientPoolExhaustedException;
import com.urlshortener.logging.AppLogger;


/**
 * By keeping a limited-size pool of db clients, we can prevent ourselves
 * from overloading the db
 */
public class DbClientPool {

    private final Config config;
    private final AppLogger log;

    private final ConcurrentLinkedQueue<DbClient> clientPool;

    public DbClientPool(Config config, DbClientFactory dbClientFactory) {
        this.config = config;
        this.log = new AppLogger(config);
        this.clientPool = new ConcurrentLinkedQueue<>();

        int numClients = config.getInt(ConfigKey.NumDbClients);
        log.doAssert(numClients > 0, "DbClientPool", "invalid number of clients",
                     "numClients", numClients);
        for (int i = 0; i < numClients; i++) {
            clientPool.offer(dbClientFactory.createDbClient());
        }
    }

    /**
     * Grabs a db client if there's one available
     * Otherwise, fail fast and throws
     * IMPORTANT - return the db client unconditionally
     */
    public DbClient getClient() throws ClientPoolExhaustedException {
        DbClient client = null;
        try {
            client = clientPool.remove();
        } catch (NoSuchElementException e) {
            // TODO: log event
            throw new ClientPoolExhaustedException("Out of database clients");
        }
        return client;
    }

    /**
     * Returns a db client to the pool
     */
    public void returnClient(DbClient client) {
        clientPool.offer(client);
    }
}
