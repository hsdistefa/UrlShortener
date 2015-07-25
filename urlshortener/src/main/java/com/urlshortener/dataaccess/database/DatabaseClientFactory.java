package com.urlshortener.dataaccess.database;

import static com.urlshortener.logging.AppLogger.doAssert;

import com.urlshortener.Stage;
import com.urlshortener.config.Config;
import com.urlshortener.config.ConfigKey;
import com.urlshortener.dataaccess.database.dynamodb.DynamoClientFactory;


/**
 * Factory creates clients based on the specific persistent store
 */
public abstract class DatabaseClientFactory {

    // valid databases (only DynamoDb right now)
    public static final String DYNAMODB = "DynamoDB";

    protected final Config config;

    protected DatabaseClientFactory(Config config) {
        this.config = config;
    }

    public abstract DatabaseDdlClient createDdlClient();

    public abstract DatabaseDmlClient createDmlClient();

    public static DatabaseClientFactory create(Config config, Stage stage) {
        String database = config.getString(ConfigKey.Database);

        // for now, we only have DynamoDb
        doAssert(database.equals(DYNAMODB), "createDatabaseClientFactory",
                 "invalid database", "database", database);
        return new DynamoClientFactory(config, stage);
    }
}
