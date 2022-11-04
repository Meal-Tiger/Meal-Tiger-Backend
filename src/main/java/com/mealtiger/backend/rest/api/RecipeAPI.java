package com.mealtiger.backend.rest.api;

import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.database.repository.RecipeRepository;
import com.mealtiger.backend.rest.controller.RecipeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This acts as the view-part to our REST API.
 *
 * @author Sebastian Maier, Lucca Greschner
 */
@RestController
public class RecipeAPI {

    private static final Logger log = LoggerFactory.getLogger(RecipeAPI.class);

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private RecipeController recipeController;


    /**
     * Sends requested recipe to user.
     * @param page int of Page we are on.
     * @param size int of Page size.
     * @param sort string to sort after.
     * @return all recipes in database.
     */
    @GetMapping("/recipes")
    private ResponseEntity<Map<String, Object>> getAllRecipesPage(
            @RequestParam() String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        log.debug("Sorting after: {}", sort);
        log.debug("Page is: {}", page);
        log.debug("Size is: {}", size);

        Map<String, Object> returnValue = recipeController.getRecipePage(page,size,sort);

        if (returnValue == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(returnValue);
    }

    /**
     * User adds a recipe to database.
     *
     * @param recipe Recipe to add.
     *               HTTP Status 200 if adding recipe was successful, HTTP Status 500 on error/exception.
     */
    @PostMapping("/recipes")
    public ResponseEntity<?> postRecipe(@RequestBody Recipe recipe) {
        log.debug("Recipe posted: {}", recipe);

        if (!recipeController.checkValidity(recipe)) {
            log.debug("Bad request on posting recipe!");
            return ResponseEntity.badRequest().body("Invalid recipe sent!");
        }

        if (recipe.getId() != null) {
            log.debug("Replacing user given id with null!");
            recipe.setId(null);
        }

        recipeController.saveRecipe(recipe);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * Sends requested recipe to user.
     *
     * @param id ID of the requested recipe.
     * @return HTTP Status 200 if getting recipes was successful, HTTP Status 404 if it was not found and HTTP Status 500 on error/exception.
     */
    @GetMapping("/recipes/{id}")
    public ResponseEntity<Recipe> getSingleRecipe(@PathVariable(value = "id") String id) {
        log.debug("Getting recipe with id {}!", id);

        Recipe returnValue = recipeController.getRecipe(id);

        if (returnValue == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(returnValue);
    }

    /**
     * Replaces recipe in database with user-given recipe.
     *
     * @param id     ID of the recipe to be replaced.
     * @param recipe Recipe to replace the old recipe.
     * @return HTTP Status 200 if replacing recipes was successful, HTTP Status 404 if it was not found and HTTP Status 500 on error/exception.
     */
    @PutMapping("/recipes/{id}")
    public ResponseEntity<?> replaceRecipe(@PathVariable(value = "id") String id, @RequestBody Recipe recipe) {
        log.debug("Editing recipe with id {}!", id);

        if (!recipeController.checkValidity(recipe)) {
            log.debug("Bad request on editing recipe with id {}!", id);
            return ResponseEntity.badRequest().body("Invalid recipe sent!");
        }

        return recipeController.replaceRecipe(id, recipe) ? ResponseEntity.ok(HttpStatus.OK) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipe ID not found!");
    }

    /**
     * Deletes recipe.
     *
     * @param id ID of the requested recipe.
     * @return HTTP Status 200 if deleting recipes was successful, HTTP Status 404 if it was not found and HTTP Status 500 on error/exception.
     */
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable(value = "id") String id) {
        log.debug("Deleting recipe with id {}!", id);

        return recipeController.deleteRecipe(id) ? ResponseEntity.ok(HttpStatus.OK) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipe ID not found!");
    }
}