package com.urlshortener.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.urlshortener.AssertionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ConfigTest {

    private Config config;

    @Before
    public void setUp() {
        config = new Config();
    }

    @After
    public void tearDown() {
        config = null;
    }

    @Test
    public void testGetIntValid() {
        int value = config.getInt(ConfigKey.NumDbClients);
    }

    @Test
    public void testGetLongValid() {
        long value = config.getLong(ConfigKey.MappingTableReads);
    }

    @Test
    public void testGetBooleanValid() {
        boolean value = config.getBoolean(ConfigKey.Allow2Slashes);
    }

    @Test
    public void testGetStringValid() {
        String value = config.getString(ConfigKey.Database);
    }

    @Test
    public void testUnitTestOverridesValid() {
        int preValue = config.getInt(ConfigKey.NumDbClients);

        // create the override
        int newValue = preValue + 1;
        config.setUnitTestOverride(ConfigKey.NumDbClients, String.valueOf(newValue));
        assertEquals(newValue, config.getInt(ConfigKey.NumDbClients));

        // remove override
        config.removeUnitTestOverride(ConfigKey.NumDbClients);
        assertEquals(preValue, config.getInt(ConfigKey.NumDbClients));
    }

    @Test
    public void testGetIntInvalid() {
        try {
            int value = config.getInt(ConfigKey.Database);
            fail("should have thrown for an invalid int");
        } catch (AssertionException e) {
            // expected
        }
    }

    @Test
    public void testGetLongInvalid() {
        try {
            long value = config.getLong(ConfigKey.Database);
            fail("should have thrown for an invalid long");
        } catch (AssertionException e) {
            // expected
        }
    }

    @Test
    public void testGetBooleanInvalid() {
        try {
            boolean value = config.getBoolean(ConfigKey.Database);
            fail("should have thrown for an invalid boolean");
        } catch (AssertionException e) {
            // expected
        }
    }

    @Test
    public void testNull() {

        // int
        try {
            int value = config.getInt(null);
            fail("should have thrown for a null key");
        } catch (AssertionException e) {
            // expected
        }

        // boolean
        try {
            boolean value = config.getBoolean(null);
            fail("should have thrown for a null key");
        } catch (AssertionException e) {
            // expected
        }

        // string
        try {
            String value = config.getString(null);
            fail("should have thrown for a null key");
        } catch (AssertionException e) {
            // expected
        }

        // unit test override
        // null key
        try {
            config.setUnitTestOverride(null, "dummy_value");
        } catch (AssertionException e) {
            // expected
        }
        // null value
        try {
            config.setUnitTestOverride(ConfigKey.Database, null);
        } catch (AssertionException e) {
            // expected
        }
        // null key and value
        try {
            config.setUnitTestOverride(null, null);
        } catch (AssertionException e) {
            // expected
        }
    }
}
