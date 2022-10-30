package com.mealtiger.backend;

import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.configuration.exceptions.NoSuchConfigException;
import com.mealtiger.backend.configuration.exceptions.NoSuchPropertyException;
import com.mealtiger.backend.database.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Properties;

/**
 * This is the main class. Its main method is run to start the application.
 *
 * @author Lucca Greschner, Sebastian Maier
 */
@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(BackendApplication.class);

	private static ConfigurableApplicationContext applicationContext;

	@Autowired
	private RecipeRepository repository;

	public static void main(String[] args) {
		Configurator configurator = new Configurator();
		Properties springProperties = configurator.getSpringProperties();

		try {
			if (configurator.getString("Main.Database.mongoDBURL").length() == 0) {
				log.info("Database connection string is not defined. Please use the config file main.yml to configure!");
			} else {
				applicationContext = new SpringApplicationBuilder(BackendApplication.class)
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
