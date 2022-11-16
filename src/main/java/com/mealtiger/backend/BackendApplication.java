package com.mealtiger.backend;

import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.configuration.exceptions.NoSuchConfigException;
import com.mealtiger.backend.configuration.exceptions.NoSuchPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Properties;

/**
 * This is the main class. Its main method is run to start the application.
 *
 * @author Lucca Greschner, Sebastian Maier
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class BackendApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(BackendApplication.class);

    public static void main(String[] args) {
        Configurator configurator = new Configurator();
        Properties springProperties = configurator.getSpringProperties();

        try {
            if (configurator.getString("Main.Database.mongoDBURL").length() == 0) {
                log.info("Database connection string is not defined. Please use the config file main.yml to configure!");
            } else {
                log.debug("Starting application with custom properties: {}!", springProperties);
                new SpringApplicationBuilder(BackendApplication.class)
                        .properties(springProperties)
                        .build()
                        .run(args);
            }
        } catch (NoSuchPropertyException | NoSuchConfigException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(String... args) {
    }

}
