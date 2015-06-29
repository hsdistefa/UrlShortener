package com.urlshortener.validation;

import org.apache.commons.validator.routines.UrlValidator;

import com.urlshortener.config.Config;
import com.urlshortener.model.ShortenRequest;


/**
 * Validates the request's url format
 */
public class AddressValidator extends Validator {
    // Get Url Validation options from config file?
    private static UrlValidator urlValidator = new UrlValidator();

    public AddressValidator(Config config) {
        super(config);
    }

    @Override
    public boolean validate(ShortenRequest req) {
        if (req == null) {
            return false;
        }
        return urlValidator.isValid(req.getUrl());
    }

    @Override
    public String getErrorMessage(ShortenRequest req) {
        // TODO
        return "";
    }
}
