package com.urlshortener.dataaccess.database.dynamodb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.urlshortener.AssertionException;
import com.urlshortener.Stage;
import com.urlshortener.config.Config;
import com.urlshortener.config.ConfigKey;
import com.urlshortener.dataaccess.database.DatabaseDdlClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration tests for initial bootstrap + verification
 */
public class DynamoBootstrapIT {

    private DynamoClientFactory factory;
    private DatabaseDdlClient client;
    
    public DynamoBootstrapIT() {
        factory = new DynamoClientFactory(new Config(), Stage.INTEG);
    }

    @Before
    public void setUp() {
        client = factory.createDdlClient();
        assertEquals("factory constructed wrong client",
                     client.getClass(), DynamoDdlClient.class);
    }

    @After
    public void tearDown() {
        client.destroyTables();
        client.close();
    }

    @Test
    public void testDynamoBootstrapAndVerify() {
        client.bootstrapTables();
        client.verifyTables();
    }

    @Test
    public void testDynamoBootstrapTwice() {
        client.bootstrapTables();

        try {
            client.bootstrapTables();
            fail("second bootstrap should have failed");
        } catch (AssertionException e) {
            // expected
        }
    }

    @Test
    public void testVerifyNoBootstrap() {
        try {
            client.verifyTables();
            fail("verify before bootstrap should fail");
        } catch (AssertionException e) {
            // expected
        }
    }

    @Test
    public void testVerifyAfterDestroy() {
        client.bootstrapTables();
        client.destroyTables();

        try {
            client.verifyTables();
            fail("verify should fail after destroy");
        } catch (AssertionException e) {
            // expected
        }
    }

    @Test
    public void testDestroyNoBootstrap() {
        client.destroyTables();
    }

    @Test
    public void testVerifyDifferentBaseThroughput() {

        // override the table config
        Config config = new Config();
        Long prevTableReads = config.getLong(ConfigKey.DataTableReads);
        config.setUnitTestOverride(ConfigKey.DataTableReads,
                                   "" + (prevTableReads + 1L));

        // create the table
        DynamoClientFactory tmpFactory = new DynamoClientFactory(config,
                                                                 Stage.INTEG);
        DatabaseDdlClient tmpClient = tmpFactory.createDdlClient();
        tmpClient.bootstrapTables();
        tmpClient.close();

        // verify with a different config
        try {
            client.verifyTables();
            fail("verify should have failed for wrong table config");
        } catch (AssertionException e) {
            // expected
        }
    }

    @Test
    public void testVerifyDifferentGSIThroughput() {

        // override the gsi config
        Config config = new Config();
        Long prevGSIReads = config.getLong(ConfigKey.DataGSIReads);
        config.setUnitTestOverride(ConfigKey.DataGSIReads,
                                   "" + (prevGSIReads + 1L));

        // create the table
        DynamoClientFactory tmpFactory = new DynamoClientFactory(config,
                                                                 Stage.INTEG);
        DatabaseDdlClient tmpClient = tmpFactory.createDdlClient();
        tmpClient.bootstrapTables();
        tmpClient.close();

        // verify with a different config
        try {
            client.verifyTables();
            fail("verify should have failed for wrong table config");
        } catch (AssertionException e) {
            // expected
        }
    }
}
