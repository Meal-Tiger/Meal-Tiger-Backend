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
    private int difficulty;
    private int rating;
    private int time;

    public Recipe(String title, Ingredient[] ingredients, String description, int difficulty, int rating, int time) {
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

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
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
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(getIngredients(), recipe.getIngredients())) return false;
        if (getDescription() != null ? !getDescription().equals(recipe.getDescription()) : recipe.getDescription() != null)
            return false;
        return (getTime() != recipe.getTime());
    }

    @Override
    public int hashCode() {
        int result = getTitle() != null ? getTitle().hashCode() : 0;
        result = 31 * result + Arrays.hashCode(getIngredients());
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + getDifficulty();
        result = 31 * result + getRating();
        result = 31 * result + getTime();
        return result;
    }
}
