package com.urlshortener.requesthandler;

import static com.urlshortener.logging.AppLogger.doAssert;
import static com.urlshortener.logging.AppLogger.doFail;
import static com.urlshortener.logging.AppLogger.info;

import static spark.Spark.post;

import com.urlshortener.Stage;
import com.urlshortener.config.Config;
import com.urlshortener.dataaccess.DataAccess;
import com.urlshortener.dataaccess.model.UrlMappingData;
import com.urlshortener.logging.AppLogger;
import com.urlshortener.requesthandler.model.ShortenRequest;
import com.urlshortener.requesthandler.model.ShortenResponse;
import com.urlshortener.requesthandler.validation.AddressValidator;
import com.urlshortener.requesthandler.validation.BlacklistValidator;
import com.urlshortener.requesthandler.validation.Validator;

import com.google.gson.JsonSyntaxException;

import spark.Request;
import spark.Response;


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

                // check persistent store to see if an alias has already been created
                UrlMappingData prevMapping = DATA_ACCESS.getMappingForOriginalUrl(
                    shortenRequest.url);
                if (prevMapping != null) {
                    return new ShortenResponse(prevMapping);
                }

                // generate new mapping
                String newAliasUrl = "http://dummy.com";  // TODO
                long creationTime = System.currentTimeMillis() / 1000;
                UrlMappingData newMapping = new UrlMappingData(shortenRequest.url,
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
    }
}
