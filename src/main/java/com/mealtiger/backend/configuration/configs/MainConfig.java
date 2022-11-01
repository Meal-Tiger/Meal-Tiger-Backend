package com.mealtiger.backend.configuration.configs;

import com.mealtiger.backend.configuration.annotations.Config;
import com.mealtiger.backend.configuration.annotations.ConfigNode;
import org.springframework.boot.logging.LogLevel;

/**
 * Main config file.
 * Properties are represented as non-static fields. Categories can be done by nesting the properties in static nested classes.
 */

@Config(name="Main", configPath = "main.yml")
@SuppressWarnings("unused")
public class MainConfig {
    private final Database database;
    private final Logging logging;

    @SuppressWarnings("unused")
    public MainConfig() {
        database = new Database();
        logging = new Logging();
    }

    @ConfigNode(name = "Database.mongoDBURL", envKey = "DBURL")
    @SuppressWarnings("unused")
    public String getMongoConnectionString() {
        return database.mongoDBURL;
    }

    @ConfigNode(name = "Database.databaseName", envKey = "DB")
    @SuppressWarnings("unused")
    public String getMongoDatabase() {
        return database.database;
    }

    @ConfigNode(name = "Logging.logLevel", envKey = "LOGLEVEL")
    @SuppressWarnings("unused")
    public String getLogLevel() {
        return logging.logLevel;
    }

    static class Database {
        /**
         * MongoDB Connection String used to connect to the database.
         *
         * @see <a href=https://www.mongodb.com/docs/manual/reference/connection-string/>MongoDB Reference Documentation</a>
         */
        private final String mongoDBURL;

        /**
         * MongoDB Database name.
         */
        private final String database;

        Database() {
            mongoDBURL = "";
            database = "MealTigerDB";
        }

    }

    static class Logging {
        private final String logLevel;

        Logging() {
            logLevel = LogLevel.INFO.toString();
        }
    }
}
