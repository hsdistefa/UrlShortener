package com.urlshortener.requesthandler.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ShortenRequestTest {

    // valid ShortenRequests
    private final ShortenRequest validTest1 = new ShortenRequest("abc");
    private final String[] validJson1 = new String[] {
        "{\"url\":\"abc\"}",  // canonical
        " {\t\"url\"\n:\"abc\"}",  // various whitespacing
    };

    @Test
    public void testToJson() {
        assertEquals(validJson1[0], ShortenRequest.toJson(validTest1));
    }

    @Test
    public void testFromJson() {
        for (String s : validJson1) {
            assertEquals(validTest1, ShortenRequest.fromJson(s));
        }
    }
}
