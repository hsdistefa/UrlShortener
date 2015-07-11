package com.urlshortener.urlaliasing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.urlshortener.config.Config;
import com.urlshortener.config.ConfigKey;
import com.urlshortener.AssertionException;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * Tests for url aliasing
 */
public class UrlAliaserTest {
    // TODO anything else we can test

    private UrlAliaser aliaser;
    private Config     config;

    private final String URL = "http://google.com";

    @Before
    public void setUp() {
        config  = new Config();
    }

    @After
    public void tearDown() {
        config  = null;
        aliaser = null;
    }

    // test hash lengths
    private final String[] invalidLTOneHashLengths = new String[] {
        "0", "-1", "-27", "-1000", String.valueOf(Integer.MIN_VALUE),
    };

    private final String[] invalidGT16HashLengths = new String[] {
        "17", "18", "23", "1000", String.valueOf(Integer.MAX_VALUE),
    };

    private void invalidHashLengthHelper(String length) {
        try {
            config.setUnitTestOverride(ConfigKey.HashLength, length);
            aliaser = new UrlAliaser(config);
            fail("should have thrown for invalid hash length");
        }
        catch (AssertionException e) {
            // expected
        }
    }

    @Test
    public void testHashLengthLTOneInvalid() {
        for (String hashLength : invalidLTOneHashLengths) {
            invalidHashLengthHelper(hashLength);
        }
    }

    @Test
    public void testHashLengthValid() {
        String hash;
        for (int i=1; i <= 16; i++) {
            config.setUnitTestOverride(ConfigKey.HashLength,
                                       String.valueOf(i));
            aliaser = new UrlAliaser(config);
            hash = aliaser.getAlias(URL);
            assertEquals(hash.length(), i);
        }
    }

    @Test
    public void testHashLengthGT16Invalid() {
        for (String hashLength : invalidGT16HashLengths) {
            invalidHashLengthHelper(hashLength);
        }
    }

    // test getAlias
    @Test
    public void testNullUrl() {
        aliaser = new UrlAliaser(config);
        try {
            aliaser.getAlias(null);
            fail("should have thrown for null url");
        } catch (AssertionException e) {
            // expected
        }
    }
}
