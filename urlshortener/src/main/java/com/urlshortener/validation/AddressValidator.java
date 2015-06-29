package com.urlshortener.validation;

import com.urlshortener.config.Config;
import com.urlshortener.model.ShortenRequest;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 * Validates the request's url format
 */
public class AddressValidator extends Validator {
    // Get Url Regex from config file?
    //private static final String REGEX = AddressValidator.getUrlRegex();

    private static final String REGEX = "^(https?://)(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$";

    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public AddressValidator(Config config) {
        super(config);
    }

    @Override
    public boolean validate(ShortenRequest req) {
        if (req == null) {
            return false;
        }
        Matcher matcher = PATTERN.matcher(req.getUrl());
        return matcher.matches();
    }

    @Override
    public String getErrorMessage(ShortenRequest req) {
        // TODO
        return "";
    }
}
