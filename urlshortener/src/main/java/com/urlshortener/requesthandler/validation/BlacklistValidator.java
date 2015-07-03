package com.urlshortener.requesthandler.validation;

import com.urlshortener.config.Config;
import com.urlshortener.requesthandler.model.ShortenRequest;


/**
 * Validates the request's url against a blacklist of domains
 */
public class BlacklistValidator extends Validator {

    public BlacklistValidator(Config config) {
        super(config);
    }

    @Override
    public boolean validate(ShortenRequest req) {
        // TODO
        return true;
    }

    @Override
    public String getErrorMessage(ShortenRequest req) {
        // TODO
        return "";
    }
}
