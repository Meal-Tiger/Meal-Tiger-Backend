package com.mealtiger.backend.rest.controller;

import com.mealtiger.backend.database.model.recipe.Rating;
import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.rest.error_handling.exceptions.EntityNotFoundException;
import com.mealtiger.backend.rest.error_handling.exceptions.RatingOwnRecipeException;
import com.mealtiger.backend.rest.model.Response;
import com.mealtiger.backend.rest.model.rating.AverageRatingResponse;
import com.mealtiger.backend.rest.model.rating.RatingRequest;
import com.mealtiger.backend.rest.model.recipe.RecipeRequest;
import com.mealtiger.backend.database.repository.RecipeRepository;
import com.mealtiger.backend.rest.model.recipe.RecipeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
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

        Pageable paging = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, sort));
        Page<Recipe> recipePage = recipeRepository.findAll(paging);

        return assemblePaginatedResult(recipePage.map(Recipe::toResponse), "recipes");
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
        Pageable paging = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, sort));

        Page<Recipe> page;
        page = recipeRepository.findRecipesByTitleContainingIgnoreCase(query, paging);

        if (page == null) {
            throw new EntityNotFoundException("No recipes yet for this page!");
        }

        return assemblePaginatedResult(page.map(Recipe::toResponse), "recipes");
    }

    /**
     * This saves a recipe to the repository
     *
     * @param recipeRequest Recipe to be saved.
     */
    public void saveRecipe(RecipeRequest recipeRequest, String userID) {
        Recipe recipe = recipeRequest.toEntity();
        recipe.setUserId(userID);
        log.trace("Saving recipe {} to repository!", recipe);
        recipeRepository.save(recipe);
    }

    /**
     * Gets a recipe from the repository.
     *
     * @param id ID of the recipe to get.
     * @return Recipe requested.
     */
    public Response getRecipe(String id) {
        log.trace("Getting recipe with id {} from repository.", id);
        Recipe recipe = getRecipeFromRepository(id);
        return recipe.toResponse();
    }

    /**
     * Replaces recipe in repository with new recipe.
     *
     * @param id     ID of the repository to be replaced.
     * @param recipeRequest Recipe to replace the old recipe.
     */
    public void replaceRecipe(String id, RecipeRequest recipeRequest) {
        Recipe recipe = recipeRequest.toEntity();
        log.trace("Replacing recipe with id {} in repository with {}.", id, recipe);
        Recipe oldRecipe = getRecipeFromRepository(id);

        oldRecipe.setTitle(recipe.getTitle());
        oldRecipe.setDescription(recipe.getDescription());
        oldRecipe.setIngredients(recipe.getIngredients());
        oldRecipe.setDifficulty(recipe.getDifficulty());
        oldRecipe.setTime(recipe.getTime());
        oldRecipe.setImages(recipe.getImages());

        recipeRepository.deleteById(oldRecipe.getId());
        recipeRepository.save(oldRecipe);
    }

    /**
     * This method checks whether a user owns a certain recipe.
     * @param id ID of the recipe.
     * @param userId User ID to check against.
     * @return True if the user owns the recipe, false otherwise.
     */
    public boolean isUserRecipeOwner(String id, String userId) {
        RecipeResponse recipe = (RecipeResponse) getRecipe(id);
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
     */
    public void deleteRecipe(String id) {
        boolean returnValue = recipeRepository.existsById(id);

        if (returnValue) {
            recipeRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Recipe " + id + " does not exist yet!");
        }
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
     * Gets all ratings of a recipe.
     * @param recipeId Id of the recipe to get the ratings from.
     * @param page Page number.
     * @param size Size of the pages.
     * @return Assembled map of ratings, with total page count and total element count.
     */
    public Map<String, Object> getRatings(String recipeId, int page, int size) {
        Recipe recipe = getRecipeFromRepository(recipeId);
        Rating[] ratings = recipe.getRatings();

        Pageable pageable = PageRequest.of(page, size);
        Page<Rating> ratingPage = new PageImpl<>(List.of(ratings), pageable, ratings.length);

        return assemblePaginatedResult(ratingPage.map(Rating::toResponse), "ratings");
    }

    public Response getAverageRating(String recipeId) {
        Recipe recipe = getRecipeFromRepository(recipeId);
        return new AverageRatingResponse(Arrays.stream(recipe.getRatings()).mapToDouble(Rating::getRatingValue).average().orElse(0));
    }

    /**
     * Adds a rating to a recipe.
     * @param recipeId Recipe to add the rating to
     * @param userId UserId of the user who rated the recipe.
     * @param ratingRequest Rating the user gave.
     * @return The added rating
     */
    public Response addRating(String recipeId, String userId, RatingRequest ratingRequest) {
        if (isUserRecipeOwner(recipeId, userId)) {
            throw new RatingOwnRecipeException("Recipe " + recipeId + " is your own recipe. You may not rate your own recipe!");
        }

        Recipe recipe = getRecipeFromRepository(recipeId);
        Rating rating = ratingRequest.toEntity();
        rating.setId(UUID.randomUUID().toString());
        rating.setUserId(userId);

        List<Rating> allRatings = new ArrayList<>(Arrays.stream(recipe.getRatings()).toList());
        allRatings.add(rating);

        recipe.setRatings(allRatings.toArray(new Rating[0]));

        recipeRepository.save(recipe);

        return rating.toResponse();
    }

    /**
     * Updates an existent rating.
     * @param recipeId Recipe to update the rating on.
     * @param userId UserId of the user that has rated
     * @param ratingRequest Value user has rated.
     */
    public void updateRating(String recipeId, String userId, RatingRequest ratingRequest) {
        deleteRating(recipeId, userId);
        addRating(recipeId, userId, ratingRequest);
    }

    /**
     * Delete rating from recipe.
     * @param recipeId Id of the recipe to delete rating from.
     * @param userId UserId of the creator of the rating.
     */
    public void deleteRating(String recipeId, String userId) {
        Recipe recipe = getRecipeFromRepository(recipeId);

        if (!doesRatingExist(recipeId, userId)) {
            throw new EntityNotFoundException("Rating for recipe " + recipeId + " does not exist yet!");
        }

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
     * @return Map that contains the entries objectName, 'currentPage', 'totalItems', 'totalPages'.
     */
    private Map<String, Object> assemblePaginatedResult(Page<Response> page, String objectName) {
        List<?> objects = page.getContent();

        if (objects.isEmpty()) {
            throw new EntityNotFoundException("No items for page " + page.getNumber() + " yet!");
        }

        Map<String, Object> response = new HashMap<>();
        response.put(objectName, objects);
        response.put("currentPage", page.getNumber());
        response.put("totalItems", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());

        return response;
    }
}