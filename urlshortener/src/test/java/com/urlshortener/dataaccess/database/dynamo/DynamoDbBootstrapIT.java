package com.urlshortener.dataaccess.database.dynamodb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.urlshortener.AssertionException;
import com.urlshortener.Stage;
import com.urlshortener.config.Config;
import com.urlshortener.dataaccess.database.DbDDLClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration tests for initial bootstrap + verification
 */
public class DynamoDbBootstrapIT {

    private DynamoDbClientFactory factory;
    private DbDDLClient client;
    
    public DynamoDbBootstrapIT() {
        factory = new DynamoDbClientFactory(new Config(), Stage.INTEG);
    }

    @Before
    public void setUp() {
        client = factory.createDDLClient();
        assertEquals("factory constructed wrong client",
                     client.getClass(), DDLDynamoDbClient.class);
    }

    @After
    public void tearDown() {
        client.destroyTables();
        client.close();
    }

    @Test
    public void testDynamoDbBootstrapAndVerify() {
        client.bootstrapTables();
        client.verifyTables();
    }

    @Test
    public void testDynamoDbBootstrapTwice() {
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
}
