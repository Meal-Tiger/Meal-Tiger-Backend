package com.mealtiger.backend.database.model.recipe;

import com.mealtiger.backend.rest.model.recipe.RecipeRequest;
import com.mealtiger.backend.rest.model.recipe.RecipeResponse;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the methods to convert a Recipe to RecipeDTO and vice versa.
 *
 * @author Sebastian Maier
 */
class RecipeTest {

    private static final String SAMPLE_USER_ID = "123e4567-e89b-12d3-a456-42661417400";
    private static final String SAMPLE_IMAGE_ID = "bc2ef248-91a5-4f06-86f1-726b412170ca";

    /**
     * Tests if Recipe converted to RecipeResponse is equal to original Recipe.
     */
    @Test
    void convertToResponse() {
        // NO RATING, NO IMAGES

        Recipe recipe = new Recipe(
                "Gebrannte Mandeln",
                SAMPLE_USER_ID,
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                new Rating[]{},
                15,
                new UUID[]{}
        );

        RecipeResponse recipeResponse = recipe.toResponse();

        assertEquals(recipe.getId(), recipeResponse.getId());
        assertEquals(recipe.getUserId(), recipeResponse.getUserId());
        assertArrayEquals(recipe.getIngredients(), recipeResponse.getIngredients());
        assertEquals(recipe.getDescription(), recipeResponse.getDescription());
        assertEquals(recipe.getDifficulty(), recipeResponse.getDifficulty());
        assertEquals(recipe.getTime(), recipeResponse.getTime());
        assertArrayEquals(recipe.getImages(), recipeResponse.getImages());

        // WITH RATING, NO IMAGES

        recipe = new Recipe(
                "Gebrannte Mandeln",
                SAMPLE_USER_ID,
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                new Rating[]{new Rating(4, SAMPLE_USER_ID), new Rating(2, SAMPLE_USER_ID)},
                15,
                new UUID[]{}
        );

        recipeResponse = recipe.toResponse();

        assertEquals(recipe.getId(), recipeResponse.getId());
        assertEquals(recipe.getUserId(), recipeResponse.getUserId());
        assertArrayEquals(recipe.getIngredients(), recipeResponse.getIngredients());
        assertEquals(recipe.getDescription(), recipeResponse.getDescription());
        assertEquals(recipe.getDifficulty(), recipeResponse.getDifficulty());
        assertEquals(recipe.getTime(), recipeResponse.getTime());
        assertArrayEquals(recipe.getImages(), recipeResponse.getImages());

        // WITH RATING, IMAGES

        recipe = new Recipe(
                "Gebrannte Mandeln",
                SAMPLE_USER_ID,
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                new Rating[]{new Rating(4, SAMPLE_USER_ID), new Rating(2, SAMPLE_USER_ID)},
                15,
                new UUID[]{UUID.fromString("e818d27a-ee98-473d-82d3-9e2455969ab0"), UUID.fromString("a3e94848-3ee4-4200-a9ed-a7f00debe554")}
        );

        recipeResponse = recipe.toResponse();

        assertEquals(recipe.getId(), recipeResponse.getId());
        assertEquals(recipe.getUserId(), recipeResponse.getUserId());
        assertArrayEquals(recipe.getIngredients(), recipeResponse.getIngredients());
        assertEquals(recipe.getDescription(), recipeResponse.getDescription());
        assertEquals(recipe.getDifficulty(), recipeResponse.getDifficulty());
        assertEquals(recipe.getTime(), recipeResponse.getTime());
        assertArrayEquals(recipe.getImages(), recipeResponse.getImages());
    }

    /**
     * Tests if RecipeRequest converted to Recipe is equal to original RecipeRequest.
     */
    @Test
    void convertFromRequest() {
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setTitle("Gebrannte Mandeln");
        recipeRequest.setIngredients(new Ingredient[]{
                new Ingredient(500, "Gramm", "Mandeln, geschält"),
                new Ingredient(200, "Gramm", "Zucker")
        });
        recipeRequest.setDescription("TestDescription");
        recipeRequest.setDifficulty(3);
        recipeRequest.setTime(15);
        recipeRequest.setImages(new UUID[]{UUID.fromString(SAMPLE_IMAGE_ID)});

        Recipe recipe = Recipe.fromRequest(recipeRequest);

        assertEquals(recipeRequest.getTitle(), recipe.getTitle());
        assertArrayEquals(recipeRequest.getIngredients(), recipe.getIngredients());
        assertEquals(recipeRequest.getDescription(), recipe.getDescription());
        assertEquals(recipeRequest.getDifficulty(), recipe.getDifficulty());
        assertEquals(recipeRequest.getTime(), recipe.getTime());
        assertEquals(recipeRequest.getImages(), recipe.getImages());
    }
}