package com.urlshortener.dataaccess.database.dynamodb;

import static com.urlshortener.logging.AppLogger.doAssert;
import static com.urlshortener.logging.AppLogger.doFail;
import static com.urlshortener.logging.AppLogger.info;

import java.util.List;

import com.urlshortener.config.Config;
import com.urlshortener.config.ConfigKey;
import com.urlshortener.dataaccess.database.DbDDLClient;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableResult;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndexDescription;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughputDescription;
import com.amazonaws.services.dynamodbv2.model.TableDescription;


/**
 * DynamoDB implementation for DDL operations
 */
public class DDLDynamoDbClient implements DbDDLClient {

    private final Config config;
    private final DynamoDB dynamoDb;

    private Table mappingTable;

    private final ProvisionedThroughput baseThroughput;
    private final ProvisionedThroughput gsiThroughput;

    public DDLDynamoDbClient(Config config, DynamoDB dynamoDb) {
        this.config = config;
        this.dynamoDb = dynamoDb;
        this.mappingTable = dynamoDb.getTable(DynamoDbConstants.MAPPING_TABLE_NAME);

        // initialize throughput based on config
        this.baseThroughput = new ProvisionedThroughput(
            config.getLong(ConfigKey.MappingTableReads),
            config.getLong(ConfigKey.MappingTableWrites));
        this.gsiThroughput = new ProvisionedThroughput(
            config.getLong(ConfigKey.MappingGSIReads),
            config.getLong(ConfigKey.MappingGSIWrites));
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

        // create the table
        CreateTableRequest createRequest = newCreateMappingTableRequest(
                                               baseThroughput,
                                               gsiThroughput);
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
            ProvisionedThroughput baseThroughput,
            ProvisionedThroughput gsiThroughput) {

        CreateTableRequest req = new CreateTableRequest()
            .withTableName(DynamoDbConstants.MAPPING_TABLE_NAME)
            .withAttributeDefinitions(
                new AttributeDefinition(
                    DynamoDbConstants.MAPPING_HASH_KEY_NAME,
                    DynamoDbConstants.MAPPING_HASH_KEY_TYPE),
                new AttributeDefinition(
                    DynamoDbConstants.MAPPING_GSI_KEY_NAME,
                    DynamoDbConstants.MAPPING_GSI_KEY_TYPE))
            .withKeySchema(
                new KeySchemaElement(
                    DynamoDbConstants.MAPPING_HASH_KEY_NAME,
                    KeyType.HASH))
            .withProvisionedThroughput(baseThroughput)
            .withGlobalSecondaryIndexes(
                new GlobalSecondaryIndex()
                    .withIndexName(DynamoDbConstants.MAPPING_GSI_NAME)
                    .withKeySchema(
                        new KeySchemaElement(
                            DynamoDbConstants.MAPPING_GSI_KEY_NAME,
                            KeyType.HASH))
                    .withProjection(DynamoDbConstants.MAPPING_GSI_PROJECTION)
                    .withProvisionedThroughput(gsiThroughput));
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

        // assert table is in the correct state
        doAssert(td != null, METHOD_NAME, "table should exist",
                 "tableDescription", td);

        // schema
        List<KeySchemaElement> actualSchema = td.getKeySchema();
        doAssert(actualSchema.size() == 1,
                 METHOD_NAME, "incorrect number of keys",
                 "schema", actualSchema);
        KeySchemaElement actualHashKey = actualSchema.get(0);
        doAssert(actualHashKey.getAttributeName().equals(
                     DynamoDbConstants.MAPPING_HASH_KEY_NAME) &&
                 actualHashKey.getKeyType().equals(KeyType.HASH.name()),
                 METHOD_NAME, "incorrect schema",
                 "hashKey", actualHashKey);

        // provisioned throughput
        ProvisionedThroughputDescription actualThroughput =
            td.getProvisionedThroughput();
        doAssert(actualThroughput.getReadCapacityUnits() ==
                     baseThroughput.getReadCapacityUnits() &&
                 actualThroughput.getWriteCapacityUnits() ==
                     baseThroughput.getWriteCapacityUnits(),
                 METHOD_NAME, "incorrect throughput",
                 "throughput", actualThroughput);
        

        // gsi settings
        List<GlobalSecondaryIndexDescription> actualGSIs =
            td.getGlobalSecondaryIndexes();
        doAssert(actualGSIs != null && actualGSIs.size() == 1,
                 METHOD_NAME, "incorrect gsi settings",
                 "GSIs", actualGSIs);
        GlobalSecondaryIndexDescription actualGSI = actualGSIs.get(0);
        String actualGSIName = actualGSI.getIndexName();
        doAssert(actualGSIName.equals(DynamoDbConstants.MAPPING_GSI_NAME),
                 METHOD_NAME, "incorrect gsi name",
                 "indexName", actualGSIName);

        // schema
        List<KeySchemaElement> actualGSISchema = actualGSI.getKeySchema();
        doAssert(actualSchema.size() == 1,
                 METHOD_NAME, "incorrect number of keys",
                 "schema", actualGSISchema);
        KeySchemaElement actualGSIHashKey = actualGSISchema.get(0);
        doAssert(actualGSIHashKey.getAttributeName().equals(
                     DynamoDbConstants.MAPPING_GSI_KEY_NAME) &&
                 actualGSIHashKey.getKeyType().equals(KeyType.HASH.name()),
                 METHOD_NAME, "incorrect gsi schema",
                 "hashKey", actualGSIHashKey);

        // provisioned throughput
        ProvisionedThroughputDescription actualGSIThroughput =
            actualGSI.getProvisionedThroughput();
        doAssert(actualGSIThroughput.getReadCapacityUnits() ==
                     gsiThroughput.getReadCapacityUnits() &&
                 actualGSIThroughput.getWriteCapacityUnits() ==
                     gsiThroughput.getWriteCapacityUnits(),
                 METHOD_NAME, "incorrect gsi throughput",
                 "throughput", actualGSIThroughput);

        // projection
        Projection actualProjection = actualGSI.getProjection();
        doAssert(actualProjection.equals(
                     DynamoDbConstants.MAPPING_GSI_PROJECTION),
                 METHOD_NAME, "incorrect gsi projection",
                 "projection", actualProjection);
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
