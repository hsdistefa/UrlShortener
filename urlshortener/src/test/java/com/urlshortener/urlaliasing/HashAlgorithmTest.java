package com.urlshortener.urlaliasing;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for enum HashAlgorithm methods
 */
public class HashAlgorithmTest {
    private static final String[] hashAlgorithmStrings = new String[] {
        "MD2",
        "MD5",
        "SHA-1",
        "SHA-256",
        "SHA-384",
        "SHA-512",
    };

    @Test
    public void testToString() {
        int i = 0;
        for (HashAlgorithm alg : HashAlgorithm.values()) {
            assertEquals(alg.toString(), hashAlgorithmStrings[i]);
            i++;
        }
    }

    @Test
    public void testGetEnum() {
        int i = 0;
        for (HashAlgorithm alg : HashAlgorithm.values()) {
            assertEquals(alg, HashAlgorithm.getEnum(hashAlgorithmStrings[i]));
            i++;
        }
    }
}
