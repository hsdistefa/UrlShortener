package com.urlshortener.urlaliasing;

import com.urlshortener.config.Config;
import com.urlshortener.config.ConfigKey;
import static com.urlshortener.logging.AppLogger.doAssert;
import static com.urlshortener.logging.AppLogger.doFail;

import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;

public class UrlAliaser {
    /**
     * Returns a hash of the url encoded in url-safe base64
     */
    private final Config config = new Config();

    public String getAlias(String url) {
        final String METHOD_NAME = "getAlias";
        final String ALGORITHM   = config.getString(ConfigKey.Algorithm);
        final int    HASH_LENGTH = config.getInt(ConfigKey.HashLength);

        // check values
        doAssert(HASH_LENGTH > 0,
                 METHOD_NAME,
                 "hash length must be greater than 0");
        doAssert(HASH_LENGTH <= 16, // bad hard code
                 METHOD_NAME,
                 "hash length must be less than or equal to 16");

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // specifying charset as utf-8 helps handle encoding issues
            byte[] urlBytes = url.getBytes(Charset.forName("UTF-8"));

            // hash url
            md.update(urlBytes); // add url bytes to be hashed
            byte[] hashBytes = md.digest();

            // convert hash to base 64 to further shorten
            String hash = Base64.encodeBase64URLSafeString(hashBytes);

            // shorten hash to HASH_LENGTH
            return hash.substring(0, HASH_LENGTH);
        } catch (NoSuchAlgorithmException e) {
            doFail("getAlias", "no such algorithm", ALGORITHM);
        }
        return ""; // dead code
    }
}
