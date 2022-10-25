package com.mealtiger.backend.database.configuration;

import com.mealtiger.backend.configuration.ConfigLoader;
import com.mealtiger.backend.configuration.configs.Config;
import com.mealtiger.backend.configuration.configs.MainConfig;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.mealtiger.backend.database.repository")
public class MongoClientConfiguration extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Override
    public MongoClient mongoClient() {
        Config config;
        try {
            ConfigLoader configLoader = new ConfigLoader();
            config = configLoader.loadConfig(MainConfig.class);
        } catch (Exception e) {
            config = new MainConfig();
        }

        ConnectionString connectionString = new ConnectionString(((MainConfig) config).getMongoConnectionString());
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

}



