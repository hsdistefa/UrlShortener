package com.urlshortener.dataaccess.database;

import static com.urlshortener.logging.AppLogger.doAssert;

import com.urlshortener.Stage;
import com.urlshortener.config.Config;
import com.urlshortener.config.ConfigKey;
import com.urlshortener.dataaccess.database.dynamodb.DynamoDbClientFactory;


/**
 * Factory creates clients based on the specific persistent store
 */
public abstract class DbClientFactory {

    // valid databases (only DynamoDb right now)
    public static final String DYNAMODB = "DynamoDB";

    protected final Config config;

    public DbClientFactory(Config config) {
        this.config = config;
    }

    public abstract DbDDLClient createDDLClient();

    public abstract DbClient createDbClient();

    public static DbClientFactory createDbClientFactory(Config config, Stage stage) {
        String database = config.getString(ConfigKey.Database);

        // for now, we only have DynamoDb
        doAssert(database.equals(DYNAMODB), "createDbCLientFactory",
                 "invalid database", "database", database);
        return new DynamoDbClientFactory(config, stage);
    }
}
