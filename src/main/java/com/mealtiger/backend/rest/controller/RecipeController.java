package com.mealtiger.backend.rest.controller;

import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.database.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This acts as the controller-part for our REST API.
 *
 * @author Sebastian Maier, Lucca Greschner
 */
@Service
public class RecipeController {

    private static final Logger log = LoggerFactory.getLogger(RecipeController.class);

    @Autowired
    private RecipeRepository recipeRepository;

    /**
     * This retrieves all recipes from the repository.
     *
     * @return All recipes from the repository as an array.
     */
    public Recipe[] getAllRecipes() {
        log.trace("Getting recipes from repository.");
        return recipeRepository.findAll().toArray(new Recipe[0]);
    }

    /**
     * This saves a recipe to the repository
     *
     * @param recipe Recipe to be saved.
     */
    public void saveRecipe(Recipe recipe) {
        log.trace("Saving recipe {} to repository!", recipe);
        recipeRepository.save(recipe);
    }

    /**
     * Gets a recipe from the repository.
     *
     * @param id ID of the recipe to get.
     * @return Recipe requested.
     */
    public Recipe getRecipe(String id) {
        log.trace("Getting recipe with id {} from repository.", id);
        return recipeRepository.findById(id).orElse(null);
    }

    /**
     * Replaces recipe in repository with new recipe.
     *
     * @param id     ID of the repository to be replaced.
     * @param recipe Recipe to replace the old recipe.
     * @return Whether it was successful or not.
     */
    public boolean replaceRecipe(String id, Recipe recipe) {
        log.trace("Replacing recipe with id {} in repository with {}.", id, recipe);
        Recipe oldRecipe = recipeRepository.findById(id).orElse(null);

        if (oldRecipe != null) {
            oldRecipe.setTitle(recipe.getTitle());
            oldRecipe.setDescription(recipe.getDescription());
            oldRecipe.setIngredients(recipe.getIngredients());
            oldRecipe.setDifficulty(recipe.getDifficulty());
            oldRecipe.setRating(recipe.getRating());
            oldRecipe.setTime(recipe.getTime());

            recipeRepository.deleteById(oldRecipe.getId());
            recipeRepository.save(oldRecipe);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes recipe.
     *
     * @param id ID of the requested recipe.
     * @return Whether it was successful or not.
     */
    public boolean deleteRecipe(String id) {
        boolean returnValue = recipeRepository.existsById(id);

        if (returnValue) {
            recipeRepository.deleteById(id);
        }

        return returnValue;
    }

    /**
     * Checks if a recipe is valid or not.
     *
     * @param recipe Recipe to check.
     */
    public boolean checkValidity(Recipe recipe) {
        try {
            boolean correctRating = recipe.getRating() <= 5 && recipe.getRating() > 0;
            boolean correctDifficulty = recipe.getDifficulty() <= 3 && recipe.getDifficulty() > 0;
            boolean correctIngredients = recipe.getIngredients().length > 0;
            boolean correctDescription = recipe.getDescription().length() > 0;
            boolean correctTime = recipe.getTime() > 0;
            boolean correctTitle = recipe.getTitle().length() > 0;

            return correctRating && correctDifficulty && correctIngredients && correctDescription && correctTime && correctTitle;
        } catch (NullPointerException e) {
            return false;
        }
    }
}