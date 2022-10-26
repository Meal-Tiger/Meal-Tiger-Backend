package com.mealtiger.backend;

import com.mealtiger.backend.configuration.ConfigLoader;
import com.mealtiger.backend.configuration.configs.Config;
import com.mealtiger.backend.configuration.configs.MainConfig;
import com.mealtiger.backend.database.model.Recipe;
import com.mealtiger.backend.database.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is the main class. Its main method is run to start the application.
 *
 * @author Lucca Greschner, Sebastian Maier
 */
@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

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

		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args) {
		repository.deleteAll();

		repository.save(new Recipe("Straßburger Irgendwas"));

		System.out.println("Fetching all recipes: ");
		for(Recipe recipe : repository.findAll()) {
			System.out.println(recipe);
		}
	}

}
