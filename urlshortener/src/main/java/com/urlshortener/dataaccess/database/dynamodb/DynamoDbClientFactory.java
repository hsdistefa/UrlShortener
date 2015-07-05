package com.urlshortener.dataaccess.database.dynamodb;

import static com.urlshortener.logging.AppLogger.doFail;
import static com.urlshortener.logging.AppLogger.info;

import com.urlshortener.Stage;
import com.urlshortener.config.Config;
import com.urlshortener.dataaccess.database.DbClient;
import com.urlshortener.dataaccess.database.DbClientFactory;
import com.urlshortener.dataaccess.database.DbDDLClient;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;


/**
 * Factory that creates DynamoDb clients
 */
public class DynamoDbClientFactory extends DbClientFactory {

    private final DynamoDB dynamoDb;

    public DynamoDbClientFactory(Config config, Stage stage) {
        super(config);

        final String METHOD_NAME = "DynamoDbClientFactory";

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
    public DbDDLClient createDDLClient() {
        return new DDLDynamoDbClient(config, dynamoDb);
    }

    @Override
    public DbClient createDbClient() {
        return new DynamoDbClient(dynamoDb);
    }
}
