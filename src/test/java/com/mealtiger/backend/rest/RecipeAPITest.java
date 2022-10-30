package com.mealtiger.backend.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtiger.backend.BackendApplication;
import com.mealtiger.backend.database.model.recipe.Ingredient;
import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.database.model.recipe.Time;
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
                        new Time(15, "Minuten")
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
                        new Time(15, "Minuten")
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
                        new Time(30, "Minuten")
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
                new Time(15, "Minuten")
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
                new Time(15, "Minuten")
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
                new Time(30, "Minuten")
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
                new Time(15, "Minuten")
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
                new Time(15, "Minuten")
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
                new Time(15, "Minuten")
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
                new Time(15, "Minuten")
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
                new Time(15, "Minuten")
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
                new Time(15, "Minuten")
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
                new Time(15, "Minuten")
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
                new Time(15, "Minuten")
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
                new Time(15, "Minuten")
        );

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertTrue(recipeRepository.findAll().isEmpty());

        // Wrong time
        // Maximum equal to 0 when unit is set

        testRecipe = new Recipe(
                "Gebrannte Mandeln",
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                5,
                new Time(15, "Minuten", 0, "Some unit")
        );

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertTrue(recipeRepository.findAll().isEmpty());

        // Minimum is 0

        testRecipe = new Recipe(
                "Gebrannte Mandeln",
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                5,
                new Time(0, "Minuten")
        );

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertTrue(recipeRepository.findAll().isEmpty());

        // Minimum unit is null

        testRecipe = new Recipe(
                "Gebrannte Mandeln",
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                5,
                new Time(15, null)
        );

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertTrue(recipeRepository.findAll().isEmpty());
    }

}