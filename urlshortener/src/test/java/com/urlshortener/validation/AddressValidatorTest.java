package com.urlshortener.validation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.urlshortener.model.ShortenRequest;
import com.urlshortener.config.Config;

import org.junit.Test;


public class AddressValidatorTest {
    // Make test variable names more clear? e.g. invalidMistypedUrl
    private Config config = new Config();
    private AddressValidator addressValidator = new AddressValidator(config);
    private final ShortenRequest validTest1 = new ShortenRequest(
            "http://www.ab.com");
    private final ShortenRequest invalidTest1 = new ShortenRequest("ab");
    private final ShortenRequest invalidTest2 = new ShortenRequest(
            "htp://ab.com");

    @Test
    public void testValidUrl() {
        assertTrue(addressValidator.validate(validTest1));
    }

    @Test
    public void testInvalidPlainStrings() {
        assertFalse(addressValidator.validate(invalidTest1));
    }

    @Test
    public void testInvalidProtocol() {
        assertFalse(addressValidator.validate(invalidTest2));
    }
}
