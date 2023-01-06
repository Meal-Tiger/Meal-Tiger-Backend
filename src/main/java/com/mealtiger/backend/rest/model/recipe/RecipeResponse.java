package com.mealtiger.backend.rest.model.recipe;

import com.mealtiger.backend.database.model.recipe.Ingredient;
import java.util.UUID;

@SuppressWarnings("unused")
public class RecipeResponse {

    private final String id;
    private final String title;
    private final String userId;
    private final Ingredient[] ingredients;
    private final String description;
    private final double difficulty;
    private final double rating;
    private final int time;
    private final UUID[] images;

    public RecipeResponse(String id, String title, String userId, Ingredient[] ingredients, String description, double difficulty, double rating, int time, UUID[] images) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.ingredients = ingredients;
        this.description = description;
        this.difficulty = difficulty;
        this.rating = rating;
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

    public double getRating() {
        return rating;
    }

    public int getTime() {
        return time;
    }

    public UUID[] getImages() {
        return images;
    }
}
