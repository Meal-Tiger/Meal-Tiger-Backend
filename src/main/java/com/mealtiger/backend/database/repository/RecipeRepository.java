package com.mealtiger.backend.database.repository;

import com.mealtiger.backend.database.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecipeRepository extends MongoRepository<Recipe, String> {



}
