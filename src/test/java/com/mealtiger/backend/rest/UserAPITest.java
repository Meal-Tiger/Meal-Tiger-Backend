package com.mealtiger.backend.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtiger.backend.BackendApplication;
import com.mealtiger.backend.SampleSource;
import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.database.model.image_metadata.ImageMetadata;
import com.mealtiger.backend.database.model.recipe.Ingredient;
import com.mealtiger.backend.database.model.recipe.Rating;
import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.database.model.user.UserMetadata;
import com.mealtiger.backend.database.repository.ImageMetadataRepository;
import com.mealtiger.backend.database.repository.RecipeRepository;
import com.mealtiger.backend.database.repository.UserMetadataRepository;
import com.mealtiger.backend.rest.model.user.UserInformationRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static com.mealtiger.backend.SampleSource.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for UserAPI
 * Attention mongoDB is needed!
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {BackendApplication.class}
)
@AutoConfigureMockMvc
@Tag("integration")
class UserAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ImageMetadataRepository imageMetadataRepository;

    @Autowired
    private UserMetadataRepository userMetadataRepository;

    @Autowired
    private Configurator configurator;

    @BeforeEach
    @AfterEach
    void beforeAfterEach() throws IOException {
        recipeRepository.deleteAll();
        userMetadataRepository.deleteAll();
        imageMetadataRepository.deleteAll();
        if(Files.exists(Path.of(configurator.getString("Image.imagePath")))) {
            Helper.deleteFile(Path.of(configurator.getString("Image.imagePath")));
        }
    }

    /**
     * Tests GET request on "/user"
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void getOwnUserInformationTest() throws Exception {
        userMetadataRepository.save(new UserMetadata(SAMPLE_USER_ID, "SampleUsername", null));

        mvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(SAMPLE_USER_ID))
                .andExpect(jsonPath("$.username").value("SampleUsername"))
                .andExpect(jsonPath("$.profilePictureId").isEmpty());

        userMetadataRepository.deleteAll();
        userMetadataRepository.save(new UserMetadata(SAMPLE_USER_ID, "SampleUsername", SAMPLE_IMAGE_ID));
        Files.createDirectories(Path.of(configurator.getString("Image.imagePath"), SAMPLE_IMAGE_ID));

        mvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(SAMPLE_USER_ID))
                .andExpect(jsonPath("$.username").value("SampleUsername"))
                .andExpect(jsonPath("$.profilePictureId").value(SAMPLE_IMAGE_ID));
    }

    /**
     * Tests POST request on "/user"
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void postUserInformationTest() throws Exception {
        Files.createDirectories(Path.of(configurator.getString("Image.imagePath"), SAMPLE_IMAGE_ID));
        UserInformationRequest userInformationRequest = new UserInformationRequest("SampleUsername", null);

        mvc.perform(post("/user")
                        .content(new ObjectMapper().writer().writeValueAsString(userInformationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(SAMPLE_USER_ID))
                .andExpect(jsonPath("$.username").value("SampleUsername"))
                .andExpect(jsonPath("$.profilePictureId").isEmpty());

        assertTrue(userMetadataRepository.existsById(SAMPLE_USER_ID));
        assertEquals(userInformationRequest.getUsername(), userMetadataRepository.findById(SAMPLE_USER_ID).orElseThrow().getUsername());
        assertEquals(userInformationRequest.getProfilePictureId(), userMetadataRepository.findById(SAMPLE_USER_ID).orElseThrow().getProfilePictureId());
        userMetadataRepository.deleteAll();

        userInformationRequest = new UserInformationRequest("SampleUsername", "");

        mvc.perform(post("/user")
                        .content(new ObjectMapper().writer().writeValueAsString(userInformationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(SAMPLE_USER_ID))
                .andExpect(jsonPath("$.username").value("SampleUsername"))
                .andExpect(jsonPath("$.profilePictureId").isEmpty());

        assertTrue(userMetadataRepository.existsById(SAMPLE_USER_ID));
        assertEquals(userInformationRequest.getUsername(), userMetadataRepository.findById(SAMPLE_USER_ID).orElseThrow().getUsername());
        assertEquals(userInformationRequest.getProfilePictureId(), userMetadataRepository.findById(SAMPLE_USER_ID).orElseThrow().getProfilePictureId());
        userMetadataRepository.deleteAll();

        userInformationRequest = new UserInformationRequest("SampleUsername", SAMPLE_IMAGE_ID);

        mvc.perform(post("/user")
                        .content(new ObjectMapper().writer().writeValueAsString(userInformationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(SAMPLE_USER_ID))
                .andExpect(jsonPath("$.username").value("SampleUsername"))
                .andExpect(jsonPath("$.profilePictureId").value(SAMPLE_IMAGE_ID));

        assertTrue(userMetadataRepository.existsById(SAMPLE_USER_ID));
        assertEquals(userInformationRequest.getUsername(), userMetadataRepository.findById(SAMPLE_USER_ID).orElseThrow().getUsername());
        assertEquals(userInformationRequest.getProfilePictureId(), userMetadataRepository.findById(SAMPLE_USER_ID).orElseThrow().getProfilePictureId());

    }

    /**
     * Tests PUT request on "/user"
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void putUserInformationTest() throws Exception {
        Files.createDirectories(Path.of(configurator.getString("Image.imagePath"), SAMPLE_IMAGE_ID));
        UserInformationRequest userInformationRequest = new UserInformationRequest("SampleUsername", null);

        mvc.perform(put("/user")
                        .content(new ObjectMapper().writer().writeValueAsString(userInformationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(SAMPLE_USER_ID))
                .andExpect(jsonPath("$.username").value("SampleUsername"))
                .andExpect(jsonPath("$.profilePictureId").isEmpty());

        assertTrue(userMetadataRepository.existsById(SAMPLE_USER_ID));
        assertEquals(userInformationRequest.getUsername(), userMetadataRepository.findById(SAMPLE_USER_ID).orElseThrow().getUsername());
        assertEquals(userInformationRequest.getProfilePictureId(), userMetadataRepository.findById(SAMPLE_USER_ID).orElseThrow().getProfilePictureId());
        userMetadataRepository.deleteAll();

        userInformationRequest = new UserInformationRequest("SampleUsername", "");

        mvc.perform(put("/user")
                        .content(new ObjectMapper().writer().writeValueAsString(userInformationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(SAMPLE_USER_ID))
                .andExpect(jsonPath("$.username").value("SampleUsername"))
                .andExpect(jsonPath("$.profilePictureId").isEmpty());

        assertTrue(userMetadataRepository.existsById(SAMPLE_USER_ID));
        assertEquals(userInformationRequest.getUsername(), userMetadataRepository.findById(SAMPLE_USER_ID).orElseThrow().getUsername());
        assertEquals(userInformationRequest.getProfilePictureId(), userMetadataRepository.findById(SAMPLE_USER_ID).orElseThrow().getProfilePictureId());
        userMetadataRepository.deleteAll();

        userInformationRequest = new UserInformationRequest("SampleUsername", SAMPLE_IMAGE_ID);

        mvc.perform(put("/user")
                        .content(new ObjectMapper().writer().writeValueAsString(userInformationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(SAMPLE_USER_ID))
                .andExpect(jsonPath("$.username").value("SampleUsername"))
                .andExpect(jsonPath("$.profilePictureId").value(SAMPLE_IMAGE_ID));

        assertTrue(userMetadataRepository.existsById(SAMPLE_USER_ID));
        assertEquals(userInformationRequest.getUsername(), userMetadataRepository.findById(SAMPLE_USER_ID).orElseThrow().getUsername());
        assertEquals(userInformationRequest.getProfilePictureId(), userMetadataRepository.findById(SAMPLE_USER_ID).orElseThrow().getProfilePictureId());

        userInformationRequest = new UserInformationRequest("SampleUsername", null);

        mvc.perform(put("/user")
                        .content(new ObjectMapper().writer().writeValueAsString(userInformationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(SAMPLE_USER_ID))
                .andExpect(jsonPath("$.username").value("SampleUsername"))
                .andExpect(jsonPath("$.profilePictureId").isEmpty());

        assertTrue(userMetadataRepository.existsById(SAMPLE_USER_ID));
        assertEquals(userInformationRequest.getUsername(), userMetadataRepository.findById(SAMPLE_USER_ID).orElseThrow().getUsername());
        assertEquals(userInformationRequest.getProfilePictureId(), userMetadataRepository.findById(SAMPLE_USER_ID).orElseThrow().getProfilePictureId());

        userInformationRequest = new UserInformationRequest("SampleUsername", "");

        mvc.perform(put("/user")
                        .content(new ObjectMapper().writer().writeValueAsString(userInformationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(SAMPLE_USER_ID))
                .andExpect(jsonPath("$.username").value("SampleUsername"))
                .andExpect(jsonPath("$.profilePictureId").isEmpty());

        assertTrue(userMetadataRepository.existsById(SAMPLE_USER_ID));
        assertEquals(userInformationRequest.getUsername(), userMetadataRepository.findById(SAMPLE_USER_ID).orElseThrow().getUsername());
        assertEquals(userInformationRequest.getProfilePictureId(), userMetadataRepository.findById(SAMPLE_USER_ID).orElseThrow().getProfilePictureId());

    }

    /**
     * Tests GET request on "/user/recipes"
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void getOwnRecipesTest() throws Exception {
        recipeRepository.saveAll(SampleSource.getSampleRecipes(9));
        recipeRepository.save(new Recipe(
                "Gebrannte Mandeln",
                SAMPLE_OTHER_USER_ID,
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                new Rating[]{},
                15,
                new UUID[]{}
        ));

        assertEquals(10, recipeRepository.findAll().size());

        Pageable paging = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "title"));

        // Page 0

        Map<String, Object> expectedAnswer = new HashMap<>();
        expectedAnswer.put("recipes", recipeRepository.findRecipesByUserId(SAMPLE_USER_ID, paging).getContent().stream().map(Recipe::toResponse).toList());
        expectedAnswer.put("totalItems", 9);
        expectedAnswer.put("totalPages", 3);
        expectedAnswer.put("currentPage", 0);

        String testRecipesJSON = new ObjectMapper().writer().writeValueAsString(expectedAnswer);

        System.out.println(testRecipesJSON);

        mvc.perform(get("/user/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testRecipesJSON));

        // Page 1

        paging = PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "title"));

        expectedAnswer.clear();
        expectedAnswer.put("recipes", recipeRepository.findRecipesByUserId(SAMPLE_USER_ID, paging).getContent().stream().map(Recipe::toResponse).toList());
        expectedAnswer.put("totalItems", 9);
        expectedAnswer.put("totalPages", 3);
        expectedAnswer.put("currentPage", 1);

        testRecipesJSON = new ObjectMapper().writer().writeValueAsString(expectedAnswer);

        mvc.perform(get("/user/recipes?page=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testRecipesJSON));

        // Page 2

        paging = PageRequest.of(2, 3, Sort.by(Sort.Direction.DESC, "title"));

        expectedAnswer.clear();
        expectedAnswer.put("recipes", recipeRepository.findRecipesByUserId(SAMPLE_USER_ID, paging).getContent().stream().map(Recipe::toResponse).toList());
        expectedAnswer.put("totalItems", 9);
        expectedAnswer.put("totalPages", 3);
        expectedAnswer.put("currentPage", 2);

        testRecipesJSON = new ObjectMapper().writer().writeValueAsString(expectedAnswer);

        mvc.perform(get("/user/recipes?page=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testRecipesJSON));

    }

    /**
     * Tests GET request on "/user/images"
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void getOwnImagesTest() throws Exception {
        imageMetadataRepository.saveAll(List.of(
                new ImageMetadata(SampleSource.getSampleUUIDs().get(0), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(1), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(2), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(3), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(4), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(5), SAMPLE_OTHER_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(6), SAMPLE_OTHER_USER_ID)
        ));

        List<String> ownImages = Stream.of(
                new ImageMetadata(SampleSource.getSampleUUIDs().get(0), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(1), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(2), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(3), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(4), SAMPLE_USER_ID)
        ).map(ImageMetadata::getId).toList();

        mvc.perform(get("/user/images")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(ownImages)));
    }

    /**
     * Tests GET request on "/user/{userId}"
     */
    @Test
    @WithMockUser("bc2ef248-91a5-4f06-86f1-726b412170ca")
    void getUserInformationTest() throws Exception {
        userMetadataRepository.save(new UserMetadata(SAMPLE_USER_ID, "SampleUsername", null));

        mvc.perform(get("/user/" + SAMPLE_USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(SAMPLE_USER_ID))
                .andExpect(jsonPath("$.username").value("SampleUsername"))
                .andExpect(jsonPath("$.profilePictureId").isEmpty());

        userMetadataRepository.deleteAll();
        userMetadataRepository.save(new UserMetadata(SAMPLE_USER_ID, "SampleUsername", SAMPLE_IMAGE_ID));
        Files.createDirectories(Path.of(configurator.getString("Image.imagePath"), SAMPLE_IMAGE_ID));

        mvc.perform(get("/user/" + SAMPLE_USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(SAMPLE_USER_ID))
                .andExpect(jsonPath("$.username").value("SampleUsername"))
                .andExpect(jsonPath("$.profilePictureId").value(SAMPLE_IMAGE_ID));
    }

    /**
     * Tests GET request on "/user/{userId}/recipes"
     */
    @Test
    void getUserRecipesTest() throws Exception {
        recipeRepository.saveAll(SampleSource.getSampleRecipes(9));
        recipeRepository.save(new Recipe(
                "Gebrannte Mandeln",
                SAMPLE_OTHER_USER_ID,
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                new Rating[]{},
                15,
                new UUID[]{}
        ));

        assertEquals(10, recipeRepository.findAll().size());

        Pageable paging = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "title"));

        // Page 0

        Map<String, Object> expectedAnswer = new HashMap<>();
        expectedAnswer.put("recipes", recipeRepository.findRecipesByUserId(SAMPLE_USER_ID, paging).getContent().stream().map(Recipe::toResponse).toList());
        expectedAnswer.put("totalItems", 9);
        expectedAnswer.put("totalPages", 3);
        expectedAnswer.put("currentPage", 0);

        String testRecipesJSON = new ObjectMapper().writer().writeValueAsString(expectedAnswer);

        System.out.println(testRecipesJSON);

        mvc.perform(get("/user/" + SAMPLE_USER_ID + "/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testRecipesJSON));

        // Page 1

        paging = PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "title"));

        expectedAnswer.clear();
        expectedAnswer.put("recipes", recipeRepository.findRecipesByUserId(SAMPLE_USER_ID, paging).getContent().stream().map(Recipe::toResponse).toList());
        expectedAnswer.put("totalItems", 9);
        expectedAnswer.put("totalPages", 3);
        expectedAnswer.put("currentPage", 1);

        testRecipesJSON = new ObjectMapper().writer().writeValueAsString(expectedAnswer);

        mvc.perform(get("/user/" + SAMPLE_USER_ID + "/recipes?page=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testRecipesJSON));

        // Page 2

        paging = PageRequest.of(2, 3, Sort.by(Sort.Direction.DESC, "title"));

        expectedAnswer.clear();
        expectedAnswer.put("recipes", recipeRepository.findRecipesByUserId(SAMPLE_USER_ID, paging).getContent().stream().map(Recipe::toResponse).toList());
        expectedAnswer.put("totalItems", 9);
        expectedAnswer.put("totalPages", 3);
        expectedAnswer.put("currentPage", 2);

        testRecipesJSON = new ObjectMapper().writer().writeValueAsString(expectedAnswer);

        mvc.perform(get("/user/" + SAMPLE_USER_ID + "/recipes?page=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(testRecipesJSON));

    }

    /**
     * Tests GET request on "/user/{userId}/images"
     */
    @Test
    void getUserImagesTest() throws Exception {
        imageMetadataRepository.saveAll(List.of(
                new ImageMetadata(SampleSource.getSampleUUIDs().get(0), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(1), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(2), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(3), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(4), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(5), SAMPLE_OTHER_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(6), SAMPLE_OTHER_USER_ID)
        ));

        List<String> ownImages = Stream.of(
                new ImageMetadata(SampleSource.getSampleUUIDs().get(0), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(1), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(2), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(3), SAMPLE_USER_ID),
                new ImageMetadata(SampleSource.getSampleUUIDs().get(4), SAMPLE_USER_ID)
        ).map(ImageMetadata::getId).toList();

        mvc.perform(get("/user/" + SAMPLE_USER_ID + "/images")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(ownImages)));
    }

    // NEGATIVE TESTS

    /**
     * Tests unauthorized GET request on "/user"
     */
    @Test
    void negative_401_getOwnUserInformationTest() throws Exception {
        mvc.perform(get("/user"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Tests GET request on "/user" with result NOT_FOUND (404)
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void negative_404_getOwnUserInformationTest() throws Exception {
        mvc.perform(get("/user"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void negative_400_postUserInformationTest() throws Exception {
        // EMPTY USERNAME
        UserInformationRequest userInformationRequest = new UserInformationRequest("", null);

        mvc.perform(post("/user")
                        .content(new ObjectMapper().writer().writeValueAsString(userInformationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/user"))
                .andExpect(jsonPath("$.error").isString());

        // NULL USERNAME
        userInformationRequest = new UserInformationRequest(null, null);

        mvc.perform(post("/user")
                        .content(new ObjectMapper().writer().writeValueAsString(userInformationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/user"))
                .andExpect(jsonPath("$.error").isString());

        // NON-EXISTENT IMAGE
        userInformationRequest = new UserInformationRequest("Sample Username", SAMPLE_IMAGE_ID);

        mvc.perform(post("/user")
                        .content(new ObjectMapper().writer().writeValueAsString(userInformationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/user"))
                .andExpect(jsonPath("$.error").isString());
    }

    /**
     * Tests unauthorized POST request on "/user"
     */
    @Test
    void negative_401_postUserInformationTest() throws Exception {
        mvc.perform(post("/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void negative_400_putUserInformationTest() throws Exception {
        // EMPTY USERNAME
        UserInformationRequest userInformationRequest = new UserInformationRequest("", null);

        mvc.perform(put("/user")
                        .content(new ObjectMapper().writer().writeValueAsString(userInformationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/user"))
                .andExpect(jsonPath("$.error").isString());

        // NULL USERNAME
        userInformationRequest = new UserInformationRequest(null, null);

        mvc.perform(put("/user")
                        .content(new ObjectMapper().writer().writeValueAsString(userInformationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/user"))
                .andExpect(jsonPath("$.error").isString());

        // NON-EXISTENT IMAGE
        userInformationRequest = new UserInformationRequest("Sample Username", SAMPLE_IMAGE_ID);

        mvc.perform(put("/user")
                        .content(new ObjectMapper().writer().writeValueAsString(userInformationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/user"))
                .andExpect(jsonPath("$.error").isString());
    }

    /**
     * Tests unauthorized PUT request on "/user"
     */
    @Test
    void negative_401_putUserInformationTest() throws Exception {
        mvc.perform(put("/user"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Tests unauthorized GET request on "/user/recipes"
     */
    @Test
    void negative_401_getOwnRecipesTest() throws Exception {
        mvc.perform(get("/user/recipes"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Tests GET request on "/user/recipes" with result NOT_FOUND (404)
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void negative_404_getOwnRecipesTest() throws Exception {
        mvc.perform(get("/user/recipes"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/user/recipes"))
                .andExpect(jsonPath("$.error").isString());
    }

    /**
     * Tests unauthorized GET request on "/user/images"
     */
    @Test
    void negative_401_getOwnImagesTest() throws Exception {
        mvc.perform(get("/user/images"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Tests GET request on "/user/images" with result NOT_FOUND (404)
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void negative_404_getOwnImagesTest() throws Exception {
        mvc.perform(get("/user/images"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/user/images"))
                .andExpect(jsonPath("$.error").isString());
    }

    /**
     * Tests GET request on "/user/{userid}" with result NOT_FOUND (404)
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void negative_404_getUserInformationTest() throws Exception {
        mvc.perform(get("/user/" + SAMPLE_OTHER_USER_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/user/" + SAMPLE_OTHER_USER_ID))
                .andExpect(jsonPath("$.error").isString());
    }

    /**
     * Tests GET request on "/user/{userid}/recipes" with result NOT_FOUND (404)
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void negative_404_getUserRecipesTest() throws Exception {
        mvc.perform(get("/user/" + SAMPLE_OTHER_USER_ID + "/recipes"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/user/" + SAMPLE_OTHER_USER_ID + "/recipes"))
                .andExpect(jsonPath("$.error").isString());
    }

    /**
     * Tests GET request on "/user/{userId}/images" with result NOT_FOUND (404)
     */
    @Test
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    void negative_404_getUserImagesTest() throws Exception {
        mvc.perform(get("/user/" + SAMPLE_OTHER_USER_ID + "/images"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/user/" + SAMPLE_OTHER_USER_ID + "/images"))
                .andExpect(jsonPath("$.error").isString());
    }
}
