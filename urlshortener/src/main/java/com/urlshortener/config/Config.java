package com.urlshortener.config;

import java.util.HashMap;
import java.util.Map;

import com.urlshortener.logging.AppLogger;
import com.urlshortener.logging.AssertionException;


/**
 * Holds configurable values
 * Allows updates during runtime
 */
public class Config {

    // TODO: allow an override file (not just a per-object override)

    private final AppLogger log;

    // IMPORTANT: only use in unit tests
    private final Map<ConfigKey, String> unitTestOverrides;

    public Config() {
        this.log = new AppLogger(this);
        this.unitTestOverrides = new HashMap<>();
    }

    /**
     * Parses the key's value as an int
     * Fails if value is malformed
     */
    public int getInt(ConfigKey key) {
        log.doAssert(key != null, "getString", "key must not be null");

        // check unit test override
        String value = unitTestOverrides.get(key) != null ?
                       unitTestOverrides.get(key) :
                       key.getValue();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.doFail("getInt", "malformed int", "value", value);
        }
        return -1;  // dead code
    }

    /**
     * Parses the key's value as a boolean
     * Fails if value is malformed
     */
    public boolean getBoolean(ConfigKey key) {
        log.doAssert(key != null, "getString", "key must not be null");

        // check unit test overrides
        String value = unitTestOverrides.get(key) != null ?
                       unitTestOverrides.get(key) :
                       key.getValue();

        // parseBoolean returns false on malformed input
        // we would rather throw
        log.doAssert(value.equalsIgnoreCase("true") ||
                     value.equalsIgnoreCase("false"),
                     "getBoolean", "malformed boolean", "value", value);
        return Boolean.parseBoolean(value);
    }

    public String getString(ConfigKey key) {
        log.doAssert(key != null, "getString", "key must not be null");

        // check unit test overrides
        return unitTestOverrides.get(key) != null ?
            unitTestOverrides.get(key) :
            key.getValue();
    }

    public void setUnitTestOverride(ConfigKey key, String value) {
        final String METHOD_NAME = "setUnitTestOverride";
        log.doAssert(key != null, METHOD_NAME, "key must not be null");
        log.doAssert(value != null, METHOD_NAME, "value must not be null");
        unitTestOverrides.put(key, value);
    }

    public void removeUnitTestOverride(ConfigKey key) {
        unitTestOverrides.remove(key);
    }
}
