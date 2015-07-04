package com.urlshortener.dataaccess.database;

import com.urlshortener.config.Config;
import com.urlshortener.config.ConfigKey;
import com.urlshortener.dataaccess.database.dynamodb.DDLDynamoDbClient;
import com.urlshortener.dataaccess.database.dynamodb.DynamoDbClient;
import com.urlshortener.logging.AppLogger;


/**
 * Factory creates clients based on the specific persistent store
 */
public class DbClientFactory {

    public static final String DYNAMODB = "DynamoDB";

    private final Config config;
    private final AppLogger log;

    public DbClientFactory(Config config) {
        this.config = config;
        this.log = new AppLogger(config);
    }

    public DbDDLClient createDDLClient() {
        String database = config.getString(ConfigKey.Database);

        // change if we have more databases
        log.doAssert(database.equals(DYNAMODB), "createDDLClient",
                     "unknown database", "database", database);
        return new DDLDynamoDbClient(config);
    }

    public DbClient createDbClient() {
        String database = config.getString(ConfigKey.Database);

        // change if we have more databases
        log.doAssert(database.equals(DYNAMODB), "createDbClient",
                     "unknown database", "database", database);
        return new DynamoDbClient(config);
    }
}
