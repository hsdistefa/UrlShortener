package com.urlshortener.requesthandler;

import static spark.Spark.post;

import com.urlshortener.config.Config;
import com.urlshortener.dataaccess.DataAccess;
import com.urlshortener.dataaccess.model.UrlMappingData;
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

    private static final Config config = new Config();

    // array of request validators
    // will be executed in the order listed
    private static final Validator[] validators = new Validator[] {
        new AddressValidator(config),
        new BlacklistValidator(config),
    };

    // access for cache / db
    private static final DataAccess dataAccess = new DataAccess(config);

    public static void main(String[] args) {
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
                for (Validator validator : validators) {
                    if (!validator.validate(shortenRequest)) {
                        String errorMessage = validator.getErrorMessage(shortenRequest);
                        // TODO - generate bad response
                    }
                }

                // check persistent store to see if an alias has already been created
                UrlMappingData prevMapping = dataAccess.getMappingForOriginalUrl(
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
                dataAccess.createUrlMapping(newMapping);

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
