package com.urlshortener.dataaccess.database.dynamodb;

import static com.urlshortener.logging.AppLogger.doAssert;
import static com.urlshortener.logging.AppLogger.doFail;

import java.util.Iterator;

import com.urlshortener.dataaccess.database.DatabaseDmlClient;
import com.urlshortener.dataaccess.model.UrlMappingData;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Expected;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;


/**
 * DynamoDB implementation for data access operations
 */
public class DynamoDmlClient implements DatabaseDmlClient {

    private final Table dataTable;
    private final Index dataIndex;

    public DynamoDmlClient(DynamoDB dynamoDb) {
        dataTable = dynamoDb.getTable(DynamoConstants.DATA_TABLE_NAME);
        dataIndex = dataTable.getIndex(DynamoConstants.DATA_GSI_NAME);
    }

    /**
     * Stores an (alias url -> original url) mapping
     * Does not overwrite
     */
    @Override
    public void createUrlMapping(UrlMappingData data) {
        Item item = toDynamoItem(data);
        Expected expected =
            new Expected(DynamoConstants.DATA_HASH_KEY)
                .notExist();

        // put item and verify success
        PutItemOutcome outcome = dataTable.putItem(item, expected);
        // TODO: error handling
    }

    /**
     * Given a original url, retrieve mapping data
     */
    @Override
    public UrlMappingData getMappingForOriginalUrl(String originalUrl) {
        final String METHOD_NAME = "getMappingForOriginalUrl";

        GetItemSpec spec = new GetItemSpec()
            .withPrimaryKey(DynamoConstants.DATA_GSI_KEY,
                            originalUrl);

        // TODO: error handling
        ItemCollection<QueryOutcome> result =
            dataIndex.query(DynamoConstants.DATA_GSI_KEY, originalUrl);
        Iterator<Item> it = result.iterator();

        // no hit
        if (!it.hasNext()) {
            return null;
        }

        // get the item
        Item item = it.next();
        if (it.hasNext()) {
            doFail(METHOD_NAME,
                   "too many items in the query result",
                   "first item", item,
                   "second item", it.next());
        }

        return toUrlMappingData(item);
    }

    /**
     * Given an alias url, retrieve mapping data
     */
    @Override
    public UrlMappingData getMappingForAliasUrl(String aliasUrl) {
        GetItemSpec spec = new GetItemSpec()
            .withPrimaryKey(DynamoConstants.DATA_HASH_KEY,
                            aliasUrl);

        // TODO: error handling
        Item item = dataTable.getItem(spec);
        return item == null ? null : toUrlMappingData(item);
    }

    /**
     * Release all resources
     */
    @Override
    public void close() {
        // TODO
    }

    /**
     * Converts a UrlMappingData object to a DynamoDb item
     */
    private static Item toDynamoItem(UrlMappingData data) {
        Item item = new Item()
            .withPrimaryKey(DynamoConstants.DATA_HASH_KEY,
                data.aliasUrl)
            .withString(DynamoConstants.DATA_GSI_KEY,
                data.originalUrl)
            .withLong(DynamoConstants.DATA_CREATED_KEY,
                data.creationTime);
        return item;
    }

    /**
     * Converts a DynamoDb item to a UrlMappingData
     */
    private static UrlMappingData toUrlMappingData(Item item) {
        final String METHOD_NAME = "toUrlMappingData";

        // get the data
        String originalUrl = item.getString(DynamoConstants.DATA_GSI_KEY);
        String aliasUrl = item.getString(DynamoConstants.DATA_HASH_KEY);
        Long creationTime = item.getLong(DynamoConstants.DATA_CREATED_KEY);

        // assert data is as expected
        doAssert(originalUrl != null, METHOD_NAME, "missing field in item");
        doAssert(aliasUrl != null, METHOD_NAME, "missing field in item");
        doAssert(creationTime != null, METHOD_NAME, "missing field in item");

        return new UrlMappingData(originalUrl,
                                  aliasUrl,
                                  creationTime);
    }
}
