package com.urlshortener.dataaccess.database.dynamodb;

import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;


/**
 * Constants specific to DynamoDb
 */
public class DynamoDbConstants {

    // base table
    public static final String MAPPING_TABLE_NAME = "url-mappings";
    public static final String MAPPING_HASH_KEY_NAME = "alias-url";
    public static final ScalarAttributeType MAPPING_HASH_KEY_TYPE =
        ScalarAttributeType.S;

    // gsi
    public static final String MAPPING_GSI_NAME = "original-url-index";
    public static final String MAPPING_GSI_KEY_NAME = "original-url";
    public static final ScalarAttributeType MAPPING_GSI_KEY_TYPE =
        ScalarAttributeType.S;
    public static final Projection MAPPING_GSI_PROJECTION =
        new Projection().withProjectionType(ProjectionType.ALL);
}
