package com.mealtiger.backend;

import com.mealtiger.backend.configuration.ConfigLoader;
import com.mealtiger.backend.configuration.configs.Config;
import com.mealtiger.backend.configuration.configs.MainConfig;
import com.mealtiger.backend.database.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

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
		Config config;
		try {
			ConfigLoader configLoader = new ConfigLoader();
			config = configLoader.loadConfig(MainConfig.class);
		} catch (Exception e) {
			config = new MainConfig();
		}

		if (((MainConfig) config).getMongoConnectionString().length() == 0) {
			log.info("Database connection string is not defined. Please use the config file main.yml to configure!");
		} else {
			applicationContext = SpringApplication.run(BackendApplication.class, args);
		}
	}

	@Override
	public void run(String... args) {

	}

}
