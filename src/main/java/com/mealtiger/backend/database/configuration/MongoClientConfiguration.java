package com.mealtiger.backend.database.configuration;

import com.mealtiger.backend.configuration.Configurator;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * This class serves as a supplier of MongoClients for the Spring Data API.
 *
 * @author Sebastian Maier
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.mealtiger.backend.database.repository")
public class MongoClientConfiguration extends AbstractMongoClientConfiguration {

    /**
     * @return Database Name
     */
    @Override
    protected String getDatabaseName() {
        Configurator configurator = new Configurator();
        String databaseName;

        databaseName = configurator.getString("Main.Database.databaseName");

        return databaseName;
    }

    /**
     * This method provides a mongoDB Client that is connecting to the server configured in the config file.
     *
     * @return mongoDB Client
     */
    @Override
    public MongoClient mongoClient() {
        Configurator configurator = new Configurator();


        String mongoDBConnectionString;
        mongoDBConnectionString = configurator.getString("Main.Database.mongoDBURL");

        ConnectionString connectionString = new ConnectionString(mongoDBConnectionString);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

}



