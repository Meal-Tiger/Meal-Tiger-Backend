package com.mealtiger.backend.rest.controller;

import com.mealtiger.backend.database.model.recipe.Ingredient;
import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.database.model.recipe.RecipeDTO;
import com.mealtiger.backend.database.repository.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class RecipeControllerTest {

    private static final String SAMPLE_USER_ID = "123e4567-e89b-12d3-a456-42661417400";

    private static final RecipeDTO SAMPLE_RECIPE_DTO = new RecipeDTO();

    private static final Recipe SAMPLE_RECIPE = new Recipe(
            "Test recipe title",
            SAMPLE_USER_ID,
            new Ingredient[]{
                    new Ingredient(10, "Units", "Very different ingredient"),
                    new Ingredient(25, "Units", "Other test ingredient")
            },
            "Test description",
            1.4,
            5,
            10,
            new UUID[]{}
    );

    @MockBean
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeController recipeController;

    /**
     * This sets up the SAMPLE_RECIPE and SAMPLE_RECIPE_DTO examples.
     */
    @BeforeAll
    static void setup() {
        SAMPLE_RECIPE.setId("TestId");
        SAMPLE_RECIPE_DTO.setId("TestId");
        SAMPLE_RECIPE_DTO.setTitle("Test title");
        SAMPLE_RECIPE_DTO.setUserId(SAMPLE_USER_ID);
        SAMPLE_RECIPE_DTO.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Test ingredient"),
                new Ingredient(25, "Units", "Other test ingredient")
        });
        SAMPLE_RECIPE_DTO.setDescription("Test description");
        SAMPLE_RECIPE_DTO.setDifficulty(1.2);
        SAMPLE_RECIPE_DTO.setRating(5);
        SAMPLE_RECIPE_DTO.setTime(10);
        SAMPLE_RECIPE_DTO.setImages(new UUID[]{});
    }

    @BeforeEach
    @AfterEach
    void beforeAfterEach() {
        recipeRepository.deleteAll();
    }

    /**
     * This tests the getRecipePage method in RecipeController.
     */
    @Test
    void getRecipePageTest() {
        Pageable paging = PageRequest.of(1, 5, Sort.by(Sort.Direction.DESC, "title"));

        recipeController.getRecipePage(1, 5, "title");

        verify(recipeRepository).findAll(paging);
    }

    /**
     * This tests the getRecipePageByTitleQuery method in RecipeController.
     */
    @Test
    void getRecipePageByTitleQueryTest() {
        Pageable paging = PageRequest.of(1, 5, Sort.by(Sort.Direction.DESC, "title"));

        recipeController.getRecipePageByTitleQuery(1, 5, "title", "query");

        verify(recipeRepository, never()).findAll(paging);
        verify(recipeRepository).findRecipesByTitleContainingIgnoreCase("query", paging);
    }

    /**
     * This tests the saveRecipe method in RecipeController.
     */
    @Test
    void saveRecipeTest() {
        recipeController.saveRecipe(SAMPLE_RECIPE_DTO);
        verify(recipeRepository).save(Recipe.fromDTO(SAMPLE_RECIPE_DTO));
    }

    /**
     * This tests the getRecipe method in RecipeController.
     */
    @Test
    void getRecipeTest() {
        recipeController.getRecipe("TestId");
        verify(recipeRepository).findById("TestId");
    }

    /**
     * This tests the replaceRecipe method in RecipeController.
     */
    @Test
    void replaceRecipeTest() {
        Optional<Recipe> mockOptional = Optional.of(SAMPLE_RECIPE);
        when(recipeRepository.findById("TestId")).thenReturn(mockOptional);

        recipeController.replaceRecipe("TestId", SAMPLE_RECIPE_DTO);

        verify(recipeRepository).findById("TestId");
        verify(recipeRepository).deleteById("TestId");
        verify(recipeRepository).save(any());
    }

    /**
     * This tests the isUserRecipeOwner method in RecipeController.
     */
    @Test
    void isUserRecipeOwnerTest() {
        when(recipeRepository.findById("TestId")).thenReturn(Optional.of(SAMPLE_RECIPE));

        assertTrue(recipeController.isUserRecipeOwner("TestId", SAMPLE_USER_ID));
        assertFalse(recipeController.isUserRecipeOwner("TestId", "SomeOtherId"));
    }

    /**
     * This tests the doesRecipeExist method in RecipeController.
     */
    @Test
    void doesRecipeExistTest() {
        when(recipeRepository.existsById("A")).thenReturn(true);
        when(recipeRepository.existsById("B")).thenReturn(false);

        assertTrue(recipeController.doesRecipeExist("A"));
        assertFalse(recipeController.doesRecipeExist("B"));
    }

    /**
     * This tests the deleteRecipe method by verifying whether the correct methods on RecipeRepository are called.
     */
    @Test
    void deleteRecipeTest() {
        when(recipeRepository.existsById("A")).thenReturn(true);
        assertTrue(recipeController.deleteRecipe("A"));
        verify(recipeRepository).deleteById("A");

        when(recipeRepository.existsById("B")).thenReturn(false);
        assertFalse(recipeController.deleteRecipe("B"));
        verify(recipeRepository, never()).deleteById("B");
    }

    /**
     * This tests the checkValidity method in RecipeController.
     */
    @Test
    void checkValidityTest() {
        assertTrue(recipeController.checkValidity(SAMPLE_RECIPE_DTO));

        // INCORRECT RATING

        RecipeDTO incorrectRatingTooHigh = new RecipeDTO();
        incorrectRatingTooHigh.setId("TestId");
        incorrectRatingTooHigh.setTitle("Test title");
        incorrectRatingTooHigh.setUserId(SAMPLE_USER_ID);
        incorrectRatingTooHigh.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Test ingredient"),
                new Ingredient(25, "Units", "Other test ingredient")
        });
        incorrectRatingTooHigh.setDescription("Test description");
        incorrectRatingTooHigh.setDifficulty(1.2);
        incorrectRatingTooHigh.setRating(6);
        incorrectRatingTooHigh.setTime(10);
        incorrectRatingTooHigh.setImages(new UUID[]{});

        assertFalse(recipeController.checkValidity(incorrectRatingTooHigh));

        RecipeDTO incorrectRatingTooLow = new RecipeDTO();
        incorrectRatingTooLow.setId("TestId");
        incorrectRatingTooLow.setTitle("Test title");
        incorrectRatingTooLow.setUserId(SAMPLE_USER_ID);
        incorrectRatingTooLow.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Test ingredient"),
                new Ingredient(25, "Units", "Other test ingredient")
        });
        incorrectRatingTooLow.setDescription("Test description");
        incorrectRatingTooLow.setDifficulty(1.2);
        incorrectRatingTooLow.setRating(0);
        incorrectRatingTooLow.setTime(10);
        incorrectRatingTooLow.setImages(new UUID[]{});

        assertFalse(recipeController.checkValidity(incorrectRatingTooLow));

        // INCORRECT DIFFICULTY

        RecipeDTO incorrectDifficultyTooHigh = new RecipeDTO();
        incorrectDifficultyTooHigh.setId("TestId");
        incorrectDifficultyTooHigh.setTitle("Test title");
        incorrectDifficultyTooHigh.setUserId(SAMPLE_USER_ID);
        incorrectDifficultyTooHigh.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Test ingredient"),
                new Ingredient(25, "Units", "Other test ingredient")
        });
        incorrectDifficultyTooHigh.setDescription("Test description");
        incorrectDifficultyTooHigh.setDifficulty(3.1);
        incorrectDifficultyTooHigh.setRating(5);
        incorrectDifficultyTooHigh.setTime(10);
        incorrectDifficultyTooHigh.setImages(new UUID[]{});

        assertFalse(recipeController.checkValidity(incorrectDifficultyTooHigh));

        RecipeDTO incorrectDifficultyTooLow = new RecipeDTO();
        incorrectDifficultyTooLow.setId("TestId");
        incorrectDifficultyTooLow.setTitle("Test title");
        incorrectDifficultyTooLow.setUserId(SAMPLE_USER_ID);
        incorrectDifficultyTooLow.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Test ingredient"),
                new Ingredient(25, "Units", "Other test ingredient")
        });
        incorrectDifficultyTooLow.setDescription("Test description");
        incorrectDifficultyTooLow.setDifficulty(0);
        incorrectDifficultyTooLow.setRating(5);
        incorrectDifficultyTooLow.setTime(10);
        incorrectDifficultyTooLow.setImages(new UUID[]{});

        assertFalse(recipeController.checkValidity(incorrectDifficultyTooLow));

        // NO INGREDIENTS

        RecipeDTO noIngredients = new RecipeDTO();
        noIngredients.setId("TestId");
        noIngredients.setTitle("Test title");
        noIngredients.setUserId(SAMPLE_USER_ID);
        noIngredients.setIngredients(new Ingredient[]{});
        noIngredients.setDescription("Test description");
        noIngredients.setDifficulty(1);
        noIngredients.setRating(5);
        noIngredients.setTime(10);
        noIngredients.setImages(new UUID[]{});

        assertFalse(recipeController.checkValidity(noIngredients));

        // INCORRECT TIME

        RecipeDTO incorrectTime = new RecipeDTO();
        incorrectTime.setId("TestId");
        incorrectTime.setTitle("Test title");
        incorrectTime.setUserId(SAMPLE_USER_ID);
        incorrectTime.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Sample")
        });
        incorrectTime.setDescription("Test description");
        incorrectTime.setDifficulty(1);
        incorrectTime.setRating(5);
        incorrectTime.setTime(-11);
        incorrectTime.setImages(new UUID[]{});

        assertFalse(recipeController.checkValidity(incorrectTime));

        // NO TITLE

        RecipeDTO noTitle = new RecipeDTO();
        noTitle.setId("TestId");
        noTitle.setTitle("");
        noTitle.setUserId(SAMPLE_USER_ID);
        noTitle.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Sample")
        });
        noTitle.setDescription("Test description");
        noTitle.setDifficulty(1);
        noTitle.setRating(5);
        noTitle.setTime(11);
        noTitle.setImages(new UUID[]{});

        assertFalse(recipeController.checkValidity(noTitle));

        // NON-EXISTENT IMAGES

        RecipeDTO nonExistentImages = new RecipeDTO();
        nonExistentImages.setId("TestId");
        nonExistentImages.setTitle("Something");
        nonExistentImages.setUserId(SAMPLE_USER_ID);
        nonExistentImages.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Sample")
        });
        nonExistentImages.setDescription("Test description");
        nonExistentImages.setDifficulty(1);
        nonExistentImages.setRating(5);
        nonExistentImages.setTime(11);
        nonExistentImages.setImages(new UUID[]{
                UUID.fromString(SAMPLE_USER_ID)
        });

        assertFalse(recipeController.checkValidity(nonExistentImages));

        // NULL POINTER EXCEPTION

        RecipeDTO titleNull = new RecipeDTO();
        titleNull.setId("TestId");
        titleNull.setTitle(null);
        titleNull.setUserId(SAMPLE_USER_ID);
        titleNull.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Sample")
        });
        titleNull.setDescription("Test description");
        titleNull.setDifficulty(1);
        titleNull.setRating(5);
        titleNull.setTime(11);
        titleNull.setImages(new UUID[]{});

        assertDoesNotThrow(() -> recipeController.checkValidity(titleNull));
        assertFalse(recipeController.checkValidity(titleNull));

        RecipeDTO ingredientsNull = new RecipeDTO();
        ingredientsNull.setId("TestId");
        ingredientsNull.setTitle("Title");
        ingredientsNull.setUserId(SAMPLE_USER_ID);
        ingredientsNull.setIngredients(null);
        ingredientsNull.setDescription("Test description");
        ingredientsNull.setDifficulty(1);
        ingredientsNull.setRating(5);
        ingredientsNull.setTime(11);
        ingredientsNull.setImages(new UUID[]{});

        assertDoesNotThrow(() -> recipeController.checkValidity(ingredientsNull));
        assertFalse(recipeController.checkValidity(ingredientsNull));

        RecipeDTO descriptionNull = new RecipeDTO();
        descriptionNull.setId("TestId");
        descriptionNull.setTitle("Title");
        descriptionNull.setUserId(SAMPLE_USER_ID);
        descriptionNull.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Sample")
        });
        descriptionNull.setDescription(null);
        descriptionNull.setDifficulty(1);
        descriptionNull.setRating(5);
        descriptionNull.setTime(11);
        descriptionNull.setImages(new UUID[]{});

        assertDoesNotThrow(() -> recipeController.checkValidity(descriptionNull));
        assertFalse(recipeController.checkValidity(descriptionNull));

        RecipeDTO imagesNull = new RecipeDTO();
        imagesNull.setId("TestId");
        imagesNull.setTitle("Title");
        imagesNull.setUserId(SAMPLE_USER_ID);
        imagesNull.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Sample")
        });
        imagesNull.setDescription("Test description");
        imagesNull.setDifficulty(1);
        imagesNull.setRating(5);
        imagesNull.setTime(11);
        imagesNull.setImages(null);

        assertDoesNotThrow(() -> recipeController.checkValidity(imagesNull));
        // Images may be null as they are not required!
        assertTrue(recipeController.checkValidity(imagesNull));
    }

}
