package com.urlshortener.requesthandler.normalization;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Integration tests for url normalization
 */
public class UrlNormalizerTest {
    // TODO: add more tests
    private UrlNormalizer urlNormalizer;

    private static final String testUrlCase = "http://www.example.com/";

    private static final String[] equivalentCaseUrl1 = new String[] {
        "HTTP://www.example.com/", // scheme to lower case
        "http://www.Example.com/", // host to lower case
    };

    @Before
    public void setUp() {
        urlNormalizer = new UrlNormalizer();
    }

    @After
    public void tearDown() {
        urlNormalizer = null;
    }

    @Test
    public void testUrlCaseNormalize() {
        for (String url : equivalentCaseUrl1) {
            assertEquals(testUrlCase, urlNormalizer.normalizeUrl(url));
        }
    }

}
