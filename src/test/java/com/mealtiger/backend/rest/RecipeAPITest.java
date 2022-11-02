package com.mealtiger.backend.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtiger.backend.BackendApplication;
import com.mealtiger.backend.database.model.recipe.Ingredient;
import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.database.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Integration test for RecipeAPI
 * Attention mongoDB is needed!
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {BackendApplication.class}
)
@AutoConfigureMockMvc
public class RecipeAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    void beforeEach() {
        recipeRepository.deleteAll();
    }

    // POSITIVE TESTS

    /**
     * Tests getting all recipes
     */
    @Test
    void getRecipesTest() throws Exception {
        Recipe[] testRecipes = {
                new Recipe(
                        "Gebrannte Mandeln",
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Mandeln, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        5,
                        15
                ),
                new Recipe(
                        "Gebratene Cashewkerne",
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Cashewkerne, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        5,
                        15
                ),
                new Recipe(
                        "Toast Hawaii",
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Schinken"),
                                new Ingredient(10, "Scheiben", "Toastbrot"),
                                new Ingredient(10, "Scheiben", "Ananas"),
                                new Ingredient(10, "Scheiben", "Schmelzkäse")
                        },
                        "TestDescription",
                        1,
                        4,
                        15
                ),
        };

        recipeRepository.saveAll(Arrays.asList(testRecipes));
        String testRecipesJSON = new ObjectMapper().writer().writeValueAsString(recipeRepository.findAll().toArray());

        mvc.perform(get("/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testRecipesJSON));
    }

    /**
     * Tests posting a recipe.
     */
    @Test
    void postRecipeTest() throws Exception {
        Recipe testRecipe = new Recipe(
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

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, recipeRepository.findAll().size());
        assertEquals(testRecipe, recipeRepository.findAll().get(0));

    }

    /**
     * Testing replacing a recipe
     */
    @Test
    void putRecipeTest() throws Exception {
        Recipe testRecipe = new Recipe(
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

        recipeRepository.save(testRecipe);

        String id = recipeRepository.findAll().get(0).getId();

        testRecipe = new Recipe(
                "Toast Hawaii",
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Schinken"),
                        new Ingredient(10, "Scheiben", "Toastbrot"),
                        new Ingredient(10, "Scheiben", "Ananas"),
                        new Ingredient(10, "Scheiben", "Schmelzkäse")
                },
                "TestDescription",
                1,
                4,
                30
        );

        mvc.perform(put("/recipes/" + id)
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(testRecipe, recipeRepository.findAll().get(0));
    }

    /**
     * Testing getting a single recipe
     */
    @Test
    void getSingleRecipeTest() throws Exception {
        Recipe testRecipe = new Recipe(
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

        recipeRepository.save(testRecipe);

        String id = recipeRepository.findAll().get(0).getId();

        testRecipe = recipeRepository.findAll().get(0);

        mvc.perform(get("/recipes/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writer().writeValueAsString(testRecipe)));
    }

    /**
     * Testing deleting a recipe
     */
    @Test
    void deleteRecipeTest() throws Exception {
        Recipe testRecipe = new Recipe(
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

        recipeRepository.save(testRecipe);

        String id = recipeRepository.findAll().get(0).getId();

        assertFalse(recipeRepository.findAll().isEmpty());

        mvc.perform(delete("/recipes/" + id))
                .andExpect(status().isOk());

        assertTrue(recipeRepository.findAll().isEmpty());
    }

    // NEGATIVE TESTS

    /**
     * Testing a 404 error on a wrong ID.
     */
    @Test
    void negative_404_getTest() throws Exception {
        mvc.perform(get("/recipes/someRandomID"))
                .andExpect(status().isNotFound());
    }

    /**
     * Testing a 404 error on a wrong ID.
     */
    @Test
    void negative_404_putTest() throws Exception {
        Recipe testRecipe = new Recipe(
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

        mvc.perform(put("/recipes/someRandomID")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Testing a 400 error on a wrong recipe object.
     */
    @Test
    void negative_400_postTest() throws Exception {
        // Wrong title

        Recipe testRecipe = new Recipe(
                null,
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                5,
                15
        );

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertTrue(recipeRepository.findAll().isEmpty());

        // Wrong ingredients

        testRecipe = new Recipe(
                "Gebrannte Mandeln",
                new Ingredient[0],
                "TestDescription",
                3,
                5,
                15
        );

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertTrue(recipeRepository.findAll().isEmpty());

        // Wrong difficulty
        // Too high

        testRecipe = new Recipe(
                "Gebrannte Mandeln",
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                4,
                5,
                15
        );

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertTrue(recipeRepository.findAll().isEmpty());

        // Too low

        testRecipe = new Recipe(
                "Gebrannte Mandeln",
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                0,
                5,
                15
        );

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertTrue(recipeRepository.findAll().isEmpty());

        // Wrong rating
        // Too high

        testRecipe = new Recipe(
                "Gebrannte Mandeln",
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                6,
                15
        );

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertTrue(recipeRepository.findAll().isEmpty());

        // Too low

        testRecipe = new Recipe(
                "Gebrannte Mandeln",
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                0,
                15
        );

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertTrue(recipeRepository.findAll().isEmpty());

        // Wrong time

        testRecipe = new Recipe(
                "Gebrannte Mandeln",
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                5,
                -1
        );

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertTrue(recipeRepository.findAll().isEmpty());

        // Minimum Time is 0

        testRecipe = new Recipe(
                "Gebrannte Mandeln",
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                5,
                0
        );

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertTrue(recipeRepository.findAll().isEmpty());
    }

}