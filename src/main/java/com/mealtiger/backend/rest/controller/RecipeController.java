package com.mealtiger.backend.rest.controller;

import com.mealtiger.backend.database.model.recipe.Rating;
import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.rest.error_handling.exceptions.EntityNotFoundException;
import com.mealtiger.backend.rest.model.recipe.RecipeRequest;
import com.mealtiger.backend.database.repository.RecipeRepository;
import com.mealtiger.backend.rest.model.recipe.RecipeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

            return assemblePaginatedResult(recipePage.map(Recipe::toResponse));
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

            return assemblePaginatedResult(page.map(Recipe::toResponse));
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    /**
     * This saves a recipe to the repository
     *
     * @param recipeRequest Recipe to be saved.
     */
    public void saveRecipe(RecipeRequest recipeRequest) {
        Recipe recipe = Recipe.fromRequest(recipeRequest);
        log.trace("Saving recipe {} to repository!", recipe);
        recipeRepository.save(recipe);
    }

    /**
     * Gets a recipe from the repository.
     *
     * @param id ID of the recipe to get.
     * @return Recipe requested.
     */
    public RecipeResponse getRecipe(String id) {
        log.trace("Getting recipe with id {} from repository.", id);
        Recipe recipe = getRecipeFromRepository(id);
        return recipe != null ? recipe.toResponse(): null;
    }

    /**
     * Replaces recipe in repository with new recipe.
     *
     * @param id     ID of the repository to be replaced.
     * @param recipeRequest Recipe to replace the old recipe.
     * @return Whether it was successful or not.
     */
    public boolean replaceRecipe(String id, RecipeRequest recipeRequest) {
        Recipe recipe = Recipe.fromRequest(recipeRequest);
        log.trace("Replacing recipe with id {} in repository with {}.", id, recipe);
        Recipe oldRecipe = getRecipeFromRepository(id);

        if (oldRecipe != null) {
            oldRecipe.setTitle(recipe.getTitle());
            oldRecipe.setDescription(recipe.getDescription());
            oldRecipe.setIngredients(recipe.getIngredients());
            oldRecipe.setDifficulty(recipe.getDifficulty());
            oldRecipe.setTime(recipe.getTime());
            oldRecipe.setImages(recipe.getImages());

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
        RecipeResponse recipe = getRecipe(id);
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

    // RATINGS

    /**
     * Checks if a user has already rated a recipe.
     * @param recipeId ID of the recipe to check.
     * @param userId UserID of the user to check.
     * @return True, if the user has already rated the recipe. False, if not.
     */
    public boolean doesRatingExist(String recipeId, String userId) {
        Recipe recipe = getRecipeFromRepository(recipeId);
        return Arrays.stream(recipe.getRatings()).anyMatch(rating -> rating.getUserId().equals(userId));
    }

    /**
     * Adds a rating to a recipe.
     * @param recipeId Recipe to add the rating to
     * @param userId UserId of the user who rated the recipe.
     * @param ratingValue Rating the user gave.
     */
    public void addRating(String recipeId, String userId, int ratingValue) {
        Recipe recipe = getRecipeFromRepository(recipeId);
        Rating rating = new Rating(ratingValue, userId);

        List<Rating> allRatings = new ArrayList<>(Arrays.stream(recipe.getRatings()).toList());
        allRatings.add(rating);

        recipe.setRatings(allRatings.toArray(new Rating[0]));

        recipeRepository.save(recipe);
    }

    /**
     * Updates an existent rating.
     * @param recipeId Recipe to update the rating on.
     * @param userId UserId of the user that has rated
     * @param ratingValue Value user has rated.
     */
    public void updateRating(String recipeId, String userId, int ratingValue) {
        deleteRating(recipeId, userId);
        addRating(recipeId, userId, ratingValue);
    }

    /**
     * Delete rating from recipe.
     * @param recipeId Id of the recipe to delete rating from.
     * @param userId UserId of the creator of the rating.
     */
    public void deleteRating(String recipeId, String userId) {
        Recipe recipe = getRecipeFromRepository(recipeId);

        List<Rating> allRatings = new ArrayList<>(Arrays.stream(recipe.getRatings()).toList());
        allRatings.removeIf(r -> r.getUserId().equals(userId));
        recipe.setRatings(allRatings.toArray(new Rating[0]));

        recipeRepository.save(recipe);
    }

    // PRIVATE HELPER METHODS

    /**
     * Gets a recipe from repository by its id.
     * @param recipeId Id of recipe to get.
     * @return Recipe
     */
    private Recipe getRecipeFromRepository(String recipeId) {
        return recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException("Recipe " + recipeId + " does not exist!"));
    }

    /**
     * This method assembles a map that contains all needed information for a stateless implementation in the frontend application from a page.
     *
     * @param page Page to be used for assembling the result.
     * @return Map that contains the entries 'recipes', 'currentPage', 'totalItems', 'totalPages'.
     */
    private Map<String, Object> assemblePaginatedResult(Page<RecipeResponse> page) {
        List<RecipeResponse> recipes = page.getContent();

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
}