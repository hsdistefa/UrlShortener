package com.urlshortener.dataaccess.database.dynamodb;

import static com.urlshortener.logging.AppLogger.doAssert;
import static com.urlshortener.logging.AppLogger.doFail;
import static com.urlshortener.logging.AppLogger.info;

import com.urlshortener.config.Config;
import com.urlshortener.config.ConfigKey;
import com.urlshortener.dataaccess.database.DbDDLClient;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.TableDescription;


/**
 * DynamoDB implementation for DDL operations
 */
public class DDLDynamoDbClient implements DbDDLClient {

    private final Config config;
    private final DynamoDB dynamoDb;

    private Table mappingTable;

    public DDLDynamoDbClient(Config config, DynamoDB dynamoDb) {
        this.config = config;
        this.dynamoDb = dynamoDb;
        this.mappingTable = dynamoDb.getTable(DynamoDbConstants.MAPPING_TABLE_NAME);
    }

    /**
     * Bootstraps the database tables
     * Fails if the tables are already bootstrapped
     */
    @Override
    public void bootstrapTables() {
        final String METHOD_NAME = "bootstrapTables";

        // ensure table does not exist yet
        // TODO: timeout?
        try {
            TableDescription td = mappingTable.waitForActiveOrDelete();
            doAssert(td == null, METHOD_NAME, "table should not exist",
                     "tableDescription", td);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            doFail(METHOD_NAME, "got interrupted getting table status");
        }

        // get provisioned throughput from config
        ProvisionedThroughput throughput = new ProvisionedThroughput(
            config.getLong(ConfigKey.MappingTableReads),
            config.getLong(ConfigKey.MappingTableWrites));

        // create the table
        CreateTableRequest createRequest = newCreateMappingTableRequest(throughput);
        info(METHOD_NAME, "creating table", "request", createRequest);
        mappingTable = dynamoDb.createTable(createRequest);

        // wait for creation
        // TODO: timeout?
        try {
            TableDescription td = mappingTable.waitForActive();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            doFail(METHOD_NAME, "got interrupted getting table status");
        }
        info(METHOD_NAME, "table created successlly");
    }

    /**
     * Constructs a CreateTableRequest with the given parameters
     */
    private CreateTableRequest newCreateMappingTableRequest(
            ProvisionedThroughput throughput) {

        CreateTableRequest req = new CreateTableRequest()
            .withTableName(DynamoDbConstants.MAPPING_TABLE_NAME)
            .withAttributeDefinitions(
                new AttributeDefinition(
                    DynamoDbConstants.MAPPING_HASH_KEY_NAME,
                    DynamoDbConstants.MAPPING_HASH_KEY_TYPE))
            .withKeySchema(
                new KeySchemaElement(
                    DynamoDbConstants.MAPPING_HASH_KEY_NAME,
                    KeyType.HASH))
            .withProvisionedThroughput(throughput);
        return req;
    }

    /**
     * Verifies the database tables are bootstrapped
     */
    @Override
    public void verifyTables() {
        final String METHOD_NAME = "verifyTables";

        // get the current state of the table
        TableDescription td = null;
        // TODO: timeout?
        try {
            td = mappingTable.waitForActiveOrDelete();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            doFail(METHOD_NAME, "got interrupted getting table status");
        }
        doAssert(td != null, METHOD_NAME, "table should exist",
                 "tableDescription", td);
    }

    /**
     * Destroys the database tables
     * Not for general prod use
     */
    @Override
    public void destroyTables() {
        final String METHOD_NAME = "destroyTables";

        // get the current state of the table
        TableDescription td = null;
        // TODO: timeout?
        try {
            td = mappingTable.waitForActiveOrDelete();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            doFail(METHOD_NAME, "got interrupted getting table status");
        }
        info(METHOD_NAME, "checked table", "tableDescription", td);

        // table does not exist, nothing to do
        if (td == null) {
            return;
        }

        // issue delete and wait for completion
        DeleteTableResult result = mappingTable.delete();
        info(METHOD_NAME, "issued delete", "deleteTableResult", result);
        // TODO: timeout?
        try {
            mappingTable.waitForDelete();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            doFail(METHOD_NAME, "got interrupted getting table status");
        }
    }

    /**
     * Releases all resources
     */
    @Override
    public void close() {
        dynamoDb.shutdown();
    }
}
