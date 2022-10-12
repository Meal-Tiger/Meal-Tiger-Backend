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

    public String getJdbcUrl() {
        return database.jdbcURL;
    }

    public String getDatabaseUser() {
        return database.user;
    }

    public String getDatabasePassword() {
        return database.password;
    }

    static class Database {
        private final String jdbcURL;
        private final String user;
        private final String password;

        Database() {
            jdbcURL = "jdbc:mariadb://localhost:3306/";
            user = "";
            password = "";
        }

    }
}
