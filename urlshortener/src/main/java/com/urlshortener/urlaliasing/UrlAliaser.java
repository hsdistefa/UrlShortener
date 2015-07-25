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
    private final int aliasLength;
    private final String alg;

    // constructor
    public UrlAliaser(Config config) {
        final String METHOD_NAME = "UrlAliaser constructor";

        this.alg = config.getString(ConfigKey.Algorithm);
        this.aliasLength = config.getInt(ConfigKey.AliasLength);

        // validate
        doAssert(this.alg != null, METHOD_NAME, "algorithm must not be null");
        doAssert(this.aliasLength > 0,
                 METHOD_NAME,
                 "hash length must be greater than 0");
        doAssert(this.aliasLength <= 16, // bad hard code?
                 METHOD_NAME,
                 "hash length must be less than or equal to 16");
    }

    public String getAlias(String url) {
        final String METHOD_NAME = "getAlias";

        // check values
        doAssert(url != null, METHOD_NAME, "url must not be null");

        // load algorithm
        // NOTE: MessageDigest is NOT thread safe
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(this.alg.toString());
        } catch (NoSuchAlgorithmException e) {
            doFail("getAlias", "no such algorithm", this.alg);
        }

        // specifying charset as utf-8 helps handle encoding issues
        byte[] urlBytes = url.getBytes(Charset.forName("UTF-8"));

        // hash url
        md.update(urlBytes); // add url bytes to be hashed
        byte[] hashBytes = md.digest();

        // convert hash to base 64
        String hash = Base64.encodeBase64URLSafeString(hashBytes);

        // truncate hash
        return hash.substring(0, this.aliasLength);
    }
}
