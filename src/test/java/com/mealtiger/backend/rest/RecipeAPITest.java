package com.mealtiger.backend.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtiger.backend.BackendApplication;
import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.database.model.recipe.Ingredient;
import com.mealtiger.backend.database.model.recipe.Rating;
import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.database.repository.RecipeRepository;
import com.mealtiger.backend.rest.model.recipe.RecipeRequest;
import com.mealtiger.backend.rest.model.recipe.RecipeResponse;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

    private static final String SAMPLE_USER_ID = "123e4567-e89b-12d3-a456-42661417400";

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
        Recipe[] testRecipes = SampleSource.getSampleRecipes(3).toArray(new Recipe[]{});

        recipeRepository.saveAll(Arrays.stream(testRecipes).toList());
        Pageable paging = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "title"));

        Map<String, Object> expectedAnswer = new HashMap<>();
        expectedAnswer.put("recipes", recipeRepository.findAll(paging).getContent().stream().map(Recipe::toResponse).toList());
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
        Recipe[] testRecipes = SampleSource.getSampleRecipes(9).toArray(new Recipe[]{});

        recipeRepository.saveAll(Arrays.asList(testRecipes));

        Pageable paging = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "title"));

        // Page 0

        Map<String, Object> expectedAnswer = new HashMap<>();
        expectedAnswer.put("recipes", recipeRepository.findAll(paging).getContent().stream().map(Recipe::toResponse).toList());
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
        expectedAnswer.put("recipes", recipeRepository.findAll(paging).getContent().stream().map(Recipe::toResponse).toList());
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
        expectedAnswer.put("recipes", recipeRepository.findAll(paging).getContent().stream().map(Recipe::toResponse).toList());
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
        Recipe[] testRecipes = SampleSource.getSampleRecipes(9).toArray(new Recipe[]{});

        recipeRepository.saveAll(Arrays.asList(testRecipes));

        // Size 3

        Pageable paging = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "title"));

        Map<String, Object> expectedAnswer = new HashMap<>();
        expectedAnswer.put("recipes", recipeRepository.findAll(paging).getContent().stream().map(Recipe::toResponse).toList());
        expectedAnswer.put("totalItems", 9);
        expectedAnswer.put("totalPages", 3);
        expectedAnswer.put("currentPage", 0);

        String testRecipesJSON = new ObjectMapper().writer().writeValueAsString(expectedAnswer);

        mvc.perform(get("/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testRecipesJSON));

        // Size 4

        paging = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "title"));

        expectedAnswer.clear();
        expectedAnswer.put("recipes", recipeRepository.findAll(paging).getContent().stream().map(Recipe::toResponse).toList());
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
        expectedAnswer.put("recipes", recipeRepository.findAll(paging).getContent().stream().map(Recipe::toResponse).toList());
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
        expectedAnswer.put("recipes", recipeRepository.findAll(paging).getContent().stream().map(Recipe::toResponse).toList());
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
        Recipe testRecipe = SampleSource.getSampleRecipes(1).get(0);

        recipeRepository.save(testRecipe);

        String id = recipeRepository.findAll().get(0).getId();

        RecipeResponse testRecipeResponse = recipeRepository.findAll().get(0).toResponse();

        mvc.perform(get("/recipes/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writer().writeValueAsString(testRecipeResponse)));
    }

    /**
     * Testing the query parameter
     */

    @Test
    void getQueriedRecipeTest() throws Exception {
        Recipe[] testRecipes = SampleSource.getSampleRecipes(3).toArray(new Recipe[]{});

        recipeRepository.saveAll(Arrays.asList(testRecipes));

        Pageable pageable = PageRequest.of(0, 1);

        Map<String, Object> expectedAnswer = new HashMap<>();
        expectedAnswer.put("recipes", recipeRepository.findRecipesByTitleContaining("Gebrannte Mandeln", pageable).stream().map(Recipe::toResponse).toList());
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
        RecipeRequest testRecipe = new RecipeRequest();
        testRecipe.setTitle("Gebrannte Mandeln");
        testRecipe.setIngredients(
            new Ingredient[]{
                new Ingredient(500, "Gramm", "Mandeln, geschält"),
                new Ingredient(200, "Gramm", "Zucker")
            }
        );
        testRecipe.setDescription("TestDescription");
        testRecipe.setDifficulty(2.8);
        testRecipe.setTime(15);
        testRecipe.setImages(new UUID[]{});

        //SampleSource.getSampleUUIDs().map(UUID::fromString).toList().toArray(new UUID[]{})

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        assertEquals(1, recipeRepository.findAll().size());

        Recipe recipe = recipeRepository.findAll().get(0);

        assertEquals(testRecipe.getTitle(), recipe.getTitle());
        assertEquals(SAMPLE_USER_ID, recipe.getUserId());
        assertArrayEquals(testRecipe.getIngredients(), recipe.getIngredients());
        assertEquals(testRecipe.getDescription(), recipe.getDescription());
        assertEquals(testRecipe.getDifficulty(), recipe.getDifficulty());
        assertArrayEquals(new Rating[]{}, recipe.getRatings());
        assertEquals(testRecipe.getTime(), recipe.getTime());
        assertArrayEquals(testRecipe.getImages(), recipe.getImages());
    }

    /**
     * Tests posting a recipe with an image.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void postRecipeWithImageTest() throws Exception {
        UUID imageUUID = UUID.fromString(SampleSource.getSampleUUIDs().toList().get(0));

        Files.createDirectories(Path.of(configurator.getString("Image.imagePath"), imageUUID.toString()));

        RecipeRequest testRecipe = new RecipeRequest();
        testRecipe.setTitle("Gebrannte Mandeln");
        testRecipe.setIngredients(
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                }
        );
        testRecipe.setDescription("TestDescription");
        testRecipe.setDifficulty(2.8);
        testRecipe.setTime(15);
        testRecipe.setImages(new UUID[]{imageUUID});

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Recipe recipe = recipeRepository.findAll().get(0);

        assertEquals(1, recipeRepository.findAll().size());
        assertEquals(testRecipe.getTitle(), recipe.getTitle());
        assertEquals(SAMPLE_USER_ID, recipe.getUserId());
        assertArrayEquals(testRecipe.getIngredients(), recipe.getIngredients());
        assertEquals(testRecipe.getDescription(), recipe.getDescription());
        assertEquals(testRecipe.getDifficulty(), recipe.getDifficulty());
        assertArrayEquals(new Rating[]{}, recipe.getRatings());
        assertEquals(testRecipe.getTime(), recipe.getTime());
        assertArrayEquals(testRecipe.getImages(), recipe.getImages());
    }

    // PUT TESTS

    /**
     * Testing replacing a recipe
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void putRecipeTest() throws Exception {
        recipeRepository.save(SampleSource.getSampleRecipes().get(0));

        String id = recipeRepository.findAll().get(0).getId();

        RecipeRequest testRecipe = new RecipeRequest();
        testRecipe.setTitle("Toast Hawaii");
        testRecipe.setIngredients(
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Schinken"),
                        new Ingredient(10, "Scheiben", "Toastbrot"),
                        new Ingredient(10, "Scheiben", "Ananas"),
                        new Ingredient(10, "Scheiben", "Schmelzkäse")
                }
        );
        testRecipe.setDescription("TestDescription");
        testRecipe.setDifficulty(1);
        testRecipe.setTime(30);
        testRecipe.setImages(new UUID[]{});

        mvc.perform(put("/recipes/" + id)
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertEquals(1, recipeRepository.findAll().size());

        Recipe recipe = recipeRepository.findAll().get(0);

        assertEquals(testRecipe.getTitle(), recipe.getTitle());
        assertEquals(SAMPLE_USER_ID, recipe.getUserId());
        assertArrayEquals(testRecipe.getIngredients(), recipe.getIngredients());
        assertEquals(testRecipe.getDescription(), recipe.getDescription());
        assertEquals(testRecipe.getDifficulty(), recipe.getDifficulty());
        assertArrayEquals(new Rating[]{}, recipe.getRatings());
        assertEquals(testRecipe.getTime(), recipe.getTime());
        assertArrayEquals(testRecipe.getImages(), recipe.getImages());
    }

    // DELETE TESTS

    /**
     * Testing deleting a recipe
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void deleteRecipeTest() throws Exception {
        Recipe testRecipe = SampleSource.getSampleRecipes().get(0);

        recipeRepository.save(testRecipe);

        String id = recipeRepository.findAll().get(0).getId();

        assertFalse(recipeRepository.findAll().isEmpty());

        mvc.perform(delete("/recipes/" + id))
                .andExpect(status().isNoContent());

        assertTrue(recipeRepository.findAll().isEmpty());
    }

    // NEGATIVE TESTS

    // GET TESTS

    @Test
    void negative_404_getPaginationTest() throws Exception {
        recipeRepository.saveAll(SampleSource.getSampleRecipes(3));

        mvc.perform(get("/recipes?page=15"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes?page=15"));
    }

    /**
     * Testing a 404 error on a wrong ID.
     */
    @Test
    void negative_404_getSingleTest() throws Exception {
        mvc.perform(get("/recipes/someRandomID"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes/someRandomID"));
    }

    // POST TESTS

    /**
     * Testing a 400 error on a wrong recipe object.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void negative_400_postTest() throws Exception {
        // Wrong title

        RecipeRequest testRecipe = new RecipeRequest();
        testRecipe.setTitle(null);
        testRecipe.setIngredients(
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Schinken"),
                        new Ingredient(10, "Scheiben", "Toastbrot"),
                        new Ingredient(10, "Scheiben", "Ananas"),
                        new Ingredient(10, "Scheiben", "Schmelzkäse")
                }
        );
        testRecipe.setDescription("TestDescription");
        testRecipe.setDifficulty(1);
        testRecipe.setTime(30);
        testRecipe.setImages(new UUID[]{});

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes"));

        assertTrue(recipeRepository.findAll().isEmpty());

        // Wrong ingredients

        testRecipe = new RecipeRequest();
        testRecipe.setTitle("Toast Hawaii");
        testRecipe.setIngredients(
                new Ingredient[]{}
        );
        testRecipe.setDescription("TestDescription");
        testRecipe.setDifficulty(1);
        testRecipe.setTime(30);
        testRecipe.setImages(new UUID[]{});

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes"));

        assertTrue(recipeRepository.findAll().isEmpty());

        // Wrong difficulty
        // Too high

        testRecipe = new RecipeRequest();
        testRecipe.setTitle("Toast Hawaii");
        testRecipe.setIngredients(
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Schinken"),
                        new Ingredient(10, "Scheiben", "Toastbrot"),
                        new Ingredient(10, "Scheiben", "Ananas"),
                        new Ingredient(10, "Scheiben", "Schmelzkäse")
                }
        );
        testRecipe.setDescription("TestDescription");
        testRecipe.setDifficulty(3.1);
        testRecipe.setTime(30);
        testRecipe.setImages(new UUID[]{});

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes"));

        assertTrue(recipeRepository.findAll().isEmpty());

        // Too low

        testRecipe = new RecipeRequest();
        testRecipe.setTitle("Toast Hawaii");
        testRecipe.setIngredients(
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Schinken"),
                        new Ingredient(10, "Scheiben", "Toastbrot"),
                        new Ingredient(10, "Scheiben", "Ananas"),
                        new Ingredient(10, "Scheiben", "Schmelzkäse")
                }
        );
        testRecipe.setDescription("TestDescription");
        testRecipe.setDifficulty(0);
        testRecipe.setTime(30);
        testRecipe.setImages(new UUID[]{});

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes"));

        assertTrue(recipeRepository.findAll().isEmpty());

        // Wrong time

        testRecipe = new RecipeRequest();
        testRecipe.setTitle("Toast Hawaii");
        testRecipe.setIngredients(
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Schinken"),
                        new Ingredient(10, "Scheiben", "Toastbrot"),
                        new Ingredient(10, "Scheiben", "Ananas"),
                        new Ingredient(10, "Scheiben", "Schmelzkäse")
                }
        );
        testRecipe.setDescription("TestDescription");
        testRecipe.setDifficulty(1);
        testRecipe.setTime(0);
        testRecipe.setImages(new UUID[]{});

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes"));

        assertTrue(recipeRepository.findAll().isEmpty());

        // Non-existent image

        testRecipe = new RecipeRequest();
        testRecipe.setTitle("Toast Hawaii");
        testRecipe.setIngredients(
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Schinken"),
                        new Ingredient(10, "Scheiben", "Toastbrot"),
                        new Ingredient(10, "Scheiben", "Ananas"),
                        new Ingredient(10, "Scheiben", "Schmelzkäse")
                }
        );
        testRecipe.setDescription("TestDescription");
        testRecipe.setDifficulty(1);
        testRecipe.setTime(0);
        testRecipe.setImages(new UUID[]{UUID.fromString(SampleSource.getSampleUUIDs().toList().get(0))});

        mvc.perform(post("/recipes")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes"));

        assertTrue(recipeRepository.findAll().isEmpty());
    }

    // PUT TESTS

    /**
     * Testing a 404 error on a wrong ID.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void negative_404_putTest() throws Exception {
        RecipeRequest testRecipe = new RecipeRequest();
        testRecipe.setTitle("Toast Hawaii");
        testRecipe.setIngredients(
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Schinken"),
                        new Ingredient(10, "Scheiben", "Toastbrot"),
                        new Ingredient(10, "Scheiben", "Ananas"),
                        new Ingredient(10, "Scheiben", "Schmelzkäse")
                }
        );
        testRecipe.setDescription("TestDescription");
        testRecipe.setDifficulty(1);
        testRecipe.setTime(30);
        testRecipe.setImages(new UUID[]{});

        mvc.perform(put("/recipes/someRandomID")
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes/someRandomID"));
    }

    /**
     * Testing a 403 error on a wrong user id.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void negative_403_putTest() throws Exception {
        Recipe testRecipe = SampleSource.getSampleRecipes().get(0);
        testRecipe.setUserId("fffa5f32-c451-4f06-91e6-2857a8982eb5");

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
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes/someRandomID"));
    }

    /**
     * Testing a 403 error on a wrong user id.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void negative_403_deleteTest() throws Exception {
        Recipe testRecipe = SampleSource.getSampleRecipes().get(0);
        testRecipe.setUserId("22ec6016-8b9b-11ed-a1eb-0242ac120002");

        recipeRepository.save(testRecipe);

        String id = recipeRepository.findAll().get(0).getId();

        mvc.perform(delete("/recipes/" + id)
                        .content(new ObjectMapper().writer().writeValueAsString(testRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}