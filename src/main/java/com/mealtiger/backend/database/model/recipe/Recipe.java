package com.mealtiger.backend.database.model.recipe;

import com.mealtiger.backend.rest.model.QueriedObject;
import com.mealtiger.backend.rest.model.Response;
import com.mealtiger.backend.rest.model.recipe.RecipeResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * This class serves as a model for the recipes stored in database.
 *
 * @author Sebastian Maier
 */

@Document(collection = "recipe")
public class Recipe implements QueriedObject {
    @Id
    private String id;
    @Indexed(direction = IndexDirection.ASCENDING)
    private String title;
    private String userId;
    private Ingredient[] ingredients;
    private String description;
    private double difficulty;
    private Rating[] ratings;
    private int time;
    private UUID[] images;

    @PersistenceCreator
    public Recipe(String title, String userId, Ingredient[] ingredients,
                  String description, double difficulty, Rating[] ratings,
                  int time, UUID[] images) {
        this.title = title;
        this.userId = userId;
        this.ingredients = ingredients;
        this.description = description;
        this.difficulty = difficulty;
        this.ratings = ratings;
        this.time = time;
        this.images = Objects.requireNonNullElseGet(images, () -> new UUID[0]);
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

    public Rating[] getRatings() {
        return ratings;
    }

    public void setRatings(Rating[] ratings) {
        this.ratings = ratings;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UUID[] getImages() {
        return images;
    }

    public void setImages(UUID[] images) {
        this.images = images;
    }

    // HELPER METHODS

    /**
     * Calculates the average rating.
     * @return Average rating.
     */
    private double getAverageRating() {
        if (ratings.length == 0) {
            return 0;
        }

        return Arrays.stream(ratings).mapToDouble(Rating::getRatingValue).average().orElseThrow();
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + userId + '\'' +
                ", ingredients=" + Arrays.toString(ingredients) +
                ", description='" + description + '\'' +
                ", difficulty=" + difficulty +
                ", rating=" + getAverageRating() +
                ", time=" + time +
                ", images=" + Arrays.toString(images) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (Double.compare(recipe.getDifficulty(), getDifficulty()) != 0) return false;
        if (getTime() != recipe.getTime()) return false;
        if (!getId().equals(recipe.getId())) return false;
        if (!getTitle().equals(recipe.getTitle())) return false;
        if (!getUserId().equals(recipe.getUserId())) return false;
        if (!Arrays.equals(getIngredients(), recipe.getIngredients())) return false;
        if (!getDescription().equals(recipe.getDescription())) return false;
        if (!Arrays.equals(getRatings(), recipe.getRatings())) return false;
        return Arrays.equals(getImages(), recipe.getImages());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getId().hashCode();
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + getUserId().hashCode();
        result = 31 * result + Arrays.hashCode(getIngredients());
        result = 31 * result + getDescription().hashCode();
        temp = Double.doubleToLongBits(getDifficulty());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + Arrays.hashCode(getRatings());
        result = 31 * result + getTime();
        result = 31 * result + Arrays.hashCode(getImages());
        return result;
    }

    // DTO Methods

    @Override
    public Response toResponse() {
        return new RecipeResponse(
                this.getId(),
                this.getTitle(),
                this.getUserId(),
                this.getIngredients(),
                this.getDescription(),
                this.getDifficulty(),
                this.getTime(),
                this.getImages()
        );
    }
}