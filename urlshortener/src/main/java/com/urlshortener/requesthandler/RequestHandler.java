package com.urlshortener.requesthandler;

import static spark.Spark.post;

import com.urlshortener.config.Config;
import com.urlshortener.model.ShortenRequest;
import com.urlshortener.model.ShortenResponse;
import com.urlshortener.validation.AddressValidator;
import com.urlshortener.validation.BlacklistValidator;
import com.urlshortener.validation.Validator;

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

    public static void main(String[] args) {
        post("/shorten", (request, response) -> {

            // unwrap the request
            ShortenRequest shortenRequest = null;
            try {
                shortenRequest = ShortenRequest.fromJson(request.body());
            } catch (JsonSyntaxException e) {
                // TODO - generate bad response
            }

            // validation on the user request
            for (Validator validator : validators) {
                if (!validator.validate(shortenRequest)) {
                    String errorMessage = validator.getErrorMessage(shortenRequest);
                    // TODO - generate bad response
                }
            }

            // TODO -
            //   generate shortened url
            //   query db
            //   handle collisions
            //   write to db

            // generate the successful response
            response.status(200);
            response.type("application/json");
            ShortenResponse shortenResponse = new ShortenResponse(shortenRequest.getUrl(),
                                                                  "dummy_url");
            return ShortenResponse.toJson(shortenResponse);
        });
    }
}
