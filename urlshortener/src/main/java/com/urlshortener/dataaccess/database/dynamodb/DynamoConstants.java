package com.urlshortener.dataaccess.database.dynamodb;

import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;


/**
 * Constants specific to DynamoDb
 */
public class DynamoConstants {

    // base table
    public static final String DATA_TABLE_NAME = "url-mappings";
    public static final String DATA_HASH_KEY = "alias-url";
    public static final ScalarAttributeType DATA_HASH_KEY_TYPE =
        ScalarAttributeType.S;

    // gsi
    public static final String DATA_GSI_NAME = "original-url-index";
    public static final String DATA_GSI_KEY = "original-url";
    public static final ScalarAttributeType DATA_GSI_KEY_TYPE =
        ScalarAttributeType.S;
    public static final Projection DATA_GSI_PROJECTION =
        new Projection().withProjectionType(ProjectionType.ALL);

    // other fields
    public static final String DATA_CREATED_KEY = "created";
}
