package com.urlshortener.validation;

import com.urlshortener.config.Config;
import com.urlshortener.model.ShortenRequest;


/**
 * Base class for request validation
 */
public abstract class Validator {
    private final Config config;

    public Validator(Config config) {
        this.config = config;
    }

    public abstract boolean validate(ShortenRequest req);

    public abstract String getErrorMessage(ShortenRequest req);
}
