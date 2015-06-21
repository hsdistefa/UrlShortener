package com.urlshortener.validation;

import com.urlshortener.config.Config;
import com.urlshortener.model.ShortenRequest;


/**
 * Validates the request's url format
 */
public class AddressValidator extends Validator {

    public AddressValidator(Config config) {
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
