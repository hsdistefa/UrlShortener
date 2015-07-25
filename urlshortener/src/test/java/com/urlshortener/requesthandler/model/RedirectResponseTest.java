package com.urlshortener.requesthandler.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RedirectResponseTest {
    // valid RedirectResponses
    private final RedirectResponse validTest1 = new RedirectResponse("abc");
    private final String[] validJson1 = new String[] {
        "{\"originalUrl\":\"abc\"}",  // canonical
        " {\t\"originalUrl\"\n:\"abc\"}",  // various whitespacing
    };

    @Test
    public void testToJson() {
        assertEquals(validJson1[0], RedirectResponse.toJson(validTest1));
    }
}
