package com.urlshortener.dataaccess.database.dynamodb;

import static org.junit.Assert.assertEquals;

import com.urlshortener.Stage;
import com.urlshortener.config.Config;
import com.urlshortener.config.ConfigKey;
import com.urlshortener.dataaccess.database.DatabaseClientFactory;
import com.urlshortener.dataaccess.database.DatabaseDdlClient;
import com.urlshortener.dataaccess.database.DatabaseDmlClient;
import com.urlshortener.dataaccess.database.DatabaseDmlClientPool;
import com.urlshortener.dataaccess.model.UrlMappingData;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration tests for data flow through Dynamo
 */
public class DynamoDataIT {

    private static boolean tablesCreated = false;
    private static Config config;
    private static DynamoClientFactory factory;
    private static DatabaseDdlClient ddlClient;
    static {
        config = new Config();
        config.setUnitTestOverride(ConfigKey.NumDbClients, "1");
        factory = new DynamoClientFactory(config, Stage.INTEG);
    }

    DatabaseDmlClientPool pool;
    private DatabaseDmlClient client;

    @Before
    public void setUp() {
        // bootstrap on the first test
        if (!tablesCreated) {
            ddlClient = factory.createDdlClient();
            ddlClient.bootstrapTables();
            ddlClient.verifyTables();
            tablesCreated = true;
        }

        // create a new client for each test
        pool = new DatabaseDmlClientPool(config, factory);
        client = pool.getClient();
    }

    @After
    public void tearDown() {
        pool.returnClient(client);
        pool.close();
    }

    @AfterClass
    public static void afterClass() {
        if (tablesCreated) {
            ddlClient.destroyTables();
            ddlClient.close();
        }
    }

    @Test
    public void testDataLifecycle_Happy() {

        // create dummy url mapping
        String originalUrl = "original";
        String aliasUrl = "aliasUrl";
        Long creationTime = 0L;
        UrlMappingData data = new UrlMappingData(originalUrl,
                                                 aliasUrl,
                                                 creationTime);

        // put mapping into Dynamo
        client.createUrlMapping(data);

        // get mapping from original url
        UrlMappingData fromOriginal = client.getMappingForOriginalUrl(originalUrl);
        assertEquals(data, fromOriginal);

        // get mapping from alias url
        UrlMappingData fromAlias = client.getMappingForAliasUrl(aliasUrl);
        assertEquals(data, fromAlias);
    }

    @Test
    public void testCreateMapping_DuplicateOriginal() {
    }

    @Test
    public void testCreateMapping_DuplicateAlias() {
    }

    @Test
    public void testCreateMapping_MalformedItem() {
    }

    @Test
    public void testGetEntry_EmptyOriginalUrl() {
    }

    @Test
    public void testGetEntry_EmptyAliasUrl() {
    }

    @Test
    public void testGetEntry_MalformedItem() {
    }
}
