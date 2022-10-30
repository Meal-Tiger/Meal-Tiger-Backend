package com.mealtiger.backend.database.repository;

import com.mealtiger.backend.database.model.recipe.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * This interface serves as a MongoRepository for storing data in the mongoDB database specified in MongoClientConfiguration
 */
public interface RecipeRepository extends MongoRepository<Recipe, String> {



}
