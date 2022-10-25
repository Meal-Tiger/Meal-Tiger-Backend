package com.mealtiger.backend.configuration.configs;

/**
 * Main config file.
 *
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

    static class Database {
        private final String mongoDBURL;

        Database() {
            mongoDBURL = "";
        }

    }
}
