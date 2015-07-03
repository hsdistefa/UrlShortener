package com.urlshortener.dataaccess;


/**
 * Exception when trying to get client from an exhausted pool
 */
public class ClientPoolExhaustedException extends RuntimeException {

    public ClientPoolExhaustedException() {
        super();
    }

    public ClientPoolExhaustedException(String message) {
        super(message);
    }
}
