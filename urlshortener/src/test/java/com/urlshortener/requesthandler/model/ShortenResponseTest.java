package com.urlshortener.requesthandler.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ShortenResponseTest {

    // valid ShortenResponses
    private final ShortenResponse validTest1 = new ShortenResponse("abc", "def");
    private final String[] validJson1 = new String[] {
        "{\"originalUrl\":\"abc\",\"aliasUrl\":\"def\"}",  // canonical
        "{\"originalUrl\":\"abc\";\"aliasUrl\":\"def\"}",  // semi-colon delmiter
        " {\t\"originalUrl\"\n:\"abc\",\"aliasUrl\":\"def\"}",  // various whitespacing
    };

    @Test
    public void testToJson() {
        assertEquals(validJson1[0], ShortenResponse.toJson(validTest1));
    }

    @Test
    public void testFromJson() {
        for (String s : validJson1) {
            assertEquals(validTest1, ShortenResponse.fromJson(s));
        }
    }
}
