package com.mealtiger.backend.database.repository;

import com.mealtiger.backend.database.model.recipe.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface serves as a MongoRepository for storing data in the mongoDB database specified in MongoClientConfiguration
 */
@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {

    /**
     * Finds all recipes whose titles contain the given string - case-insensitive.
     */
    Page<Recipe> findRecipesByTitleContainingIgnoreCase(String title, Pageable pageable);

    /**
     * Finds all recipes whose titles contain the given string - case-sensitive.
     */
    Page<Recipe> findRecipesByTitleContaining(String title, Pageable pageable);

}
