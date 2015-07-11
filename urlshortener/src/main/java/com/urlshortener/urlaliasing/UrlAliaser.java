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
     * Returns a hashed alias of the url encoded in url-safe base64
     */
    // variables
    private Config config;
    private String alg;
    private int    hashLength;

    // constructor
    public UrlAliaser(Config config) {
        final String METHOD_NAME = "UrlAliaser constructor";

        this.config     = config;
        this.alg        = config.getString(ConfigKey.Algorithm);
        this.hashLength = config.getInt(ConfigKey.HashLength);

        // validate
        doAssert(this.hashLength > 0,
                 METHOD_NAME,
                 "hash length must be greater than 0");
        doAssert(this.hashLength <= 16, // bad hard code
                 METHOD_NAME,
                 "hash length must be less than or equal to 16");
    }

    // methods
    public String getAlias(String url) {
        final String METHOD_NAME = "getAlias";

        // check values
        doAssert(url != null, METHOD_NAME, "url must not be null");

        try {
            MessageDigest md = MessageDigest.getInstance(this.alg);
            // specifying charset as utf-8 helps handle encoding issues
            byte[] urlBytes = url.getBytes(Charset.forName("UTF-8"));

            // hash url
            md.update(urlBytes); // add url bytes to be hashed
            byte[] hashBytes = md.digest();

            // convert hash to base 64 to further shorten
            String hash = Base64.encodeBase64URLSafeString(hashBytes);

            // shorten hash to this.hashLength
            return hash.substring(0, this.hashLength);
        } catch (NoSuchAlgorithmException e) {
            doFail("getAlias", "no such algorithm", this.alg);
        }
        return ""; // dead code
    }
}
