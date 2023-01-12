package com.mealtiger.backend.rest.api;

import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.rest.controller.RecipeController;
import com.mealtiger.backend.rest.model.Response;
import com.mealtiger.backend.rest.model.recipe.RecipeRequest;
import com.mealtiger.backend.rest.model.recipe.RecipeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
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

        return ResponseEntity.ok(returnValue);
    }


    /**
     * User adds a recipe to database.
     *
     * @param recipeRequest Recipe to add.
     *               HTTP Status 200 if adding recipe was successful, HTTP Status 500 on error/exception.
     */
    @PostMapping("/recipes")
    public ResponseEntity<Response> postRecipe(@Valid @RequestBody RecipeRequest recipeRequest) {
        log.debug("Recipe posted: {}", recipeRequest);

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Response savedRecipe = recipeController.saveRecipe(recipeRequest, userId);

        return ResponseEntity.created(URI.create("/recipes/" + ((RecipeResponse) savedRecipe).getId())).body(savedRecipe);
    }

    /**
     * Sends requested recipe to user.
     *
     * @param id ID of the requested recipe.
     * @return HTTP Status 200 if getting recipes was successful, HTTP Status 404 if it was not found and HTTP Status 500 on error/exception.
     */
    @GetMapping("/recipes/{id}")
    public ResponseEntity<Response> getSingleRecipe(@PathVariable(value = "id") String id) {
        log.debug("Getting recipe with id {}!", id);

        Response returnValue = recipeController.getRecipe(id);

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

        String adminRole = configurator.getString("Authentication.OIDC.adminRole");

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(adminRole));

        if (!recipeController.isUserRecipeOwner(id, userId) && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        recipeController.replaceRecipe(id, recipeRequest);
        return ResponseEntity.noContent().build();
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

        String adminRole = configurator.getString("Authentication.OIDC.adminRole");

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(adminRole));

        if (!recipeController.isUserRecipeOwner(id, userId) && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        recipeController.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
