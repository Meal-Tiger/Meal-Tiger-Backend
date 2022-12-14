package com.mealtiger.backend.rest.api;

import com.mealtiger.backend.database.model.recipe.RecipeDTO;
import com.mealtiger.backend.rest.controller.RecipeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * This constructor is called by the Spring Boot Framework to inject dependencies.
     *
     * @param recipeController Automatically injected.
     */
    public RecipeAPI(RecipeController recipeController) {
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
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(returnValue);
    }


    /**
     * User adds a recipe to database.
     *
     * @param recipeDTO Recipe to add.
     *               HTTP Status 200 if adding recipe was successful, HTTP Status 500 on error/exception.
     */
    @PostMapping("/recipes")
    public ResponseEntity<String> postRecipe(@RequestBody RecipeDTO recipeDTO) {
        log.debug("Recipe posted: {}", recipeDTO);
        if (!recipeController.checkValidity(recipeDTO)) {
            log.debug("Bad request on posting recipe!");
            return ResponseEntity.badRequest().body("Invalid recipe sent!");
        }

        if (recipeDTO.getId() != null) {
            log.debug("Replacing user given id with null!");
            recipeDTO.setId(null);
        }
        recipeController.saveRecipe(recipeDTO);

        return ResponseEntity.ok().build();
    }

    /**
     * Sends requested recipe to user.
     *
     * @param id ID of the requested recipe.
     * @return HTTP Status 200 if getting recipes was successful, HTTP Status 404 if it was not found and HTTP Status 500 on error/exception.
     */
    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeDTO> getSingleRecipe(@PathVariable(value = "id") String id) {
        log.debug("Getting recipe with id {}!", id);

        RecipeDTO returnValue = recipeController.getRecipe(id);

        if (returnValue == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(returnValue);
    }

    /**
     * Replaces recipe in database with user-given recipe.
     *
     * @param id     ID of the recipe to be replaced.
     * @param recipeDTO Recipe to replace the old recipe.
     * @return HTTP Status 200 if replacing recipes was successful, HTTP Status 404 if it was not found and HTTP Status 500 on error/exception.
     */
    @PutMapping("/recipes/{id}")
    public ResponseEntity<String> replaceRecipe(@PathVariable(value = "id") String id, @RequestBody RecipeDTO recipeDTO) {
        log.debug("Editing recipe with id {}!", id);

        if (!recipeController.checkValidity(recipeDTO)) {
            log.debug("Bad request on editing recipe with id {}!", id);
            return ResponseEntity.badRequest().body("Invalid recipe sent!");
        }

        return recipeController.replaceRecipe(id, recipeDTO) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
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

        return recipeController.deleteRecipe(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}