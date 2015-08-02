package com.urlshortener.requesthandler.normalization;

import static com.urlshortener.logging.AppLogger.doFail;

import io.mola.galimatias.canonicalize.RFC3986Canonicalizer;
import io.mola.galimatias.GalimatiasParseException;
import io.mola.galimatias.URL;

public class UrlNormalizer {

    public String normalizeUrl(String urlString) {
        final String METHOD_NAME = "normalizeUrl";
        RFC3986Canonicalizer urlCanonicalizer = new RFC3986Canonicalizer();

        // parse URL
        URL url = null;
        try {
            url = URL.parse(urlString);
        } catch (GalimatiasParseException e) {
            doFail(METHOD_NAME, "error parsing url", urlString);
        }

        // normalize
        URL normalizedUrl = null;
        try {
            normalizedUrl = urlCanonicalizer.canonicalize(url);
        } catch (GalimatiasParseException e) {
            doFail(METHOD_NAME, "error normalizing url", urlString);
        }

        return normalizedUrl.toString();
    }
}
