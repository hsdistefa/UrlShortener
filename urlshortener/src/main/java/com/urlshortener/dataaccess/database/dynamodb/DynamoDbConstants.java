package com.urlshortener.dataaccess.database.dynamodb;

import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;


/**
 * Constants specific to DynamoDb
 */
public class DynamoDbConstants {

    public static final String MAPPING_TABLE_NAME = "url-mappings";
    public static final String MAPPING_HASH_KEY_NAME = "original-url";
    public static final ScalarAttributeType MAPPING_HASH_KEY_TYPE =
        ScalarAttributeType.S;
}
