package com.mealtiger.backend.rest.api;

import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.rest.model.recipe.RatingDTO;
import com.mealtiger.backend.rest.model.recipe.RecipeRequest;
import com.mealtiger.backend.rest.controller.RecipeController;
import com.mealtiger.backend.rest.error_handling.exceptions.EntityNotFoundException;
import com.mealtiger.backend.rest.model.recipe.RecipeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * This acts as the view-part to our REST API.
 *
 * @author Sebastian Maier, Lucca Greschner
 */
@RestController
public class RecipeAPI {

    private static final Logger log = LoggerFactory.getLogger(RecipeAPI.class);
    private final RecipeController recipeController;
    private final Configurator configurator;

    /**
     * This constructor is called by the Spring Boot Framework to inject dependencies.
     *
     * @param recipeController Automatically injected.
     */
    public RecipeAPI(RecipeController recipeController, Configurator configurator) {
        this.configurator = configurator;
        this.recipeController = recipeController;
    }

    /**
     * Sends all recipes paginated and sorted according to parameters to user.
     *
     * @param page  # of current page, default is 0.
     * @param size  page size, default is 3.
     * @param sort  string to sort after, default is title.
     * @param query string to search after.
     * @return HTTP Status 200 if getting recipes was successful, HTTP Status 404 if it was not found and HTTP Status 500 on error/exception.
     */
    @GetMapping(value = "/recipes")
    public ResponseEntity<Map<String, Object>> getRecipesPage(
            @RequestParam(value = "sort", defaultValue = "title") String sort,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size,
            @RequestParam(value = "q", required = false) String query) {

        log.debug("Sorting after: {}", sort);
        log.debug("Page is: {}", page);
        log.debug("Size is: {}", size);
        log.debug("Query is: {}", query);

        Map<String, Object> returnValue;

        if (query != null) {
            returnValue = recipeController.getRecipePageByTitleQuery(page, size, sort, query);
        } else {
            returnValue = recipeController.getRecipePage(page, size, sort);
        }

        if (returnValue == null) {
            throw new EntityNotFoundException("No recipes in database yet for requested page!");
        }
        return ResponseEntity.ok(returnValue);
    }


    /**
     * User adds a recipe to database.
     *
     * @param recipeRequest Recipe to add.
     *               HTTP Status 200 if adding recipe was successful, HTTP Status 500 on error/exception.
     */
    @PostMapping("/recipes")
    public ResponseEntity<String> postRecipe(@Valid @RequestBody RecipeRequest recipeRequest) {
        log.debug("Recipe posted: {}", recipeRequest);

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        recipeRequest.setUserId(userId);

        recipeController.saveRecipe(recipeRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Sends requested recipe to user.
     *
     * @param id ID of the requested recipe.
     * @return HTTP Status 200 if getting recipes was successful, HTTP Status 404 if it was not found and HTTP Status 500 on error/exception.
     */
    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeResponse> getSingleRecipe(@PathVariable(value = "id") String id) {
        log.debug("Getting recipe with id {}!", id);

        RecipeResponse returnValue = recipeController.getRecipe(id);

        if (returnValue == null) {
            throw new EntityNotFoundException("Recipe " + id + " does not exist!");
        }
        return ResponseEntity.ok(returnValue);
    }

    /**
     * Replaces recipe in database with user-given recipe.
     *
     * @param id     ID of the recipe to be replaced.
     * @param recipeRequest Recipe to replace the old recipe.
     * @return HTTP Status 200 if replacing recipes was successful, HTTP Status 404 if it was not found and HTTP Status 500 on error/exception.
     */
    @PutMapping("/recipes/{id}")
    public ResponseEntity<String> replaceRecipe(@PathVariable(value = "id") String id, @RequestBody RecipeRequest recipeRequest) {
        log.debug("Editing recipe with id {}!", id);

        if (!recipeController.doesRecipeExist(id)) {
            throw new EntityNotFoundException("Recipe " + id + " does not exist!");
        }

        String adminRole = configurator.getString("Authentication.OIDC.adminRole");

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(adminRole));

        if (!recipeController.isUserRecipeOwner(id, userId) && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (recipeController.replaceRecipe(id, recipeRequest)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new EntityNotFoundException("Recipe " + id + " does not exist!");
        }
    }

    /**
     * Deletes recipe.
     *
     * @param id ID of the requested recipe.
     * @return HTTP Status 200 if deleting recipes was successful, HTTP Status 404 if it was not found and HTTP Status 500 on error/exception.
     */
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable(value = "id") String id) {
        log.debug("Deleting recipe with id {}!", id);

        if (!recipeController.doesRecipeExist(id)) {
            throw new EntityNotFoundException("Recipe " + id + " does not exist!");
        }

        String adminRole = configurator.getString("Authentication.OIDC.adminRole");

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(adminRole));

        if (!recipeController.isUserRecipeOwner(id, userId) && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (recipeController.deleteRecipe(id)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new EntityNotFoundException("Recipe " + id + " does not exist!");
        }
    }

    @PostMapping("/recipes/{id}/rating")
    public ResponseEntity<Void> postRating(@PathVariable(value = "id") String id, @Valid @RequestBody RatingDTO rating) {
        if (!recipeController.doesRecipeExist(id)) {
            throw new EntityNotFoundException("Recipe " + id + "does not exist!");
        }

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (recipeController.isUserRecipeOwner(id, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        recipeController.addRating(id, userId, rating.getRatingValue());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/recipe/{id}/rating")
    public ResponseEntity<Void> putRating(@PathVariable(value = "id") String id, @Valid @RequestBody RatingDTO rating) {
        if (!recipeController.doesRecipeExist(id)) {
            throw new EntityNotFoundException("Recipe " + id + "does not exist!");
        }

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (recipeController.isUserRecipeOwner(id, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (recipeController.doesRatingExist(id, userId)) {
            recipeController.updateRating(id, userId, rating.getRatingValue());
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            recipeController.addRating(id, userId, rating.getRatingValue());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }

    @DeleteMapping("/recipe/{id}/rating")
    public ResponseEntity<Void> deleteRating(@PathVariable(value = "id") String id) {
        if (!recipeController.doesRecipeExist(id)) {
            throw new EntityNotFoundException("Recipe " + id + " does not exist!");
        }

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!recipeController.doesRatingExist(id, userId)) {
            throw new EntityNotFoundException("Rating for recipe " + id + " does not exist!");
        }

        recipeController.deleteRating(id, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
