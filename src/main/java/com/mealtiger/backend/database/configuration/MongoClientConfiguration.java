package com.mealtiger.backend.database.configuration;

import com.mealtiger.backend.configuration.Configurator;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
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

    private final Configurator configurator;

    public MongoClientConfiguration(Configurator configurator) {
        this.configurator = configurator;
    }

    /**
     * @return Database Name
     */
    @Override
    protected String getDatabaseName() {
        String databaseName;

        databaseName = configurator.getString("Database.databaseName");

        return databaseName;
    }

    /**
     * This method provides a mongoDB Client that is connecting to the server configured in the config file.
     *
     * @return mongoDB Client
     */
    @Override
    public MongoClient mongoClient() {
        String mongoDBConnectionString;
        mongoDBConnectionString = configurator.getString("Database.mongoDBURL");

        ConnectionString connectionString = new ConnectionString(mongoDBConnectionString);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

}



