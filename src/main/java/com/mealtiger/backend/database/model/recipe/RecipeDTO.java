package com.mealtiger.backend.database.model.recipe;

import java.util.UUID;

public class RecipeDTO {

    private String id;
    private String title;
    private String userId;
    private Ingredient[] ingredients;
    private String description;
    private double difficulty;
    private double rating;
    private int time;
    private UUID[] images;


    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setImages(UUID[] images) {
        this.images = images;
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

    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public UUID[] getImages() {
        return images;
    }

}