package com.urlshortener.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


import org.junit.Test;


public class AddressValidatorTest {
    // Make test variable names more clear? e.g. invalidMistypedUrl
    private final ShortenRequest validTest1 = new ShortenRequest(
            "http://www.ab.com");
    private final ShortenRequest invalidTest1 = new ShortenRequest("ab");
    private final ShortenRequest invalidTest2 = new ShortenRequest(
            "htp://ab.com");

    @Test
    public void testValidUrl() {
        assertTrue(AddressValidator.validate(validTest1));
    }

    @Test
    public void testInvalidPlainStrings() {
        assertFalse(AddressValidator.validate(invalidTest1));
    }

    @Test
    public void testInvalidProtocol() {
        assertFalse(AddressValidator.validate(invalidTest2));
    }
}
