package com.mealtiger.backend.database.model.recipe;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecipeDTOTest {

    private static final String SAMPLE_USER_ID = "123e4567-e89b-12d3-a456-42661417400";

    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
    }

    /**
     * This tests the validation of RecipeDTO.
     */
    @Test
    void validationTest() {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setId("TestId");
        recipeDTO.setTitle("Test title");
        recipeDTO.setUserId(SAMPLE_USER_ID);
        recipeDTO.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Test ingredient"),
                new Ingredient(25, "Units", "Other test ingredient")
        });
        recipeDTO.setDescription("Test description");
        recipeDTO.setDifficulty(1.2);
        recipeDTO.setRating(5);
        recipeDTO.setTime(10);
        recipeDTO.setImages(new UUID[]{});

        assertTrue(validator.validate(recipeDTO).isEmpty());

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

        assertFalse(validator.validate(incorrectRatingTooHigh).isEmpty());

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

        assertFalse(validator.validate(incorrectRatingTooLow).isEmpty());

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

        assertFalse(validator.validate(incorrectDifficultyTooHigh).isEmpty());

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

        assertFalse(validator.validate(incorrectDifficultyTooLow).isEmpty());

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

        assertFalse(validator.validate(noIngredients).isEmpty());

        // WRONG INGREDIENTS

        RecipeDTO wrongIngredients = new RecipeDTO();
        wrongIngredients.setId("TestId");
        wrongIngredients.setTitle("Test title");
        wrongIngredients.setUserId(SAMPLE_USER_ID);
        wrongIngredients.setIngredients(new Ingredient[]{
                new Ingredient(0, "Units", "wrong ingredient"),
                new Ingredient(-1, "Units", "Other not right ingredient")
        });
        wrongIngredients.setDescription("Test description");
        wrongIngredients.setDifficulty(1);
        wrongIngredients.setRating(5);
        wrongIngredients.setTime(10);
        wrongIngredients.setImages(new UUID[]{});

        assertFalse(validator.validate(wrongIngredients).isEmpty());


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

        assertFalse(validator.validate(incorrectTime).isEmpty());

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

        assertFalse(validator.validate(noTitle).isEmpty());

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

        assertFalse(validator.validate(nonExistentImages).isEmpty());

        // NULL

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

        assertFalse(validator.validate(titleNull).isEmpty());

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

        assertFalse(validator.validate(ingredientsNull).isEmpty());

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

        assertFalse(validator.validate(descriptionNull).isEmpty());

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

        // Images may be null as they are not required!
        assertTrue(validator.validate(imagesNull).isEmpty());
    }

}
