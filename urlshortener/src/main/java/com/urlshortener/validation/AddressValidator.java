package com.urlshortener.validation;

import org.apache.commons.validator.routines.UrlValidator;

import com.urlshortener.config.Config;
import com.urlshortener.logging.AppLogger;
import com.urlshortener.model.ShortenRequest;


/**
 * Validates the request's url format
 */
public class AddressValidator extends Validator {

    private final AppLogger logger = new AppLogger();
    private final UrlValidator urlValidator;

    public AddressValidator(Config config) {
        super(config);

        // TODO: move to config
        boolean allow2Slashes = true;
        boolean allowAllSchemes = true;
        boolean allowLocalUrls = true;
        boolean noFragments = false;

        long options = (allow2Slashes ? UrlValidator.ALLOW_2_SLASHES : 0) +
                       (allowAllSchemes ? UrlValidator.ALLOW_ALL_SCHEMES : 0) +
                       (allowLocalUrls ? UrlValidator.ALLOW_LOCAL_URLS : 0) +
                       (noFragments ? UrlValidator.NO_FRAGMENTS : 0);

        urlValidator = new UrlValidator(options);
    }

    @Override
    public boolean validate(ShortenRequest req) {
        logger.doAssert(req != null, "validate", "req should not be null");
        return urlValidator.isValid(req.getUrl());
    }

    @Override
    public String getErrorMessage(ShortenRequest req) {
        // TODO
        return "";
    }
}
