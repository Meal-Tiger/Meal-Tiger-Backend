package com.mealtiger.backend.database.model.recipe;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Arrays;

public class RecipeDTO {
        @Id
        private String id;
        @Indexed(direction = IndexDirection.ASCENDING)
        private String title;
        private Ingredient[] ingredients;
        private String description;
        private double difficulty;
        private double rating;
    private int time;

    public void setId(String id) {
        this.id = id;
    }

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

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setTime(int time) {
        this.time = time;
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


        public String getId() {
            return id;
        }
}