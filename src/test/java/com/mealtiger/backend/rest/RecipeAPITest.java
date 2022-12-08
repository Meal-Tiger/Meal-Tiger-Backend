package com.mealtiger.backend.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtiger.backend.BackendApplication;
import com.mealtiger.backend.database.model.recipe.Ingredient;
import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.database.model.recipe.RecipeDTO;
import com.mealtiger.backend.database.repository.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
class RecipeAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    @AfterEach
    void beforeAfterEach() {
        recipeRepository.deleteAll();
    }

    // POSITIVE TESTS

    /**
     * Tests getting recipes with default paging parameters.
     */
    @Test
    void getRecipesDefaultTest() throws Exception {
        RecipeDTO[] testRecipes = {
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
                ).toDTO(),
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
                ).toDTO(),
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
                ).toDTO(),
        };
        recipeRepository.saveAll((Arrays.stream(testRecipes).map(DTO -> Recipe.fromDTO(DTO)).toList()));
        Pageable paging = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "title"));

        Map<String, Object> expectedAnswer = new HashMap<>();
        expectedAnswer.put("recipes", recipeRepository.findAll(paging).getContent());
        expectedAnswer.put("totalItems", 3);
        expectedAnswer.put("totalPages", 1);
        expectedAnswer.put("currentPage", 0);

        String testRecipesJSON = new ObjectMapper().writer().writeValueAsString(expectedAnswer);

        mvc.perform(get("/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testRecipesJSON));
    }

    /**
     * Tests getting recipes with multiple pages.
     */
    @Test
    void getRecipesMultiplePagesTest() throws Exception {
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

        Pageable paging = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "title"));

        // Page 0

        Map<String, Object> expectedAnswer = new HashMap<>();
        expectedAnswer.put("recipes", recipeRepository.findAll(paging).getContent());
        expectedAnswer.put("totalItems", 9);
        expectedAnswer.put("totalPages", 3);
        expectedAnswer.put("currentPage", 0);

        String testRecipesJSON = new ObjectMapper().writer().writeValueAsString(expectedAnswer);

        System.out.println(testRecipesJSON);

        mvc.perform(get("/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testRecipesJSON));

        // Page 1

        paging = PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "title"));

        expectedAnswer.clear();
        expectedAnswer.put("recipes", recipeRepository.findAll(paging).getContent());
        expectedAnswer.put("totalItems", 9);
        expectedAnswer.put("totalPages", 3);
        expectedAnswer.put("currentPage", 1);

        testRecipesJSON = new ObjectMapper().writer().writeValueAsString(expectedAnswer);

        mvc.perform(get("/recipes?page=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testRecipesJSON));

        // Page 2

        paging = PageRequest.of(2, 3, Sort.by(Sort.Direction.DESC, "title"));

        expectedAnswer.clear();
        expectedAnswer.put("recipes", recipeRepository.findAll(paging).getContent());
        expectedAnswer.put("totalItems", 9);
        expectedAnswer.put("totalPages", 3);
        expectedAnswer.put("currentPage", 2);

        testRecipesJSON = new ObjectMapper().writer().writeValueAsString(expectedAnswer);

        mvc.perform(get("/recipes?page=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testRecipesJSON));
    }

    /**
     * Tests getting recipes with multiple pages and variable sizes.
     */
    @Test
    void getRecipesMultiplePagesVariableSizeTest() throws Exception {
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

        // Size 3

        Pageable paging = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "title"));

        Map<String, Object> expectedAnswer = new HashMap<>();
        expectedAnswer.put("recipes", recipeRepository.findAll(paging).getContent());
        expectedAnswer.put("totalItems", 9);
        expectedAnswer.put("totalPages", 3);
        expectedAnswer.put("currentPage", 0);

        String testRecipesJSON = new ObjectMapper().writer().writeValueAsString(expectedAnswer);

        System.out.println(testRecipesJSON);

        mvc.perform(get("/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testRecipesJSON));

        // Size 4

        paging = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "title"));

        expectedAnswer.clear();
        expectedAnswer.put("recipes", recipeRepository.findAll(paging).getContent());
        expectedAnswer.put("totalItems", 9);
        expectedAnswer.put("totalPages", 3);
        expectedAnswer.put("currentPage", 0);

        testRecipesJSON = new ObjectMapper().writer().writeValueAsString(expectedAnswer);

        mvc.perform(get("/recipes?size=4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testRecipesJSON));

        // Size 9

        paging = PageRequest.of(0, 9, Sort.by(Sort.Direction.DESC, "title"));

        expectedAnswer.clear();
        expectedAnswer.put("recipes", recipeRepository.findAll(paging).getContent());
        expectedAnswer.put("totalItems", 9);
        expectedAnswer.put("totalPages", 1);
        expectedAnswer.put("currentPage", 0);

        testRecipesJSON = new ObjectMapper().writer().writeValueAsString(expectedAnswer);

        mvc.perform(get("/recipes?size=9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testRecipesJSON));

        // Size > 9

        paging = PageRequest.of(0, 9, Sort.by(Sort.Direction.DESC, "title"));

        expectedAnswer.clear();
        expectedAnswer.put("recipes", recipeRepository.findAll(paging).getContent());
        expectedAnswer.put("totalItems", 9);
        expectedAnswer.put("totalPages", 1);
        expectedAnswer.put("currentPage", 0);

        testRecipesJSON = new ObjectMapper().writer().writeValueAsString(expectedAnswer);

        mvc.perform(get("/recipes?size=5000")
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
                2.8,
                4.3,
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

    /**
     * Testing the query parameter
     */

    @Test
    void getQueriedRecipeTest() throws Exception {
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
                )
        };

        recipeRepository.saveAll(Arrays.asList(testRecipes));

        Pageable pageable = PageRequest.of(0, 1);

        Map<String, Object> expectedAnswer = new HashMap<>();
        expectedAnswer.put("recipes", recipeRepository.findRecipesByTitleContaining("Gebrannte Mandeln", pageable));
        expectedAnswer.put("totalItems", 1);
        expectedAnswer.put("totalPages", 1);
        expectedAnswer.put("currentPage", 0);

        String testRecipesJSON = new ObjectMapper().writer().writeValueAsString(expectedAnswer);

        mvc.perform(get("/recipes?q=Gebrannte Mandeln")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testRecipesJSON));

    }

    // NEGATIVE TESTS

    @Test
    void negative_404_getPaginationTest() throws Exception {
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

        mvc.perform(get("/recipes?page=15"))
                .andExpect(status().isNotFound());
    }

    /**
     * Testing a 404 error on a wrong ID.
     */
    @Test
    void negative_404_getSingleTest() throws Exception {
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
                3.1,
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
                5.2,
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