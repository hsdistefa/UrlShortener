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
    private Config config;

    private static final String URL = "http://google.com";

    @Before
    public void setUp() {
        config  = new Config();
    }

    @After
    public void tearDown() {
        config  = null;
        aliaser = null;
    }

    // test invalid
    private static final String[] invalidLTOneAliasLengths = new String[] {
        "0",
        "-1",
        "-27",
        "-1000",
        String.valueOf(Integer.MIN_VALUE),
    };

    private static final String[] invalidGT16AliasLengths = new String[] {
        "17",
        "18",
        "23",
        "1000",
        String.valueOf(Integer.MAX_VALUE),
    };

    private static final String[] validHashAlgorithms = new String[] {
        "MD2",
        "MD5",
        "SHA-1",
        "SHA-256",
        "SHA-384",
        "SHA-512",
    };

    private static final String[] invalidHashAlgorithms = new String[] {
        "MD2 ",
        "MD-5",
        "SHA1",
        "SHA-25",
        "sha-384",
        "SHA-512a",
    };

    private void invalidAliasLengthHelper(String length) {
        try {
            config.setUnitTestOverride(ConfigKey.AliasLength, length);
            aliaser = new UrlAliaser(config);
            fail("should have thrown for invalid hash length");
        }
        catch (AssertionException e) {
            // expected
        }
    }

    @Test
    public void testAliasLengthLTOneInvalid() {
        for (String aliasLength : invalidLTOneAliasLengths) {
            invalidAliasLengthHelper(aliasLength);
        }
    }

    @Test
    public void testAliasLengthGT16Invalid() {
        for (String aliasLength : invalidGT16AliasLengths) {
            invalidAliasLengthHelper(aliasLength);
        }
    }

    @Test
    public void testHashAlgorithmInvalid() {
        for (String alg : invalidHashAlgorithms) {
            config.setUnitTestOverride(ConfigKey.Algorithm,
                                       alg);
            try {
                aliaser = new UrlAliaser(config);
                fail("should have thrown for invalid hash algorithm");
            } catch (AssertionException e) {
                // expected
            }
        }
    }

    // test valid
    @Test
    public void testHashAlgorithmValid() {
        for (String alg : validHashAlgorithms) {
            config.setUnitTestOverride(ConfigKey.Algorithm,
                                       alg);
            try {
                aliaser = new UrlAliaser(config);
            } catch (AssertionException e) {
                fail("should not have thrown for valid hash algorithm");
            }
        }
    }

    @Test
    public void testAliasLengthValid() {
        String hash;
        for (int i=1; i <= 16; i++) {
            config.setUnitTestOverride(ConfigKey.AliasLength,
                                       String.valueOf(i));
            aliaser = new UrlAliaser(config);
            hash = aliaser.getAlias(URL);
            assertEquals(hash.length(), i);
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
