package com.urlshortener.requesthandler.validation;

import static com.urlshortener.logging.AppLogger.doAssert;

import org.apache.commons.validator.routines.UrlValidator;

import com.urlshortener.config.Config;
import com.urlshortener.config.ConfigKey;
import com.urlshortener.requesthandler.model.ShortenRequest;


/**
 * Validates the request's url format
 */
public class AddressValidator extends Validator {

    private final UrlValidator urlValidator;

    public AddressValidator(Config config) {
        super(config);

        // get options from config
        boolean allow2Slashes = config.getBoolean(ConfigKey.Allow2Slashes);
        boolean allowAllSchemes = config.getBoolean(ConfigKey.AllowAllSchemes);
        boolean allowLocalUrls = config.getBoolean(ConfigKey.AllowLocalUrls);
        boolean noFragments = config.getBoolean(ConfigKey.NoFragments);

        long options = (allow2Slashes ? UrlValidator.ALLOW_2_SLASHES : 0) +
                       (allowAllSchemes ? UrlValidator.ALLOW_ALL_SCHEMES : 0) +
                       (allowLocalUrls ? UrlValidator.ALLOW_LOCAL_URLS : 0) +
                       (noFragments ? UrlValidator.NO_FRAGMENTS : 0);

        urlValidator = new UrlValidator(options);
    }

    @Override
    public boolean validate(ShortenRequest req) {
        doAssert(req != null, "validate", "req should not be null");
        return urlValidator.isValid(req.url);
    }

    @Override
    public String getErrorMessage(ShortenRequest req) {
        // TODO
        return "";
    }
}
