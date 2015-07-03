package com.urlshortener.dataaccess.database;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.urlshortener.config.Config;
import com.urlshortener.dataaccess.ClientPoolExhaustedException;


/**
 * By keeping a limited-size pool of db clients, we can prevent ourselves
 * from overloading the db
 */
public class DbClientPool {

    private final DbClientFactory clientFactory;
    private final ConcurrentLinkedQueue<DbClient> clientPool;

    public DbClientPool(Config config) {
        clientFactory = new DbClientFactory(config);
        clientPool = new ConcurrentLinkedQueue<>();

        int numClients = 10;  // TODO: move to config
        for (int i = 0; i < numClients; i++) {
            clientPool.offer(clientFactory.createDbClient());
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
