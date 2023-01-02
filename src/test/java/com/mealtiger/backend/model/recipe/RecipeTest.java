package com.mealtiger.backend.model.recipe;

import com.mealtiger.backend.database.model.recipe.Ingredient;
import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.database.model.recipe.RecipeDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the methods to convert a Recipe to RecipeDTO and vice versa.
 *
 * @author Sebastian Maier
 */
class RecipeTest {
    /**
     * Tests if Recipe converted to DTO is equal to RecipeDTO.
     */
    @Test
    void convertToDto() {
        Recipe recipe = new Recipe(
                "Gebrannte Mandeln",
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                5,
                15
        );
        recipe.toDTO();

        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setTitle("Gebrannte Mandeln");
        recipeDTO.setIngredients(new Ingredient[]{
                new Ingredient(500, "Gramm", "Mandeln, geschält"),
                new Ingredient(200, "Gramm", "Zucker")
        });

        recipeDTO.setDescription("TestDescription");
        recipeDTO.setDifficulty(3);
        recipeDTO.setRating(5);
        recipeDTO.setTime(15);

        assertEquals(recipeDTO.getTitle(), recipe.getTitle());
        assertArrayEquals(recipeDTO.getIngredients(), recipe.getIngredients());
        assertEquals(recipeDTO.getDescription(), recipe.getDescription());
        assertEquals(recipeDTO.getRating(), recipe.getRating());
        assertEquals(recipeDTO.getTime(), recipe.getTime());
    }

    /**
     * Tests if RecipeDTo converted to Recipe is equal to Recipe.
     */
    @Test
    void convertFromDTO() {
        Recipe recipe = new Recipe(
                "Gebrannte Mandeln",
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                5,
                15
        );

        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setTitle("Gebrannte Mandeln");
        recipeDTO.setIngredients(new Ingredient[]{
                new Ingredient(500, "Gramm", "Mandeln, geschält"),
                new Ingredient(200, "Gramm", "Zucker")
        });

        recipeDTO.setDescription("TestDescription");
        recipeDTO.setDifficulty(3);
        recipeDTO.setRating(5);
        recipeDTO.setTime(15);

        Recipe.fromDTO(recipeDTO);

        assertEquals(recipeDTO.getTitle(), recipe.getTitle());
        assertArrayEquals(recipeDTO.getIngredients(), recipe.getIngredients());
        assertEquals(recipeDTO.getDescription(), recipe.getDescription());
        assertEquals(recipeDTO.getRating(), recipe.getRating());
        assertEquals(recipeDTO.getTime(), recipe.getTime());
    }
}