package com.urlshortener.dataaccess.database.dynamodb;

import static com.urlshortener.logging.AppLogger.doFail;
import static com.urlshortener.logging.AppLogger.info;

import com.urlshortener.Stage;
import com.urlshortener.config.Config;
import com.urlshortener.dataaccess.database.DatabaseClientFactory;
import com.urlshortener.dataaccess.database.DatabaseDdlClient;
import com.urlshortener.dataaccess.database.DatabaseDmlClient;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;


/**
 * Factory that creates DynamoDb clients
 */
public class DynamoClientFactory extends DatabaseClientFactory {

    private final DynamoDB dynamoDb;

    public DynamoClientFactory(Config config, Stage stage) {
        super(config);

        final String METHOD_NAME = "DynamoClientFactory";

        // get properties according to stage
        AWSCredentials credentials = null;
        String endpoint = null;
        String port = null;
        switch (stage) {
            case INTEG:
                credentials = new BasicAWSCredentials("", "");
                endpoint = "http://localhost:";
                port = System.getProperty("dynamodb.port");
                break;
            case ADHOC:
                credentials = new BasicAWSCredentials("", "");
                endpoint = "http://localhost:";
                port = "45678";
                break;
            case CLUSTER:
                doFail(METHOD_NAME, "not implemented yet");
                break;
            case PROD:
                doFail(METHOD_NAME, "not implemented yet");
                break;
            default:
                doFail(METHOD_NAME, "unexpected stage", "stage", stage);
        }

        // configure the client
        info(METHOD_NAME, "client parameters",
             "credentials", credentials,
             "stage", stage,
             "endpoint", endpoint);
        AmazonDynamoDBClient client = new AmazonDynamoDBClient(credentials);
        client.setEndpoint(endpoint + port);
        dynamoDb = new DynamoDB(client);
    }

    @Override
    public DatabaseDdlClient createDdlClient() {
        return new DynamoDdlClient(config, dynamoDb);
    }

    @Override
    public DatabaseDmlClient createDmlClient() {
        return new DynamoDmlClient(dynamoDb);
    }
}
