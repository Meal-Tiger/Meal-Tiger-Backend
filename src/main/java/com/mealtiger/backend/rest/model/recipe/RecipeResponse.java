package com.mealtiger.backend.rest.model.recipe;

import com.mealtiger.backend.database.model.recipe.Ingredient;
import com.mealtiger.backend.rest.model.Response;

import java.util.UUID;

/**
 * Response for the Recipe database model.
 * It does not include the ratings as these are queried using a different endpoint.
 * You could say, this class is used to map the database representation
 * of a recipe to the representation of a recipe used in the API documentation.
 */
@SuppressWarnings("unused")
public class RecipeResponse implements Response {

    private final String id;
    private final String title;
    private final String userId;
    private final Ingredient[] ingredients;
    private final String description;
    private final double difficulty;
    private final int time;
    private final UUID[] images;

    public RecipeResponse(String id, String title, String userId, Ingredient[] ingredients, String description, double difficulty, int time, UUID[] images) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.ingredients = ingredients;
        this.description = description;
        this.difficulty = difficulty;
        this.time = time;
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return userId;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public String getDescription() {
        return description;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public int getTime() {
        return time;
    }

    public UUID[] getImages() {
        return images;
    }
}
