package com.mealtiger.backend.rest.controller;

import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.database.model.recipe.RecipeDTO;
import com.mealtiger.backend.database.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * This acts as the controller-part for our REST API.
 *
 * @author Sebastian Maier, Lucca Greschner
 */
@Service
public class RecipeController {

    private static final Logger log = LoggerFactory.getLogger(RecipeController.class);

    private final RecipeRepository recipeRepository;

    /**
     * This constructor is called by the Spring Boot Framework to inject dependencies.
     *
     * @param recipeRepository Automatically injected.
     */
    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * Gets recipes from Database and Returns them sorted and paginated.
     *
     * @param pageNumber int of Page we are on.
     * @param size       int of Page size.
     * @param sort       string to sort after.
     * @return sorted and paginated recipes from the repository as an map.
     */
    public Map<String, Object> getRecipePage(int pageNumber, int size, String sort) {
        log.trace("Getting recipes from repository.");
        try {
            Pageable paging = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, sort));
            Page<Recipe> recipePage = recipeRepository.findAll(paging);

            return assemblePaginatedResult(recipePage.map(Recipe::toDTO));
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }


    /**
     * Gets recipes from Database and Returns them sorted and paginated.
     *
     * @param pageNumber int of Page we are on.
     * @param size       int of Page size.
     * @param sort       string to sort after.
     * @return sorted and paginated recipes from the repository as a map.
     */
    public Map<String, Object> getRecipePageByTitleQuery(int pageNumber, int size, String sort, String query) {
        log.trace("Getting recipes from repository.");
        try {

            Pageable paging = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, sort));

            Page<Recipe> page;
            page = recipeRepository.findRecipesByTitleContainingIgnoreCase(query, paging);

            return assemblePaginatedResult(page.map(Recipe::toDTO));
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    /**
     * This saves a recipe to the repository
     *
     * @param recipeDTO Recipe to be saved.
     */
    public void saveRecipe(RecipeDTO recipeDTO) {
        Recipe recipe = Recipe.fromDTO(recipeDTO);
        log.trace("Saving recipe {} to repository!", recipe);
        recipeRepository.save(recipe);
    }

    /**
     * Gets a recipe from the repository.
     *
     * @param id ID of the recipe to get.
     * @return Recipe requested.
     */
    public RecipeDTO getRecipe(String id) {
        log.trace("Getting recipe with id {} from repository.", id);
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        return recipe != null ? recipe.toDTO(): null;
    }

    /**
     * Replaces recipe in repository with new recipe.
     *
     * @param id     ID of the repository to be replaced.
     * @param recipeDTO Recipe to replace the old recipe.
     * @return Whether it was successful or not.
     */
    public boolean replaceRecipe(String id, RecipeDTO recipeDTO) {
        Recipe recipe = Recipe.fromDTO(recipeDTO);
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
     * This method checks whether a user owns a certain recipe.
     * @param id ID of the recipe.
     * @param userId User ID to check against.
     * @return True if the user owns the recipe, false otherwise.
     */
    public boolean isUserRecipeOwner(String id, String userId) {
        RecipeDTO recipe = getRecipe(id);
        return recipe.getUserId().equals(userId);
    }

    /**
     * This method Returns whether the Recipe does exit or not.
     */
    public boolean doesRecipeExist(String id) {
        return recipeRepository.existsById(id);
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
     * @param recipeDTO Recipe to check.
     */
    public boolean checkValidity(RecipeDTO recipeDTO) {
        Recipe recipe = Recipe.fromDTO(recipeDTO);
        try {
            boolean correctRating = recipe.getRating() <= 5 && recipe.getRating() > 0;
            boolean correctDifficulty = recipe.getDifficulty() <= 3 && recipe.getDifficulty() > 0;
            boolean correctIngredients = recipe.getIngredients().length > 0;
            boolean correctDescription = recipe.getDescription().length() > 0;
            boolean correctTime = recipe.getTime() > 0;
            boolean correctTitle = recipe.getTitle().length() > 0;
            boolean doImagesExist = true;

            for (UUID imageUUID : recipe.getImages()) {
                if (!doesImageExist(imageUUID)) {
                    doImagesExist = false;
                }
            }

            return correctRating && correctDifficulty && correctIngredients && correctDescription && correctTime && correctTitle && doImagesExist;
        } catch (NullPointerException e) {
            return false;
        }
    }

    // PRIVATE HELPER METHODS

    /**
     * This method assembles a map that contains all needed information for a stateless implementation in the frontend application from a page.
     *
     * @param page Page to be used for assembling the result.
     * @return Map that contains the entries 'recipes', 'currentPage', 'totalItems', 'totalPages'.
     */
    private Map<String, Object> assemblePaginatedResult(Page<RecipeDTO> page) {
        List<RecipeDTO> recipes = page.getContent();

        Map<String, Object> response;

        if (!recipes.isEmpty()) {
            response = new HashMap<>();
            response.put("recipes", recipes);
            response.put("currentPage", page.getNumber());
            response.put("totalItems", page.getTotalElements());
            response.put("totalPages", page.getTotalPages());
        } else {
            response = null;
        }
        return response;
    }

    private boolean doesImageExist(UUID uuid) {
        Configurator configurator = new Configurator();
        Path imagePath = Path.of(configurator.getString("Image.imagePath"), uuid.toString());

        return Files.exists(imagePath);
    }
}