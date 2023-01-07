package com.mealtiger.backend.database.model.recipe;

import com.mealtiger.backend.database.model.image_metadata.validation.ImageExists;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.UUID;

public class RecipeDTO {

    private String id;

    @NotBlank(message = "Recipe title is mandatory!")
    private String title;
    private String userId;
    @NotEmpty
    @NotNull
    @Valid
    private Ingredient[] ingredients;
    @NotBlank(message = "Recipe description is mandatory!")
    private String description;
    @Min(value = 1, message = "Minimum difficulty is 1!")
    @Max(value = 3, message = "Maximum difficulty is 3!")
    private double difficulty;
    @Min(value = 1, message = "Minimum rating is 1!")
    @Max(value = 5, message = "Maximum rating is 5!")
    private double rating;
    @Min(value = 1, message = "Minimum time is 1 minute!")
    private int time;

    @ImageExists(message = "At least one image does not exist!")
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