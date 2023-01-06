package com.mealtiger.backend.rest.controller;

import com.mealtiger.backend.database.model.recipe.Ingredient;
import com.mealtiger.backend.database.model.recipe.Rating;
import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.database.repository.RecipeRepository;
import com.mealtiger.backend.rest.model.recipe.RecipeRequest;
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

    private static final RecipeRequest SAMPLE_RECIPE_DTO = new RecipeRequest();

    private static final Recipe SAMPLE_RECIPE = new Recipe(
            "Test recipe title",
            SAMPLE_USER_ID,
            new Ingredient[]{
                    new Ingredient(10, "Units", "Very different ingredient"),
                    new Ingredient(25, "Units", "Other test ingredient")
            },
            "Test description",
            1.4,
            new Rating[]{},
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
        SAMPLE_RECIPE_DTO.setTitle("Test title");
        SAMPLE_RECIPE_DTO.setUserId(SAMPLE_USER_ID);
        SAMPLE_RECIPE_DTO.setIngredients(new Ingredient[]{
                new Ingredient(10, "Units", "Test ingredient"),
                new Ingredient(25, "Units", "Other test ingredient")
        });
        SAMPLE_RECIPE_DTO.setDescription("Test description");
        SAMPLE_RECIPE_DTO.setDifficulty(1.2);
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
        verify(recipeRepository).save(any());
    }

    /**
     * This tests the getRecipe method in RecipeController.
     */
    @Test
    void getRecipeTest() {
        try {
            recipeController.getRecipe("TestId");
        } catch (Exception e) {
            // Expected...
        }
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

    // RATING TESTS

    /**
     * Tests the doesRatingExist method.
     */
    @Test
    void doesRatingExistTest() {
        Recipe mockRecipe = mock(Recipe.class);
        when(mockRecipe.getRatings()).thenReturn(new Rating[]{
                new Rating(4, SAMPLE_USER_ID)
        });

        when(recipeRepository.findById("A")).thenReturn(Optional.of(mockRecipe));
        assertTrue(recipeController.doesRatingExist("A", SAMPLE_USER_ID));
        verify(mockRecipe).getRatings();
        assertFalse(recipeController.doesRatingExist("A", "a3e94848-3ee4-4200-a9ed-a7f00debe554"));
        verify(mockRecipe, times(2)).getRatings();
    }

    /**
     * Tests the addRating method
     */
    @Test
    void addRatingTest() {
        Recipe mockRecipe = mock(Recipe.class);
        when(mockRecipe.getRatings()).thenReturn(new Rating[]{});

        when(recipeRepository.findById("A")).thenReturn(Optional.of(mockRecipe));
        recipeController.addRating("A", SAMPLE_USER_ID, 4);
        verify(mockRecipe).getRatings();
        verify(mockRecipe).setRatings(new Rating[]{new Rating(4, SAMPLE_USER_ID)});
        verify(recipeRepository).save(mockRecipe);
    }

    /**
     * Tests the updateRating method
     */
    @Test
    void updateRatingTest() {
        Recipe mockRecipe = spy(new Recipe(
                "Test",
                SAMPLE_USER_ID,
                new Ingredient[]{
                        new Ingredient(1, "Test", "Test")
                },
                "Description",
                1.2,
                new Rating[]{
                        new Rating(4, SAMPLE_USER_ID)
                },
                15,
                new UUID[]{}
        ));

        when(recipeRepository.findById("A")).thenReturn(Optional.of(mockRecipe));

        recipeController.updateRating("A", SAMPLE_USER_ID, 1);
        verify(mockRecipe, times(2)).getRatings();
        verify(mockRecipe).setRatings(new Rating[]{});
        verify(mockRecipe).setRatings(new Rating[]{new Rating(1, SAMPLE_USER_ID)});

        verify(recipeRepository, times(2)).save(mockRecipe);

        assertArrayEquals(new Rating[]{new Rating(1, SAMPLE_USER_ID)}, mockRecipe.getRatings());
    }

    /**
     * Tests the deleteRating method
     */
    @Test
    void deleteRatingTest() {
        Recipe mockRecipe = spy(new Recipe(
                "Test",
                SAMPLE_USER_ID,
                new Ingredient[]{
                        new Ingredient(1, "Test", "Test")
                },
                "Description",
                1.2,
                new Rating[]{
                        new Rating(4, SAMPLE_USER_ID)
                },
                15,
                new UUID[]{}
        ));

        when(recipeRepository.findById("A")).thenReturn(Optional.of(mockRecipe));
        recipeController.deleteRating("A", SAMPLE_USER_ID);

        verify(mockRecipe).getRatings();
        verify(mockRecipe).setRatings(new Rating[]{});
        verify(recipeRepository).save(mockRecipe);

        assertArrayEquals(new Rating[]{}, mockRecipe.getRatings());
    }
}
