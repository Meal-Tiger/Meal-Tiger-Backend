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
    private static String id;
    @Indexed(direction = IndexDirection.ASCENDING)
    private static String title;
    private static Ingredient[] ingredients;
    private static String description;
    private static double difficulty;
    private static double rating;
    private static int time;

    public Recipe(String title, Ingredient[] ingredients, String description, double difficulty, double rating, int time) {
        this.title = title;
        this.ingredients = ingredients;
        this.description = description;
        this.difficulty = difficulty;
        this.rating = rating;
        this.time = time;
    }


    public static Ingredient[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public static String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public static double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public static int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public static String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static String getId() {
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
}
