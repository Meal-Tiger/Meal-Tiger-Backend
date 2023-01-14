package com.mealtiger.backend.rest.controller;

import com.mealtiger.backend.SampleSource;
import com.mealtiger.backend.database.model.recipe.Ingredient;
import com.mealtiger.backend.database.model.recipe.Rating;
import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.database.repository.RecipeRepository;
import com.mealtiger.backend.rest.error_handling.exceptions.EntityNotFoundException;
import com.mealtiger.backend.rest.error_handling.exceptions.RatingOwnRecipeException;
import com.mealtiger.backend.rest.model.Response;
import com.mealtiger.backend.rest.model.rating.AverageRatingResponse;
import com.mealtiger.backend.rest.model.rating.RatingRequest;
import com.mealtiger.backend.rest.model.rating.RatingResponse;
import com.mealtiger.backend.rest.model.recipe.RecipeRequest;
import com.mealtiger.backend.rest.model.recipe.RecipeResponse;
import org.junit.jupiter.api.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.mealtiger.backend.SampleSource.SAMPLE_RATING_ID;
import static com.mealtiger.backend.SampleSource.SAMPLE_USER_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
class RecipeControllerTest {

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

    private RecipeRepository recipeRepository;

    private RecipeController recipeController;

    /**
     * This sets up the SAMPLE_RECIPE and SAMPLE_RECIPE_DTO examples.
     */
    @BeforeAll
    static void setup() {
        SAMPLE_RECIPE.setId("TestId");
        SAMPLE_RECIPE_DTO.setTitle("Test title");
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
        recipeRepository = mock(RecipeRepository.class);
        recipeController = new RecipeController(recipeRepository);
    }

    /**
     * This tests the getRecipePage method in RecipeController.
     */
    @Test
    void getRecipePageTest() {
        Pageable paging = PageRequest.of(1, 5, Sort.by(Sort.Direction.DESC, "title"));
        when(recipeRepository.findAll(paging)).thenReturn(new PageImpl<>(List.of(
                SAMPLE_RECIPE
        ), paging, 1));

        recipeController.getRecipePage(1, 5, "title");

        verify(recipeRepository).findAll(paging);
    }

    /**
     * This tests the getRecipePageByTitleQuery method in RecipeController.
     */
    @Test
    void getRecipePageByTitleQueryTest() {
        Pageable paging = PageRequest.of(1, 5, Sort.by(Sort.Direction.DESC, "title"));
        when(recipeRepository.findRecipesByTitleContainingIgnoreCase("query", paging)).thenReturn(new PageImpl<>(List.of(
                SAMPLE_RECIPE
        ), paging, 1));

        recipeController.getRecipePageByTitleQuery(1, 5, "title", "query");

        verify(recipeRepository, never()).findAll(paging);
        verify(recipeRepository).findRecipesByTitleContainingIgnoreCase("query", paging);
    }

