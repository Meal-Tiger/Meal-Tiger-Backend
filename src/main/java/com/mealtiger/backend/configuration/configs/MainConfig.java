package com.mealtiger.backend.configuration.configs;

/**
 * Main config file.
 * Properties are represented as non-static fields. Categories can be done by nesting the properties in static nested classes.
 */
public class MainConfig implements Config {

    /**
     * Represents the path the config file is located at.
     */
    public static final String configPath = "main.yml";

    private final Database database;

    public MainConfig() {
        database = new Database();
    }

    public String getMongoConnectionString() {
        return database.mongoDBURL;
    }

    public String getMongoDatabase() {
        return database.database;
    }

    static class Database {
        /**
         * MongoDB Connection String used to connect to the database.
         *
         * @see <a href=https://www.mongodb.com/docs/manual/reference/connection-string/>MongoDB Reference Documentation</a>
         */
        private final String mongoDBURL;

        private final String database;

        Database() {
            mongoDBURL = "";
            database = "MealTigerDB";
        }

    }
}
