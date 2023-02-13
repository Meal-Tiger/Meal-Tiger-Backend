package com.mealtiger.backend.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtiger.backend.BackendApplication;
import com.mealtiger.backend.SampleSource;
import com.mealtiger.backend.database.model.recipe.Rating;
import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.database.repository.RecipeRepository;
import com.mealtiger.backend.rest.model.rating.RatingRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;

import static com.mealtiger.backend.SampleSource.SAMPLE_RATING_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the RatingAPI
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {BackendApplication.class}
)
@AutoConfigureMockMvc
@Tag("integration")
class RatingAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    @AfterEach
    void beforeAfterEach() {
        recipeRepository.deleteAll();
    }

    /**
     * Integration test for GET on ratings endpoint.
     */
    @Test
    void getRatingsTest() throws Exception {
        recipeRepository.save(SampleSource.getSampleRecipesWithRatings().get(0));
        String testId = recipeRepository.findAll().get(0).getId();

        mvc.perform(get("/recipes/" + testId + "/ratings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ratings").isArray())
                .andExpect(jsonPath("$.totalItems").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.currentPage").value(0));
    }

    /**
     * Integration test for GET on /ratings endpoint
     */
    @Test
    void getRatingTest() throws Exception {
        Rating rating = recipeRepository.save(SampleSource.getSampleRecipesWithRatings().get(0)).getRatings()[0];

        mvc.perform(get("/ratings/" + rating.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ratingValue").value(rating.getRatingValue()))
                .andExpect(jsonPath("$.comment").value(rating.getComment()))
                .andExpect(jsonPath("$.userId").value(rating.getUserId()))
                .andExpect(jsonPath("$.id").value(rating.getId()));
    }

    /**
     * Integration test for GET on average rating endpoint.
     */
    @Test
    void getAverageRatingTest() throws Exception {
        recipeRepository.save(SampleSource.getSampleRecipesWithRatings().get(0));
        String testId = recipeRepository.findAll().get(0).getId();

        mvc.perform(get("/recipes/" + testId + "/rating"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ratingValue").value(3.5));
    }

    /**
     * Integration test for POST on ratings endpoint.
     */
    @Test
    @WithMockUser("6e61cfe3-ba89-4ad0-b3b1-5f7bdbbfa887")
    void postRatingTest() throws Exception {
        recipeRepository.save(SampleSource.getSampleRecipes().get(0));
        String testId = recipeRepository.findAll().get(0).getId();

        RatingRequest request = new RatingRequest();
        request.setRatingValue(5);
        request.setComment("Very good, indeed!");

        String resultJSON = mvc.perform(post("/recipes/" + testId + "/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writer().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(Charset.defaultCharset());

        Rating rating = recipeRepository.findAll().get(0).getRatings()[0];

        assertEquals(request.getRatingValue(), rating.getRatingValue());
        assertEquals(request.getComment(), rating.getComment());
        assertEquals("6e61cfe3-ba89-4ad0-b3b1-5f7bdbbfa887", rating.getUserId());

        JsonNode resultTree = new ObjectMapper().readTree(resultJSON);

        assertEquals(request.getRatingValue(), resultTree.get("ratingValue").intValue());
        assertEquals(request.getComment(), resultTree.get("comment").textValue());
        assertEquals("6e61cfe3-ba89-4ad0-b3b1-5f7bdbbfa887", resultTree.get("userId").textValue());
        assertEquals(rating.getId(), resultTree.get("id").textValue());

    }

    /**
     * Integration test for PUT on ratings endpoint.
     */
    @Test
    @WithMockUser("6e61cfe3-ba89-4ad0-b3b1-5f7bdbbfa887")
    void putRatingTest() throws Exception {
        recipeRepository.save(SampleSource.getSampleRecipes().get(0));
        String testId = recipeRepository.findAll().get(0).getId();

        RatingRequest request = new RatingRequest();
        request.setRatingValue(5);
        request.setComment("Better than I originally thought!");

        String responseJSON = mvc.perform(put("/recipes/" + testId + "/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writer().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(Charset.defaultCharset());

        Rating rating = recipeRepository.findAll().get(0).getRatings()[0];

        assertEquals(request.getRatingValue(), rating.getRatingValue());
        assertEquals(request.getComment(), rating.getComment());
        assertEquals("6e61cfe3-ba89-4ad0-b3b1-5f7bdbbfa887", rating.getUserId());

        JsonNode resultTree = new ObjectMapper().readTree(responseJSON);

        assertEquals(request.getRatingValue(), resultTree.get("ratingValue").intValue());
        assertEquals(request.getComment(), resultTree.get("comment").textValue());
        assertEquals("6e61cfe3-ba89-4ad0-b3b1-5f7bdbbfa887", resultTree.get("userId").textValue());
        assertEquals(rating.getId(), resultTree.get("id").textValue());


        request.setRatingValue(4);
        request.setComment("Next day was like hell - never ever am I going to eat that again. However, was very tasty!");

        mvc.perform(put("/recipes/" + testId + "/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writer().writeValueAsString(request)))
                .andExpect(status().isNoContent());

        rating = recipeRepository.findAll().get(0).getRatings()[0];

        assertEquals(request.getRatingValue(), rating.getRatingValue());
        assertEquals(request.getComment(), rating.getComment());
        assertEquals("6e61cfe3-ba89-4ad0-b3b1-5f7bdbbfa887", rating.getUserId());
    }

    /**
     * Integration test for DELETE on ratings endpoint.
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void deleteRatingTest() throws Exception {
        recipeRepository.save(SampleSource.getSampleRecipesWithRatings().get(0));
        String testId = recipeRepository.findAll().get(0).getId();

        mvc.perform(delete("/recipes/" + testId + "/ratings"))
                .andExpect(status().isNoContent());

        assertEquals(0, recipeRepository.findAll().get(0).getRatings().length);
    }

    // NEGATIVE TESTS

    /**
     * Negative "NOT_FOUND" integration test for GET on ratings endpoint.
     */
    @Test
    void negative_404_getRatingsTest() throws Exception {
        mvc.perform(get("/recipes/someID/ratings"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/recipes/someID/ratings"))
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.status").value(404));
    }

    /**
     * Negative "NOT_FOUND" integration test for GET on average rating endpoint.
     */
    @Test
    void negative_404_getRatingTest() throws Exception {
        mvc.perform(get("/recipes/someID/rating"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/recipes/someID/rating"))
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.status").value(404));
    }

    /**
     * Negative "BAD_REQUEST" integration test for POST on ratings endpoint.
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void negative_400_postRatingsTest() throws Exception {
        recipeRepository.save(SampleSource.getSampleRecipes().get(0));
        String testId = recipeRepository.findAll().get(0).getId();

        RatingRequest request = new RatingRequest();
        request.setRatingValue(6);
        request.setComment("So good, I'd give it 6/5!");

        mvc.perform(post("/recipes/" + testId + "/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writer().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes/" + testId + "/ratings"))
                .andExpect(jsonPath("$.status").value("400"));

        request.setRatingValue(0);
        request.setComment("Worse than I thought was possible! 0/5!");

        mvc.perform(post("/recipes/" + testId + "/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writer().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes/" + testId + "/ratings"))
                .andExpect(jsonPath("$.status").value("400"));
    }

    /**
     * Negative "UNAUTHORIZED" integration test for POST on ratings endpoint.
     */
    @Test
    void negative_401_postRatingsTest() throws Exception {
        recipeRepository.save(SampleSource.getSampleRecipes().get(0));
        String testId = recipeRepository.findAll().get(0).getId();

        RatingRequest request = new RatingRequest();

        for (int rating = 0; rating < 7; rating++) {
            request.setRatingValue(rating);

            mvc.perform(post("/recipes/" + testId + "/ratings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writer().writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }

        request.setComment("Some comment...");

        for (int rating = 0; rating < 7; rating++) {
            request.setRatingValue(rating);

            mvc.perform(post("/recipes/" + testId + "/ratings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writer().writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }
    }

    /**
     * Negative "FORBIDDEN" integration test for POST on ratings endpoint.
     * FORBIDDEN - because it is the user's own recipe.
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void negative_403_postRatingsOwnRecipeTest() throws Exception {
        recipeRepository.save(SampleSource.getSampleRecipes().get(0));
        String testId = recipeRepository.findAll().get(0).getId();

        RatingRequest request = new RatingRequest();

        for (int rating = 1; rating < 6; rating++) {
            request.setRatingValue(rating);

            mvc.perform(post("/recipes/" + testId + "/ratings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writer().writeValueAsString(request)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.error").isString())
                    .andExpect(jsonPath("$.path").value("/recipes/" + testId + "/ratings"))
                    .andExpect(jsonPath("$.status").value("403"));
        }

        request.setComment("Some comment!");

        for (int rating = 1; rating < 6; rating++) {
            request.setRatingValue(rating);

            mvc.perform(post("/recipes/" + testId + "/ratings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writer().writeValueAsString(request)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.error").isString())
                    .andExpect(jsonPath("$.path").value("/recipes/" + testId + "/ratings"))
                    .andExpect(jsonPath("$.status").value("403"));
        }
    }

    /**
     * Negative "FORBIDDEN" integration test for POST on ratings endpoint.
     * FORBIDDEN - because the user has already rated the recipe.
     */
    @Test
    @WithMockUser("e9add05b-0e50-4be9-bc00-f3ff870d51a6")
    void negative_403_postRatingsTwiceTest() throws Exception {
        Recipe recipe = SampleSource.getSampleRecipes().get(0);
        recipe.setRatings(new Rating[]{
                new Rating("Test", 1, "Horrible!", "e9add05b-0e50-4be9-bc00-f3ff870d51a6")
        });

        recipeRepository.save(recipe);
        String testId = recipeRepository.findAll().get(0).getId();

        RatingRequest request = new RatingRequest();

        for (int rating = 1; rating < 6; rating++) {
            request.setRatingValue(rating);

            mvc.perform(post("/recipes/" + testId + "/ratings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writer().writeValueAsString(request)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.error").isString())
                    .andExpect(jsonPath("$.path").value("/recipes/" + testId + "/ratings"))
                    .andExpect(jsonPath("$.status").value("403"));
        }
    }

    /**
     * Negative "NOT_FOUND" integration test for POST on ratings endpoint.
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void negative_404_postRatingsTest() throws Exception {
        RatingRequest request = new RatingRequest();

        for (int rating = 1; rating < 6; rating++) {
            request.setRatingValue(rating);

            mvc.perform(post("/recipes/someID/ratings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writer().writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").isString())
                    .andExpect(jsonPath("$.path").value("/recipes/someID/ratings"))
                    .andExpect(jsonPath("$.status").value("404"));
        }

        request.setComment("Some comment...");

        for (int rating = 1; rating < 6; rating++) {
            request.setRatingValue(rating);

            mvc.perform(post("/recipes/someID/ratings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writer().writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").isString())
                    .andExpect(jsonPath("$.path").value("/recipes/someID/ratings"))
                    .andExpect(jsonPath("$.status").value("404"));
        }
    }

    /**
     * Negative "BAD_REQUEST" integration test for PUT on ratings endpoint.
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void negative_400_putRatingsTest() throws Exception {
        Recipe sampleRecipe = SampleSource.getSampleRecipes().get(0);
        sampleRecipe.setUserId("e9add05b-0e50-4be9-bc00-f3ff870d51a6");
        recipeRepository.save(sampleRecipe);
        String testId = recipeRepository.findAll().get(0).getId();

        RatingRequest request = new RatingRequest();
        request.setRatingValue(6);

        mvc.perform(put("/recipes/" + testId + "/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writer().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes/" + testId + "/ratings"))
                .andExpect(jsonPath("$.status").value("400"));

        request.setRatingValue(0);

        mvc.perform(put("/recipes/" + testId + "/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writer().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes/" + testId + "/ratings"))
                .andExpect(jsonPath("$.status").value("400"));

        request.setComment("Some comment...");

        request.setRatingValue(6);

        mvc.perform(put("/recipes/" + testId + "/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writer().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes/" + testId + "/ratings"))
                .andExpect(jsonPath("$.status").value("400"));

        request.setRatingValue(0);

        mvc.perform(put("/recipes/" + testId + "/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writer().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes/" + testId + "/ratings"))
                .andExpect(jsonPath("$.status").value("400"));

        sampleRecipe.setRatings(new Rating[]{
                new Rating(SAMPLE_RATING_ID, 4, "Some comment...", "123e4567-e89b-12d3-a456-42661417400")
        });
        recipeRepository.save(sampleRecipe);

        request.setRatingValue(6);

        mvc.perform(put("/recipes/" + testId + "/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writer().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes/" + testId + "/ratings"))
                .andExpect(jsonPath("$.status").value("400"));

        request.setRatingValue(0);

        mvc.perform(put("/recipes/" + testId + "/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writer().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes/" + testId + "/ratings"))
                .andExpect(jsonPath("$.status").value("400"));

        request.setComment("Some comment...");

        request.setRatingValue(6);

        mvc.perform(put("/recipes/" + testId + "/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writer().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes/" + testId + "/ratings"))
                .andExpect(jsonPath("$.status").value("400"));

        request.setRatingValue(0);

        mvc.perform(put("/recipes/" + testId + "/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writer().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes/" + testId + "/ratings"))
                .andExpect(jsonPath("$.status").value("400"));
    }

    /**
     * Negative "UNAUTHORIZED" integration test for PUT on ratings endpoint.
     */
    @Test
    void negative_401_putRatingsTest() throws Exception {
        recipeRepository.save(SampleSource.getSampleRecipes().get(0));
        String testId = recipeRepository.findAll().get(0).getId();

        RatingRequest request = new RatingRequest();
        for (int rating = 0; rating < 7; rating++) {
            request.setRatingValue(rating);

            mvc.perform(put("/recipes/" + testId + "/ratings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writer().writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }

        request.setComment("Some comment...");

        for (int rating = 0; rating < 7; rating++) {
            request.setRatingValue(rating);

            mvc.perform(put("/recipes/" + testId + "/ratings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writer().writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }
    }

    /**
     * Negative "FORBIDDEN" integration test for PUT on ratings endpoint.
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void negative_403_putRatingsTest() throws Exception {
        recipeRepository.save(SampleSource.getSampleRecipes().get(0));
        String testId = recipeRepository.findAll().get(0).getId();

        RatingRequest request = new RatingRequest();

        for (int rating = 1; rating < 6; rating++) {
            request.setRatingValue(rating);

            mvc.perform(put("/recipes/" + testId + "/ratings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writer().writeValueAsString(request)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.error").isString())
                    .andExpect(jsonPath("$.path").value("/recipes/" + testId + "/ratings"))
                    .andExpect(jsonPath("$.status").value("403"));
        }

        request.setComment("Some comment!");

        for (int rating = 1; rating < 6; rating++) {
            request.setRatingValue(rating);

            mvc.perform(put("/recipes/" + testId + "/ratings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writer().writeValueAsString(request)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.error").isString())
                    .andExpect(jsonPath("$.path").value("/recipes/" + testId + "/ratings"))
                    .andExpect(jsonPath("$.status").value("403"));
        }
    }

    /**
     * Negative "NOT_FOUND" integration test for PUT on ratings endpoint.
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void negative_404_putRatingsTest() throws Exception {
        RatingRequest request = new RatingRequest();
        for (int rating = 1; rating < 6; rating++) {
            request.setRatingValue(rating);

            mvc.perform(put("/recipes/someID/ratings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writer().writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").isString())
                    .andExpect(jsonPath("$.path").value("/recipes/someID/ratings"))
                    .andExpect(jsonPath("$.status").value("404"));
        }

        request.setComment("Some comment...");

        for (int rating = 1; rating < 6; rating++) {
            request.setRatingValue(rating);

            mvc.perform(put("/recipes/someID/ratings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writer().writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").isString())
                    .andExpect(jsonPath("$.path").value("/recipes/someID/ratings"))
                    .andExpect(jsonPath("$.status").value("404"));
        }
    }

    /**
     * Negative "UNAUTHORIZED" integration test for DELETE on ratings endpoint.
     */
    @Test
    void negative_401_deleteRatingsTest() throws Exception {
        recipeRepository.save(SampleSource.getSampleRecipes().get(0));
        String testId = recipeRepository.findAll().get(0).getId();

        mvc.perform(delete("/recipes/" + testId + "/ratings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Negative "NOT_FOUND" integration test for DELETE on ratings endpoint.
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void negative_404_deleteRatingsTest() throws Exception {
        mvc.perform(delete("/recipes/someID/ratings"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes/someID/ratings"))
                .andExpect(jsonPath("$.status").value("404"));

        recipeRepository.save(SampleSource.getSampleRecipes().get(0));
        String testId = recipeRepository.findAll().get(0).getId();

        mvc.perform(delete("/recipes/" + testId + "/ratings"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/recipes/" + testId + "/ratings"))
                .andExpect(jsonPath("$.status").value("404"));
    }

}