    /**
     * This tests the saveRecipe method in RecipeController.
     */
    @Test
    void saveRecipeTest() {
        when(recipeRepository.save(any())).thenReturn(SAMPLE_RECIPE);
        Response recipeResponse = recipeController.saveRecipe(SAMPLE_RECIPE_DTO, SAMPLE_USER_ID);
        verify(recipeRepository).save(any());

        assertEquals(SAMPLE_RECIPE.getId(), ((RecipeResponse) recipeResponse).getId());
        assertEquals(SAMPLE_RECIPE.getTitle(), ((RecipeResponse) recipeResponse).getTitle());
        assertEquals(SAMPLE_RECIPE.getDescription(), ((RecipeResponse) recipeResponse).getDescription());
        assertEquals(SAMPLE_RECIPE.getDifficulty(), ((RecipeResponse) recipeResponse).getDifficulty());
        assertEquals(SAMPLE_RECIPE.getTime(), ((RecipeResponse) recipeResponse).getTime());
        assertEquals(SAMPLE_RECIPE.getUserId(), ((RecipeResponse) recipeResponse).getUserId());
        assertArrayEquals(SAMPLE_RECIPE.getIngredients(), ((RecipeResponse) recipeResponse).getIngredients());
        assertArrayEquals(SAMPLE_RECIPE.getImages(), ((RecipeResponse) recipeResponse).getImages());
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
        assertDoesNotThrow(() -> recipeController.deleteRecipe("A"));
        verify(recipeRepository).deleteById("A");

        when(recipeRepository.existsById("B")).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> recipeController.deleteRecipe("B"));
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
                new Rating(SAMPLE_RATING_ID, 4, "Comment", SAMPLE_USER_ID)
        });

        when(recipeRepository.findById("A")).thenReturn(Optional.of(mockRecipe));
        assertTrue(recipeController.doesRatingExist("A", SAMPLE_USER_ID));
        verify(mockRecipe).getRatings();
        assertFalse(recipeController.doesRatingExist("A", "a3e94848-3ee4-4200-a9ed-a7f00debe554"));
        verify(mockRecipe, times(2)).getRatings();
    }

    /**
     * Tests the getRatings method
     */
    @Test
    void getRatingsTest() {
        Recipe mockRecipe = mock(Recipe.class);
        when(mockRecipe.getRatings()).thenReturn(new Rating[]{
                new Rating(SAMPLE_RATING_ID, 4, "Comment", SAMPLE_USER_ID)
        });

        when(recipeRepository.findById("A")).thenReturn(Optional.of(mockRecipe));

        Map<String, Object> returnValue = recipeController.getRatings("A", 1, 5);
        verify(recipeRepository).findById("A");
        verify(mockRecipe).getRatings();

        assertTrue(returnValue.containsKey("ratings"));
        assertTrue(returnValue.containsKey("totalPages"));
        assertTrue(returnValue.containsKey("totalItems"));
        assertTrue(returnValue.containsKey("currentPage"));

        assertTrue(returnValue.get("ratings") instanceof List);
    }

    @Test
    void getRatingTest() {
        Recipe mockRecipe = mock(Recipe.class);
        when(mockRecipe.getRatings()).thenReturn(new Rating[]{
                new Rating(SampleSource.getSampleUUIDs().get(0), 5, "Comment", SampleSource.getSampleUUIDs().get(1)),
                new Rating(SampleSource.getSampleUUIDs().get(2), 5, "Comment", SampleSource.getSampleUUIDs().get(3)),
                new Rating(SAMPLE_RATING_ID, 4, "Comment", SAMPLE_USER_ID)
        });

        when(recipeRepository.findRecipeByRatings_Id(SAMPLE_RATING_ID)).thenReturn(mockRecipe);

        RatingResponse rating = (RatingResponse) recipeController.getRating(SAMPLE_RATING_ID);

        assertEquals(SAMPLE_RATING_ID, rating.getId());
        assertEquals(4, rating.getRatingValue());
        assertEquals("Comment", rating.getComment());
        assertEquals(SAMPLE_USER_ID, rating.getUserId());
    }

    /**
     * Tests getAverageRating method
     */
    @Test
    void getAverageRatingTest() {
        Recipe mockRecipe = mock(Recipe.class);
        when(mockRecipe.getRatings()).thenReturn(new Rating[]{
                new Rating(SAMPLE_RATING_ID, 4, "Comment", SAMPLE_USER_ID)
        });

        when(recipeRepository.findById("A")).thenReturn(Optional.of(mockRecipe));

        double returnValue = ((AverageRatingResponse) recipeController.getAverageRating("A")).getRatingValue();
        verify(recipeRepository).findById("A");
        verify(mockRecipe).getRatings();

        assertEquals(4.0, returnValue, 0.01);

        when(mockRecipe.getRatings()).thenReturn(new Rating[]{
                new Rating(SAMPLE_RATING_ID, 4, "Comment", SAMPLE_USER_ID),
                new Rating(SAMPLE_RATING_ID, 2, "Comment", SAMPLE_USER_ID)
        });

        returnValue = ((AverageRatingResponse) recipeController.getAverageRating("A")).getRatingValue();

        assertEquals(3.0, returnValue, 0.01);
    }

    /**
     * Tests the addRating method
     */
    @Test
    void addRatingTest() {
        Recipe mockRecipe = spy(new Recipe(
                "Test",
                SAMPLE_USER_ID,
                new Ingredient[]{
                        new Ingredient(1, "Test", "Test")
                },
                "Description",
                1.2,
                new Rating[]{},
                15,
                new UUID[]{}
        ));

        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.setRatingValue(4);
        ratingRequest.setComment("Sample Comment");

        when(recipeRepository.findById("A")).thenReturn(Optional.of(mockRecipe));
        Response ratingResponse = recipeController.addRating("A", "e9add05b-0e50-4be9-bc00-f3ff870d51a6", ratingRequest);
        verify(mockRecipe).getRatings();
        verify(mockRecipe).setRatings(new Rating[]{new Rating(SAMPLE_RATING_ID, 4, "Sample comment", "e9add05b-0e50-4be9-bc00-f3ff870d51a6")});
        verify(recipeRepository).save(mockRecipe);

        assertEquals(ratingRequest.getRatingValue(), ((RatingResponse) ratingResponse).getRatingValue());
        assertEquals(ratingRequest.getComment(), ((RatingResponse) ratingResponse).getComment());
        assertNotNull(((RatingResponse) ratingResponse).getId());
        assertEquals("e9add05b-0e50-4be9-bc00-f3ff870d51a6", ((RatingResponse) ratingResponse).getUserId());

        assertThrows(RatingOwnRecipeException.class, () -> recipeController.addRating("A", SAMPLE_USER_ID, ratingRequest));
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
                        new Rating(SAMPLE_RATING_ID, 4, "Sample comment", "e9add05b-0e50-4be9-bc00-f3ff870d51a6")
                },
                15,
                new UUID[]{}
        ));

        when(recipeRepository.findById("A")).thenReturn(Optional.of(mockRecipe));

        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.setComment("Sample");
        ratingRequest.setRatingValue(3);

        recipeController.updateRating("A", "e9add05b-0e50-4be9-bc00-f3ff870d51a6", ratingRequest);
        verify(mockRecipe, times(3)).getRatings();
        verify(mockRecipe).setRatings(new Rating[]{});
        verify(mockRecipe).setRatings(new Rating[]{new Rating(SAMPLE_RATING_ID, 3, "Sample", "e9add05b-0e50-4be9-bc00-f3ff870d51a6")});

        verify(recipeRepository, times(2)).save(mockRecipe);

        assertArrayEquals(new Rating[]{new Rating(SAMPLE_RATING_ID, 3, "Sample",  "e9add05b-0e50-4be9-bc00-f3ff870d51a6")}, mockRecipe.getRatings());
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
                        new Rating(SAMPLE_RATING_ID, 4, "Comment", SAMPLE_USER_ID)
                },
                15,
                new UUID[]{}
        ));

        when(recipeRepository.findById("A")).thenReturn(Optional.of(mockRecipe));
        recipeController.deleteRating("A", SAMPLE_USER_ID);

        verify(mockRecipe, times(2)).getRatings();
        verify(mockRecipe).setRatings(new Rating[]{});
        verify(recipeRepository).save(mockRecipe);

        assertArrayEquals(new Rating[]{}, mockRecipe.getRatings());

        assertThrows(EntityNotFoundException.class, () -> recipeController.deleteRating("A", SAMPLE_USER_ID));
    }
}
