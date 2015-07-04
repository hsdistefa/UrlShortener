package com.urlshortener.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.urlshortener.logging.AssertionException;

import org.junit.Test;


public class ConfigTest {

    @Test
    public void testGetIntValid() {
        Config config = new Config();
        int value = config.getInt(ConfigKey.NumDbClients);
    }

    @Test
    public void testGetBooleanValid() {
        Config config = new Config();
        boolean value = config.getBoolean(ConfigKey.Allow2Slashes);
    }

    @Test
    public void testGetStringValid() {
        Config config = new Config();
        String value = config.getString(ConfigKey.Database);
    }

    @Test
    public void testUnitTestOverridesValid() {
        Config config = new Config();
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
        Config config = new Config();
        try {
            int value = config.getInt(ConfigKey.Database);
            fail("should have thrown for an invalid int");
        } catch (AssertionException e) {
            // expected
        }
    }

    @Test
    public void testGetBooleanInvalid() {
        Config config = new Config();
        try {
            boolean value = config.getBoolean(ConfigKey.Database);
            fail("should have thrown for an invalid boolean");
        } catch (AssertionException e) {
            // expected
        }
    }

    @Test
    public void testNull() {
        Config config = new Config();

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
