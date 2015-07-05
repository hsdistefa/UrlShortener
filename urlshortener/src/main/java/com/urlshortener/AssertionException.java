package com.urlshortener;


/**
 * Runtime exception thrown due to a custom assert
 */
public class AssertionException extends RuntimeException {

    public AssertionException() {
        super();
    }

    public AssertionException(String message) {
        super(message);
    }
}
