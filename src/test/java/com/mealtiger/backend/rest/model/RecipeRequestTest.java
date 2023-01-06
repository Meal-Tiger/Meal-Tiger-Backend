package com.mealtiger.backend.rest.model;

import com.mealtiger.backend.database.model.recipe.Ingredient;
import com.mealtiger.backend.rest.model.recipe.RecipeRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecipeRequestTest {

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
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setTitle("Test title");
        recipeRequest.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Test ingredient"),
                new Ingredient(25, "Units", "Other test ingredient")
        });
        recipeRequest.setDescription("Test description");
        recipeRequest.setDifficulty(1.2);
        recipeRequest.setTime(10);
        recipeRequest.setImages(new UUID[]{});

        assertTrue(validator.validate(recipeRequest).isEmpty());

        // USERID SET

        recipeRequest = new RecipeRequest();
        recipeRequest.setTitle("Test title");
        recipeRequest.setUserId(SAMPLE_USER_ID);
        recipeRequest.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Test ingredient"),
                new Ingredient(25, "Units", "Other test ingredient")
        });
        recipeRequest.setDescription("Test description");
        recipeRequest.setDifficulty(1.2);
        recipeRequest.setTime(10);
        recipeRequest.setImages(new UUID[]{});

        assertFalse(validator.validate(recipeRequest).isEmpty());

        // INCORRECT DIFFICULTY

        RecipeRequest incorrectDifficultyTooHigh = new RecipeRequest();
        incorrectDifficultyTooHigh.setTitle("Test title");
        incorrectDifficultyTooHigh.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Test ingredient"),
                new Ingredient(25, "Units", "Other test ingredient")
        });
        incorrectDifficultyTooHigh.setDescription("Test description");
        incorrectDifficultyTooHigh.setDifficulty(3.1);
        incorrectDifficultyTooHigh.setTime(10);
        incorrectDifficultyTooHigh.setImages(new UUID[]{});

        assertFalse(validator.validate(incorrectDifficultyTooHigh).isEmpty());

        RecipeRequest incorrectDifficultyTooLow = new RecipeRequest();
        incorrectDifficultyTooLow.setTitle("Test title");
        incorrectDifficultyTooLow.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Test ingredient"),
                new Ingredient(25, "Units", "Other test ingredient")
        });
        incorrectDifficultyTooLow.setDescription("Test description");
        incorrectDifficultyTooLow.setDifficulty(0);
        incorrectDifficultyTooLow.setTime(10);
        incorrectDifficultyTooLow.setImages(new UUID[]{});

        assertFalse(validator.validate(incorrectDifficultyTooLow).isEmpty());

        // NO INGREDIENTS

        RecipeRequest noIngredients = new RecipeRequest();
        noIngredients.setTitle("Test title");
        noIngredients.setIngredients(new Ingredient[]{});
        noIngredients.setDescription("Test description");
        noIngredients.setDifficulty(1);
        noIngredients.setTime(10);
        noIngredients.setImages(new UUID[]{});

        assertFalse(validator.validate(noIngredients).isEmpty());

        // INCORRECT TIME

        RecipeRequest incorrectTime = new RecipeRequest();
        incorrectTime.setTitle("Test title");
        incorrectTime.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Sample")
        });
        incorrectTime.setDescription("Test description");
        incorrectTime.setDifficulty(1);
        incorrectTime.setTime(-11);
        incorrectTime.setImages(new UUID[]{});

        assertFalse(validator.validate(incorrectTime).isEmpty());

        // NO TITLE

        RecipeRequest noTitle = new RecipeRequest();
        noTitle.setTitle("");
        noTitle.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Sample")
        });
        noTitle.setDescription("Test description");
        noTitle.setDifficulty(1);
        noTitle.setTime(11);
        noTitle.setImages(new UUID[]{});

        assertFalse(validator.validate(noTitle).isEmpty());

        // NON-EXISTENT IMAGES

        RecipeRequest nonExistentImages = new RecipeRequest();
        nonExistentImages.setTitle("Something");
        nonExistentImages.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Sample")
        });
        nonExistentImages.setDescription("Test description");
        nonExistentImages.setDifficulty(1);
        nonExistentImages.setTime(11);
        nonExistentImages.setImages(new UUID[]{
                UUID.fromString(SAMPLE_USER_ID)
        });

        assertFalse(validator.validate(nonExistentImages).isEmpty());

        // NULL

        RecipeRequest titleNull = new RecipeRequest();
        titleNull.setTitle(null);
        titleNull.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Sample")
        });
        titleNull.setDescription("Test description");
        titleNull.setDifficulty(1);
        titleNull.setTime(11);
        titleNull.setImages(new UUID[]{});

        assertFalse(validator.validate(titleNull).isEmpty());

        RecipeRequest ingredientsNull = new RecipeRequest();
        ingredientsNull.setTitle("Title");
        ingredientsNull.setIngredients(null);
        ingredientsNull.setDescription("Test description");
        ingredientsNull.setDifficulty(1);
        ingredientsNull.setTime(11);
        ingredientsNull.setImages(new UUID[]{});

        assertFalse(validator.validate(ingredientsNull).isEmpty());

        RecipeRequest descriptionNull = new RecipeRequest();
        descriptionNull.setTitle("Title");
        descriptionNull.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Sample")
        });
        descriptionNull.setDescription(null);
        descriptionNull.setDifficulty(1);
        descriptionNull.setTime(11);
        descriptionNull.setImages(new UUID[]{});

        assertFalse(validator.validate(descriptionNull).isEmpty());

        RecipeRequest imagesNull = new RecipeRequest();
        imagesNull.setTitle("Title");
        imagesNull.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Sample")
        });
        imagesNull.setDescription("Test description");
        imagesNull.setDifficulty(1);
        imagesNull.setTime(11);
        imagesNull.setImages(null);

        // Images may be null as they are not required!
        assertTrue(validator.validate(imagesNull).isEmpty());
    }

}
