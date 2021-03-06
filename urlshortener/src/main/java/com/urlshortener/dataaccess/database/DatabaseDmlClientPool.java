package com.urlshortener.dataaccess.database;

import static com.urlshortener.logging.AppLogger.doAssert;
import static com.urlshortener.logging.AppLogger.info;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.urlshortener.config.Config;
import com.urlshortener.config.ConfigKey;
import com.urlshortener.dataaccess.ClientPoolExhaustedException;


/**
 * By keeping a limited-size pool of db clients, we can prevent ourselves
 * from overloading the db
 */
public class DatabaseDmlClientPool {

    private volatile boolean isShutdown = false;

    private final Config config;
    private final ConcurrentLinkedQueue<DatabaseDmlClient> clientPool;

    public DatabaseDmlClientPool(Config config,
                                 DatabaseClientFactory clientFactory) {
        this.config = config;
        this.clientPool = new ConcurrentLinkedQueue<>();

        int numClients = config.getInt(ConfigKey.NumDbClients);
        doAssert(numClients > 0, "DatabaseDmlClientPool",
                 "invalid number of clients",
                 "numClients", numClients);
        for (int i = 0; i < numClients; i++) {
            clientPool.offer(clientFactory.createDmlClient());
        }
    }

    /**
     * Grabs a db client if there's one available
     * Otherwise, fail fast and throws
     * IMPORTANT - return the db client unconditionally
     */
    public DatabaseDmlClient getClient() throws ClientPoolExhaustedException {
        DatabaseDmlClient client = null;
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
    public void returnClient(DatabaseDmlClient client) {
        if (isShutdown) {
            client.close();
        } else {
            clientPool.offer(client);
        }
    }

    /**
     * Releases all resources
     * TODO: close dynamo
     */
    public void close() {
        info("close", "closing DatabaseDmlClientPool");
        isShutdown = true;
        try {
            while (true) {
                clientPool.remove().close();
            }
        } catch (NoSuchElementException e) {
            // expected
        }
    }
}
