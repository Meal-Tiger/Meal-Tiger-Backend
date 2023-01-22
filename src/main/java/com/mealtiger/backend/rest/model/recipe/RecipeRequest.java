package com.mealtiger.backend.rest.model.recipe;

import com.mealtiger.backend.database.model.image_metadata.validation.ImagesExist;
import com.mealtiger.backend.database.model.recipe.Ingredient;
import com.mealtiger.backend.database.model.recipe.Rating;
import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.rest.model.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.UUID;

/**
 * Request entity for the Recipe database model.
 * It is used to validate a request made,
 * but also to ensure no fields are submitted that are added by other users (i.e. the ratings)
 * or that are adjusted by the logic itself (e.g. the userId or the id of the recipe itself)
 */
public class RecipeRequest implements Request<Recipe> {

    // FIELDS

    @NotBlank(message = "Recipe title is mandatory!")
    private String title;
    @NotEmpty
    @NotNull
    @Valid
    private Ingredient[] ingredients;
    @NotBlank(message = "Recipe description is mandatory!")
    private String description;
    @Min(value = 1, message = "Minimum difficulty is 1!")
    @Max(value = 3, message = "Maximum difficulty is 3!")
    private double difficulty;
    @Min(value = 1, message = "Minimum time is 1 minute!")
    private int time;

    @ImagesExist(message = "At least one image does not exist!")
    private UUID[] images;

    // GETTERS

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

    public String getTitle() {
        return title;
    }

    public UUID[] getImages() {
        return images;
    }

    // SETTERS

    public void setTitle(String title) {
        this.title = title;
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

    public void setTime(int time) {
        this.time = time;
    }

    public void setImages(UUID[] images) {
        this.images = images;
    }

    // TO ENTITY

    @Override
    public Recipe toEntity() {
        return new Recipe(
                getTitle(),
                null,
                getIngredients(),
                getDescription(),
                getDifficulty(),
                new Rating[]{},
                getTime(),
                getImages()
        );
    }
}