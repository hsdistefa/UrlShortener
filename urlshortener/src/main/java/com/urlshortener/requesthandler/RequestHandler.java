package com.urlshortener.requesthandler;

import static com.urlshortener.logging.AppLogger.doAssert;
import static com.urlshortener.logging.AppLogger.doFail;
import static com.urlshortener.logging.AppLogger.info;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import com.urlshortener.Stage;
import com.urlshortener.config.Config;
import com.urlshortener.dataaccess.DataAccess;
import com.urlshortener.dataaccess.model.UrlMappingData;
import com.urlshortener.logging.AppLogger;
import com.urlshortener.requesthandler.model.RedirectResponse;
import com.urlshortener.requesthandler.model.ShortenRequest;
import com.urlshortener.requesthandler.model.ShortenResponse;
import com.urlshortener.requesthandler.normalization.UrlNormalizer;
import com.urlshortener.requesthandler.validation.AddressValidator;
import com.urlshortener.requesthandler.validation.BlacklistValidator;
import com.urlshortener.requesthandler.validation.Validator;
import com.urlshortener.urlaliasing.UrlAliaser;

import com.google.gson.JsonSyntaxException;

import spark.Request;
import spark.Response;

import org.apache.commons.codec.binary.Base64;
import java.nio.ByteBuffer;


/**
 * The request handler for the frontend servers
 * Since we only have one resource, the logic for execution is here
 * If we add more resources, consider handing off the main logic to
 *   specialized classes
 */
public class RequestHandler {

    private static final Config CONFIG = new Config();
    static {
        AppLogger.initialize(CONFIG);
    }

    // array of request validators
    // will be executed in the order listed
    private static final Validator[] VALIDATORS = new Validator[] {
        new AddressValidator(CONFIG),
        new BlacklistValidator(CONFIG),
    };

    // access for cache / db
    private static DataAccess DATA_ACCESS;

    public static void main(String[] args) {
        final String METHOD_NAME = "main";

        // input validation
        doAssert(args != null, METHOD_NAME, "args should not be null");
        doAssert(args.length == 1, METHOD_NAME, "incorrect number of args");

        // stage
        String stageArg = args[0];
        Stage stage = null;
        try {
            stage = Stage.valueOf(stageArg);
        } catch (IllegalArgumentException e) {
            doFail(METHOD_NAME, "invalid stage", "stage", stageArg);
        }
        info(METHOD_NAME, "stage found", "stage", stage);

        // verify data access is properly bootstrapped
        DATA_ACCESS = new DataAccess(CONFIG, stage);
        DATA_ACCESS.verifyPersistentStore();

        // set obscure port for now
        port(35000);

        /**
         * Takes a url and returns an alias url that will link back to
         * the original url
         */
        post("/shorten", (request, response) -> {
            try {
                // unwrap the request
                ShortenRequest shortenRequest = null;
                try {
                    shortenRequest = ShortenRequest.fromJson(request.body());
                } catch (JsonSyntaxException e) {
                    // TODO - generate bad response
                }

                // validate the user request
                for (Validator validator : VALIDATORS) {
                    if (!validator.validate(shortenRequest)) {
                        String errorMessage = validator.getErrorMessage(shortenRequest);
                        // TODO - generate bad response
                    }
                }

                // normalize the url to remove some would-be duplicates
                UrlNormalizer urlNormalizer = new UrlNormalizer();
                String normalizedUrl = urlNormalizer.normalizeUrl(shortenRequest.url);


                // check persistent store to see if an alias has already been created
                UrlMappingData prevMapping = DATA_ACCESS.getMappingForOriginalUrl(
                    normalizedUrl);
                if (prevMapping != null) {
                    return new ShortenResponse(prevMapping);
                }

                // create new url alias
                UrlAliaser aliaser = new UrlAliaser(CONFIG);
                String newAliasUrl = aliaser.getAlias(normalizedUrl);

                // check for alias (hash) collision
                UrlMappingData prevAlias = DATA_ACCESS.getMappingForAliasUrl(newAliasUrl);
                // TODO make this better
                // TODO: write test for hash collision handling
                if (prevAlias != null) {
                    // linearly probe for open hash
                    boolean urlSafe = true;
                    Base64 encoder = new Base64(urlSafe);
                    Long aliasDec; // TODO larger storage?
                    while (prevAlias != null) {
                        // increment newAliasUrl
                        byte[] aliasBytes = encoder.decodeBase64(newAliasUrl);
                        aliasDec = ByteBuffer.wrap(aliasBytes).getLong() + 1;
                        newAliasUrl = encoder.encodeBase64URLSafeString(
                                ByteBuffer.allocate(Long.SIZE)
                                .putLong(aliasDec).array());

                        prevAlias = DATA_ACCESS.getMappingForAliasUrl(newAliasUrl);
                    }
                }


                // generate new mapping
                long creationTime = System.currentTimeMillis() / 1000;
                UrlMappingData newMapping = new UrlMappingData(normalizedUrl,
                                                               newAliasUrl,
                                                               creationTime);

                // store the new mapping in the db
                DATA_ACCESS.createUrlMapping(newMapping);

                // generate the successful response
                response.status(200);
                response.type("application/json");
                ShortenResponse shortenResponse = new ShortenResponse(newMapping);
                return ShortenResponse.toJson(shortenResponse);
            } catch (Exception e) {
                // TODO
                throw new Exception();
            }
        });

        /**
         * Takes an alias url as a wildcard and redirect to the original
         * url mapped to that wildcard
         */
        get("/*", (request, response) -> {

            try {
                // retrieve url mapping for alias
                String aliasUrl = request.splat()[0];
                UrlMappingData prevMapping = DATA_ACCESS.getMappingForAliasUrl(
                        request.splat()[0]);

                // handle alias not found
                if (prevMapping == null) {
                    // TODO - generate bad response
                    return "nothing";
                }

                // redirect to mapped url (success)
                response.status(200);
                response.redirect(prevMapping.originalUrl);
                RedirectResponse redirectResponse = new RedirectResponse(prevMapping);
                return RedirectResponse.toJson(redirectResponse);
            } catch (Exception e) {
                // TODO: generate bad response
                throw new Exception();
            }
        });
    }
}
