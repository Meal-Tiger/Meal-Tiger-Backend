package com.mealtiger.backend.configuration.configs;

import com.mealtiger.backend.configuration.annotations.Config;
import com.mealtiger.backend.configuration.annotations.ConfigNode;

/**
 * Database Config File.
 * Properties are represented as non-static fields. Categories can be done by nesting the properties in static nested classes.
 *
 * @see com.mealtiger.backend.configuration.Configurator
 */
@Config(name = "Database", configPath = "database.yml")
@SuppressWarnings("unused")
public class DatabaseConfig {
    /**
     * MongoDB Connection String used to connect to the database.
     *
     * @see <a href=https://www.mongodb.com/docs/manual/reference/connection-string/>MongoDB Reference Documentation</a>
     */
    private final String mongoDBURL;

    /**
     * MongoDB Database name.
     */
    private final String databaseName;

    public DatabaseConfig() {
        mongoDBURL = "";
        databaseName = "MealTigerDB";
    }

    @ConfigNode(name = "mongoDBURL", envKey = "DBURL")
    public String getMongoDBURL() {
        return mongoDBURL;
    }

    @ConfigNode(name = "databaseName", envKey = "DB")
    public String getDatabaseName() {
        return databaseName;
    }
}
