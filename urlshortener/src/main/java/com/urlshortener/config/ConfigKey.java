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
        DataTableReads("10"),
        DataTableWrites("10"),
        DataGSIReads("10"),
        DataGSIWrites("10"),

        // url validation
        Allow2Slashes("true"),
        AllowAllSchemes("true"),
        AllowLocalUrls("true"),
        NoFragments("false"),

        // aliasing
        Algorithm("MD5"),
        AliasLength("6"),
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
