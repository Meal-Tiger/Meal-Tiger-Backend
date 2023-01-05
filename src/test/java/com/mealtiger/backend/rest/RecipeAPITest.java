package com.mealtiger.backend.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtiger.backend.BackendApplication;
import com.mealtiger.backend.configuration.Configurator;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    private static final String MOCK_USER_ID = "123e4567-e89b-12d3-a456-42661417400";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private Configurator configurator;

    @BeforeEach
    @AfterEach
    void beforeAfterEach() throws IOException {
        recipeRepository.deleteAll();
        if(Files.exists(Path.of(configurator.getString("Image.imagePath")))) {
            Helper.deleteFile(Path.of(configurator.getString("Image.imagePath")));
        }
    }

    // POSITIVE TESTS

    // GET TESTS

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
                        15,
                        new UUID[]{UUID.randomUUID()}
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
                        15,
                        new UUID[]{UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()}
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
        recipeRepository.saveAll((Arrays.stream(testRecipes).map(Recipe::fromDTO).toList()));
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
                        15,
                        new UUID[]{UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()}
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
                        15,
                        new UUID[]{UUID.randomUUID(), UUID.randomUUID()}
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
                        15,
                        new UUID[]{UUID.randomUUID()}
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

    // POST TESTS

    /**
     * Tests posting a recipe.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void postRecipeTest() throws Exception {
        Recipe testRecipe = new Recipe(
                "Gebrannte Mandeln",
                MOCK_USER_ID,
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                2.8,
                4.3,
                15,
                new UUID[]{}
        );

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, recipeRepository.findAll().size());
        assertEquals(testRecipe, recipeRepository.findAll().get(0));

    }

    /**
     * Tests posting a recipe with an image.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void postRecipeWithImageTest() throws Exception {
        UUID imageUUID = UUID.fromString("af7c1fe6-d669-414e-b066-e9733f0de7a8");

        Files.createDirectories(Path.of(configurator.getString("Image.imagePath"), imageUUID.toString()));

        Recipe testRecipe = new Recipe(
                "Gebrannte Mandeln",
                MOCK_USER_ID,
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                2.8,
                4.3,
                15,
                new UUID[]{imageUUID}
        );

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, recipeRepository.findAll().size());
        assertEquals(testRecipe, recipeRepository.findAll().get(0));
    }

    /**
     * Tests posting a recipe with an id given - it should be overridden.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void postRecipeWithIDTest() throws Exception {
        Recipe testRecipe = new Recipe(
                "Gebrannte Mandeln",
                MOCK_USER_ID,
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                2.8,
                4.3,
                15,
                new UUID[]{}
        );

        testRecipe.setId("1234567890");

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, recipeRepository.findAll().size());
        assertEquals(testRecipe, recipeRepository.findAll().get(0));
        assertNotEquals(testRecipe.getId(), recipeRepository.findAll().get(0).getId());
    }

    // PUT TESTS

    /**
     * Testing replacing a recipe
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void putRecipeTest() throws Exception {
        Recipe testRecipe = new Recipe(
                "Gebrannte Mandeln",
                MOCK_USER_ID,
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                5,
                15,
                new UUID[]{}
        );

        recipeRepository.save(testRecipe);

        String id = recipeRepository.findAll().get(0).getId();

        testRecipe = new Recipe(
                "Toast Hawaii",
                MOCK_USER_ID,
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Schinken"),
                        new Ingredient(10, "Scheiben", "Toastbrot"),
                        new Ingredient(10, "Scheiben", "Ananas"),
                        new Ingredient(10, "Scheiben", "Schmelzkäse")
                },
                "TestDescription",
                1,
                4,
                30,
                new UUID[]{}
        );

        mvc.perform(put("/recipes/" + id)
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(testRecipe, recipeRepository.findAll().get(0));
    }

    // DELETE TESTS

    /**
     * Testing deleting a recipe
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void deleteRecipeTest() throws Exception {
        Recipe testRecipe = new Recipe(
                "Gebrannte Mandeln",
                MOCK_USER_ID,
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                5,
                15,
                new UUID[]{}
        );

        recipeRepository.save(testRecipe);

        String id = recipeRepository.findAll().get(0).getId();

        assertFalse(recipeRepository.findAll().isEmpty());

        mvc.perform(delete("/recipes/" + id))
                .andExpect(status().isOk());

        assertTrue(recipeRepository.findAll().isEmpty());
    }

    // NEGATIVE TESTS

    // GET TESTS

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

    // POST TESTS

    /**
     * Testing a 400 error on a wrong recipe object.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
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

        // Non-existent image

        testRecipe = new Recipe(
                "Gebrannte Mandeln",
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                5,
                0,
                new UUID[]{UUID.randomUUID()}
        );

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertTrue(recipeRepository.findAll().isEmpty());
    }

    // PUT TESTS

    /**
     * Testing a 404 error on a wrong ID.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
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
     * Testing a 403 error on a wrong user id.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void negative_403_putTest() throws Exception {
        Recipe testRecipe = new Recipe(
                "Gebrannte Mandeln",
                "22ec6016-8b9b-11ed-a1eb-0242ac120002",
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                5,
                15,
                new UUID[]{}
        );

        recipeRepository.save(testRecipe);

        String id = recipeRepository.findAll().get(0).getId();

        mvc.perform(put("/recipes/" + id)
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // DELETE TESTS

    /**
     * Testing a 404 error on a wrong ID.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void negative_404_deleteTest() throws Exception {
        mvc.perform(delete("/recipes/someRandomID")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Testing a 403 error on a wrong user id.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void negative_403_deleteTest() throws Exception {
        Recipe testRecipe = new Recipe(
                "Gebrannte Mandeln",
                "22ec6016-8b9b-11ed-a1eb-0242ac120002",
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                5,
                15,
                new UUID[]{}
        );

        recipeRepository.save(testRecipe);

        String id = recipeRepository.findAll().get(0).getId();

        mvc.perform(delete("/recipes/" + id)
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}