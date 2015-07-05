package com.urlshortener.config;

import static com.urlshortener.logging.AppLogger.doAssert;
import static com.urlshortener.logging.AppLogger.doFail;

import java.util.HashMap;
import java.util.Map;


/**
 * Holds configurable values
 * Allows updates during runtime
 */
public class Config {

    // TODO: allow an override file (not just a per-object override)

    // IMPORTANT: only use in unit tests
    private final Map<ConfigKey, String> unitTestOverrides;

    public Config() {
        this.unitTestOverrides = new HashMap<>();
    }

    /**
     * Parses the key's value as an int
     * Fails if value is malformed
     */
    public int getInt(ConfigKey key) {
        doAssert(key != null, "getString", "key must not be null");

        // check unit test override
        String value = unitTestOverrides.get(key) != null ?
                       unitTestOverrides.get(key) :
                       key.getValue();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            doFail("getInt", "malformed int", "value", value);
        }
        return -1;  // dead code
    }

    /**
     * Parses the key's value as a long
     * Fails if value is malformed
     */
    public long getLong(ConfigKey key) {
        doAssert(key != null, "getString", "key must not be null");

        // check unit test override
        String value = unitTestOverrides.get(key) != null ?
                       unitTestOverrides.get(key) :
                       key.getValue();
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            doFail("getLong", "malformed long", "value", value);
        }
        return -1;  // dead code
    }

    /**
     * Parses the key's value as a boolean
     * Fails if value is malformed
     */
    public boolean getBoolean(ConfigKey key) {
        doAssert(key != null, "getString", "key must not be null");

        // check unit test overrides
        String value = unitTestOverrides.get(key) != null ?
                       unitTestOverrides.get(key) :
                       key.getValue();

        // parseBoolean returns false on malformed input
        // we would rather throw
        doAssert(value.equalsIgnoreCase("true") ||
                     value.equalsIgnoreCase("false"),
                     "getBoolean", "malformed boolean", "value", value);
        return Boolean.parseBoolean(value);
    }

    public String getString(ConfigKey key) {
        doAssert(key != null, "getString", "key must not be null");

        // check unit test overrides
        return unitTestOverrides.get(key) != null ?
            unitTestOverrides.get(key) :
            key.getValue();
    }

    public void setUnitTestOverride(ConfigKey key, String value) {
        final String METHOD_NAME = "setUnitTestOverride";
        doAssert(key != null, METHOD_NAME, "key must not be null");
        doAssert(value != null, METHOD_NAME, "value must not be null");
        unitTestOverrides.put(key, value);
    }

    public void removeUnitTestOverride(ConfigKey key) {
        unitTestOverrides.remove(key);
    }
}
