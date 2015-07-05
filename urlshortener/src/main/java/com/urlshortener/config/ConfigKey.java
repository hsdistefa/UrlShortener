package com.urlshortener.config;


/**
 * Defines the keys that are available in the Config
 */
public enum ConfigKey {
        // logging
        AppLogName("app_log"),  // TODO
        ServiceLogName("service_log"),  // TODO
    
        // database configs
        Database("DynamoDB"),
        NumDbClients("10"),

        // DynamoDb specific
        MappingTableReads("10"),
        MappingTableWrites("10"),
    
        // url validation
        Allow2Slashes("true"),
        AllowAllSchemes("true"),
        AllowLocalUrls("true"),
        NoFragments("false"),
    ;

    private final String value;

    private ConfigKey(String value) {
        assert value != null;
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
