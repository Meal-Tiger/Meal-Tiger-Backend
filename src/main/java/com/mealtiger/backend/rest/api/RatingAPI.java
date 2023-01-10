package com.mealtiger.backend.rest.api;

import com.mealtiger.backend.rest.controller.RecipeController;
import com.mealtiger.backend.rest.model.rating.RatingRequest;
import com.mealtiger.backend.rest.model.rating.AverageRatingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class RatingAPI {

    private static final Logger log = LoggerFactory.getLogger(RatingAPI.class);
    private final RecipeController recipeController;

    /**
     * This constructor is called by the Spring Boot Framework to inject dependencies.
     *
     * @param recipeController Automatically injected.
     */
    public RatingAPI(RecipeController recipeController) {
        this.recipeController = recipeController;
    }

    @GetMapping("/recipes/{id}/ratings")
    public ResponseEntity<Map<String, Object>> getRatings(@PathVariable(value = "id") String id,
                                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "size", defaultValue = "3") int size) {
        log.debug("Trying to get ratings of recipe {} at page {} with size {}.", id, page, size);

        Map<String, Object> ratingPage = recipeController.getRatings(id, page, size);
        return ResponseEntity.ok(ratingPage);
    }

    @GetMapping("/recipes/{id}/rating")
    public ResponseEntity<AverageRatingResponse> getAverageRating(@PathVariable(value = "id") String id) {
        log.debug("Trying to get average rating for recipe {}.", id);

        return ResponseEntity.ok(recipeController.getAverageRating(id));
    }

    @PostMapping("/recipes/{id}/ratings")
    public ResponseEntity<Void> postRating(@PathVariable(value = "id") String id, @Valid @RequestBody RatingRequest rating) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        log.debug("User {} is trying to add rating {} to recipe {}!", userId, rating, id);

        recipeController.addRating(id, userId, rating.getRatingValue());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/recipes/{id}/ratings")
    public ResponseEntity<Void> putRating(@PathVariable(value = "id") String id, @Valid @RequestBody RatingRequest rating) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        log.debug("User {} is trying to update rating on recipe {} to {}!", userId, id, rating);

        if (recipeController.doesRatingExist(id, userId)) {
            recipeController.updateRating(id, userId, rating.getRatingValue());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            log.trace("Rating by {} on {} does not exist yet! Trying to create one instead!", userId, id);
            recipeController.addRating(id, userId, rating.getRatingValue());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }

    @DeleteMapping("/recipes/{id}/ratings")
    public ResponseEntity<Void> deleteRating(@PathVariable(value = "id") String id) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        log.debug("User {} is trying to delete rating on recipe {}", userId, id);

        recipeController.deleteRating(id, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
