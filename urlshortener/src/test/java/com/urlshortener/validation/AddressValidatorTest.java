package com.urlshortener.validation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import com.urlshortener.config.Config;
import com.urlshortener.logging.AssertionException;
import com.urlshortener.model.ShortenRequest;

import org.junit.Test;


/**
 * Some light testing
 * We assume the library is rock solid
 */
public class AddressValidatorTest {

    private Config config = new Config();
    private AddressValidator addressValidator = new AddressValidator(config);


    // valid test cases

    private final ShortenRequest[] validProtocols = new ShortenRequest[] {
        new ShortenRequest("http://www.abc.com"),
        new ShortenRequest("https://www.abc.com"),
        new ShortenRequest("ftp://www.abc.com"),
        new ShortenRequest("wtfisthis://www.abc.com"),
    };

    private final ShortenRequest[] validDomainNames = new ShortenRequest[] {
        new ShortenRequest("http://abc.com"),
        new ShortenRequest("http://www.abc.com"),
        new ShortenRequest("http://www.abc.def.com"),
        new ShortenRequest("http://localhost"),
    };

    private final ShortenRequest[] validDomainExts = new ShortenRequest[] {
        new ShortenRequest("http://www.abc.com"),
        new ShortenRequest("http://www.abc.org"),
        new ShortenRequest("http://www.abc.gov"),
    };

    private final ShortenRequest[] validPorts = new ShortenRequest[] {
        new ShortenRequest("http://www.abc.com"),
        new ShortenRequest("http://www.abc.com:80"),
        new ShortenRequest("http://www.abc.com:8080"),
        new ShortenRequest("http://www.abc.com:4567"),
    };

    private final ShortenRequest[] validResources = new ShortenRequest[] {
        new ShortenRequest("http://www.abc.com"),
        new ShortenRequest("http://www.abc.com/"),
        new ShortenRequest("http://www.abc.com/yo"),
        new ShortenRequest("http://www.abc.com/yo.html"),
        new ShortenRequest("http://www.abc.com/yo.css"),
        new ShortenRequest("http://www.abc.com/yo.js"),
        new ShortenRequest("http://www.abc.com/yo/"),
        new ShortenRequest("http://www.abc.com/yo/bro"),
    };

    private final ShortenRequest[] validFragments = new ShortenRequest[] {
        new ShortenRequest("http://www.abc.com/yo#abc"),
        new ShortenRequest("http://www.abc.com/yo#123"),
        new ShortenRequest("http://www.abc.com/yo#bro"),
    };


    // invalid test cases

    private final ShortenRequest[] invalidProtocols = new ShortenRequest[] {
        new ShortenRequest("www.abc.com"),
        new ShortenRequest("/www.abc.com"),
        new ShortenRequest("//www.abc.com"),
        new ShortenRequest("://www.abc.com"),
        new ShortenRequest("123://www.abc.com"),
        new ShortenRequest("http//www.abc.com"),
        new ShortenRequest("http:/www.abc.com"),
    };

    private final ShortenRequest[] invalidDomainNames = new ShortenRequest[] {
        new ShortenRequest("http:///"),
        new ShortenRequest("http://.com"),
        new ShortenRequest("http://.abc.com"),
    };

    private final ShortenRequest[] invalidDomainExts = new ShortenRequest[] {
        new ShortenRequest("http://www.abc.yo"),
        new ShortenRequest("http://www.abc.bro"),
    };

    private final ShortenRequest[] invalidPorts = new ShortenRequest[] {
        new ShortenRequest("http://www.abc.com:80:80"),
        new ShortenRequest("http://www.abc.com:80.80"),
    };

    private final ShortenRequest[] invalidResources = new ShortenRequest[] {
        new ShortenRequest("http://www.abc.com/  "),
        new ShortenRequest("http://www.abc.com/\\"),
    };

    private final ShortenRequest[] invalidFragments = new ShortenRequest[] {
        new ShortenRequest("http://www.abc.com/yo bro"),
    };


    // test helpers

    private void validTestCaseHelper(ShortenRequest[] requests) {
        for (ShortenRequest request : requests) {
            assertTrue("testing: " + request.getUrl(), addressValidator.validate(request));
        }
    }
    
    private void invalidTestCaseHelper(ShortenRequest[] requests) {
        for (ShortenRequest request : requests) {
            assertFalse("testing: " + request.getUrl(), addressValidator.validate(request));
        }
    }


    // valid tests

    @Test
    public void testValidProtocols() {
        validTestCaseHelper(validProtocols);
    }

    @Test
    public void testValidDomainNames() {
        validTestCaseHelper(validDomainNames);
    }

    @Test
    public void testValidDomainExts() {
        validTestCaseHelper(validDomainExts);
    }

    @Test
    public void testValidDomainPorts() {
        validTestCaseHelper(validPorts);
    }

    @Test
    public void testValidResources() {
        validTestCaseHelper(validResources);
    }

    @Test
    public void testValidFragments() {
        validTestCaseHelper(validFragments);
    }


    // invalid  tests

    @Test
    public void testNullRequest() {
        try {
            addressValidator.validate(null);
            fail("should have thrown exception");
        } catch (AssertionException e) {
            // expected
        }
    }

    @Test
    public void testInvalidProtocols() {
        invalidTestCaseHelper(invalidProtocols);
    }

    @Test
    public void testInvalidDomainNames() {
        invalidTestCaseHelper(invalidDomainNames);
    }

    @Test
    public void testInvalidDomainExts() {
        invalidTestCaseHelper(invalidDomainExts);
    }

    @Test
    public void testInvalidDomainPorts() {
        invalidTestCaseHelper(invalidPorts);
    }

    @Test
    public void testInvalidResources() {
        invalidTestCaseHelper(invalidResources);
    }

    @Test
    public void testInvalidFragments() {
        invalidTestCaseHelper(invalidFragments);
    }
}
