package com.mealtiger.backend.database.model.recipe;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

/**
 * This class serves as a model for the recipes stored in database.
 *
 * @author Sebastian Maier
 */

@Document(collection = "recipe")
public class Recipe {
    @Id
    private String id;
    @Indexed(direction = IndexDirection.ASCENDING)
    private String title;
    private Ingredient[] ingredients;
    private String description;
    private double difficulty;
    private double rating;
    private int time;

    public Recipe(String title, Ingredient[] ingredients, String description, double difficulty, double rating, int time) {
        this.title = title;
        this.ingredients = ingredients;
        this.description = description;
        this.difficulty = difficulty;
        this.rating = rating;
        this.time = time;
    }


    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", ingredients=" + Arrays.toString(ingredients) +
                ", description='" + description + '\'' +
                ", difficulty=" + difficulty +
                ", rating=" + rating +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (getDifficulty() != recipe.getDifficulty()) return false;
        if (getRating() != recipe.getRating()) return false;
        if (getTitle() != null ? !getTitle().equals(recipe.getTitle()) : recipe.getTitle() != null) return false;
        if (!Arrays.equals(getIngredients(), recipe.getIngredients())) return false;
        if (getDescription() != null ? !getDescription().equals(recipe.getDescription()) : recipe.getDescription() != null)
            return false;
        return (getTime() == recipe.getTime());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getId().hashCode();
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + Arrays.hashCode(getIngredients());
        result = 31 * result + getDescription().hashCode();
        temp = Double.doubleToLongBits(getDifficulty());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getRating());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getTime();
        return result;
    }

    // DTO Methods

    public RecipeDTO toDTO() {
        RecipeDTO RecipeDTO = new RecipeDTO();
        RecipeDTO.setId(this.getId());
        RecipeDTO.setTitle(this.getTitle());
        RecipeDTO.setTime(this.getTime());
        RecipeDTO.setDescription(this.getDescription());
        RecipeDTO.setIngredients(this.getIngredients());
        RecipeDTO.setDifficulty(this.getDifficulty());
        RecipeDTO.setRating(this.getRating());
        return RecipeDTO;
    }

    public static Recipe fromDTO(RecipeDTO dto) {
        return new Recipe(
                dto.getTitle(),
                dto.getIngredients(),
                dto.getDescription(),
                dto.getDifficulty(),
                dto.getRating(),
                dto.getTime()
        );
    }
}