package com.mealtiger.backend.database.model.recipe;

import com.mealtiger.backend.SampleSource;
import com.mealtiger.backend.rest.model.recipe.RecipeRequest;
import com.mealtiger.backend.rest.model.recipe.RecipeResponse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the methods to convert a Recipe to RecipeDTO and vice versa.
 *
 * @author Sebastian Maier
 */
@Tag("unit")
class RecipeTest {

    /**
     * Tests if Recipe converted to RecipeResponse is equal to original Recipe.
     */
    @Test
    void convertToResponse() {
        // NO RATING, NO IMAGES

        Recipe recipe = new Recipe(
                "Gebrannte Mandeln",
                SampleSource.SAMPLE_USER_ID,
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                new Rating[]{},
                15,
                new UUID[]{}
        );

        RecipeResponse recipeResponse = (RecipeResponse) recipe.toResponse();

        assertEquals(recipe.getId(), recipeResponse.getId());
        assertEquals(recipe.getUserId(), recipeResponse.getUserId());
        assertArrayEquals(recipe.getIngredients(), recipeResponse.getIngredients());
        assertEquals(recipe.getDescription(), recipeResponse.getDescription());
        assertEquals(recipe.getDifficulty(), recipeResponse.getDifficulty());
        assertEquals(recipe.getTime(), recipeResponse.getTime());
        assertArrayEquals(recipe.getImages(), recipeResponse.getImages());

        // WITH RATING, NO IMAGES

        recipe = new Recipe(
                "Gebrannte Mandeln",
                SampleSource.SAMPLE_USER_ID,
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                new Rating[]{
                        new Rating(SampleSource.getSampleUUIDs().get(0), 4, "Comment", SampleSource.SAMPLE_USER_ID),
                        new Rating(SampleSource.getSampleUUIDs().get(1), 2, "Second comment", SampleSource.SAMPLE_USER_ID)
                },
                15,
                new UUID[]{}
        );

        recipeResponse = (RecipeResponse) recipe.toResponse();

        assertEquals(recipe.getId(), recipeResponse.getId());
        assertEquals(recipe.getUserId(), recipeResponse.getUserId());
        assertArrayEquals(recipe.getIngredients(), recipeResponse.getIngredients());
        assertEquals(recipe.getDescription(), recipeResponse.getDescription());
        assertEquals(recipe.getDifficulty(), recipeResponse.getDifficulty());
        assertEquals(recipe.getTime(), recipeResponse.getTime());
        assertArrayEquals(recipe.getImages(), recipeResponse.getImages());

        // WITH RATING, IMAGES

        recipe = new Recipe(
                "Gebrannte Mandeln",
                SampleSource.SAMPLE_USER_ID,
                new Ingredient[]{
                        new Ingredient(500, "Gramm", "Mandeln, geschält"),
                        new Ingredient(200, "Gramm", "Zucker")
                },
                "TestDescription",
                3,
                new Rating[]{
                        new Rating(SampleSource.getSampleUUIDs().get(0), 4, "This is a sample", SampleSource.SAMPLE_USER_ID),
                        new Rating(SampleSource.getSampleUUIDs().get(0), 2, "Other Sample", SampleSource.SAMPLE_USER_ID)
                },
                15,
                new UUID[]{
                        UUID.fromString(SampleSource.getSampleUUIDs().get(0)),
                        UUID.fromString(SampleSource.getSampleUUIDs().get(1))
                }
        );

        recipeResponse = (RecipeResponse) recipe.toResponse();

        assertEquals(recipe.getId(), recipeResponse.getId());
        assertEquals(recipe.getUserId(), recipeResponse.getUserId());
        assertArrayEquals(recipe.getIngredients(), recipeResponse.getIngredients());
        assertEquals(recipe.getDescription(), recipeResponse.getDescription());
        assertEquals(recipe.getDifficulty(), recipeResponse.getDifficulty());
        assertEquals(recipe.getTime(), recipeResponse.getTime());
        assertArrayEquals(recipe.getImages(), recipeResponse.getImages());
    }

    /**
     * Tests if RecipeRequest converted to Recipe is equal to original RecipeRequest.
     */
    @Test
    void convertFromRequest() {
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setTitle("Gebrannte Mandeln");
        recipeRequest.setIngredients(new Ingredient[]{
                new Ingredient(500, "Gramm", "Mandeln, geschält"),
                new Ingredient(200, "Gramm", "Zucker")
        });
        recipeRequest.setDescription("TestDescription");
        recipeRequest.setDifficulty(3);
        recipeRequest.setTime(15);
        recipeRequest.setImages(new UUID[]{UUID.fromString(SampleSource.SAMPLE_IMAGE_ID)});

        Recipe recipe = recipeRequest.toEntity();

        assertEquals(recipeRequest.getTitle(), recipe.getTitle());
        assertArrayEquals(recipeRequest.getIngredients(), recipe.getIngredients());
        assertEquals(recipeRequest.getDescription(), recipe.getDescription());
        assertEquals(recipeRequest.getDifficulty(), recipe.getDifficulty());
        assertEquals(recipeRequest.getTime(), recipe.getTime());
        assertEquals(recipeRequest.getImages(), recipe.getImages());
        assertArrayEquals(new Rating[]{}, recipe.getRatings());
    }

    @Test
    void equalsHashCodeTest() {
        Recipe reference = new Recipe(
                "Some title",
                SampleSource.SAMPLE_USER_ID,
                new Ingredient[]{
                        new Ingredient(1, "Some unit", "Some ingredient")
                },
                "Some description",
                1,
                new Rating[]{
                        new Rating(SampleSource.SAMPLE_RATING_ID, 1, "Some comment", SampleSource.SAMPLE_OTHER_USER_ID)
                },
                9,
                new UUID[]{
                        UUID.fromString(SampleSource.SAMPLE_IMAGE_ID)
                }
        );

        reference.setId(SampleSource.getSampleUUIDs().get(0));

        Recipe differentTitle = new Recipe(
                "Some other title",
                SampleSource.SAMPLE_USER_ID,
                new Ingredient[]{
                        new Ingredient(1, "Some unit", "Some ingredient")
                },
                "Some description",
                1,
                new Rating[]{
                        new Rating(SampleSource.SAMPLE_RATING_ID, 1, "Some comment", SampleSource.SAMPLE_OTHER_USER_ID)
                },
                9,
                new UUID[]{
                        UUID.fromString(SampleSource.SAMPLE_IMAGE_ID)
                }
        );

        Recipe differentUser = new Recipe(
                "Some title",
                SampleSource.SAMPLE_OTHER_USER_ID,
                new Ingredient[]{
                        new Ingredient(1, "Some unit", "Some ingredient")
                },
                "Some description",
                1,
                new Rating[]{
                        new Rating(SampleSource.SAMPLE_RATING_ID, 1, "Some comment", SampleSource.SAMPLE_OTHER_USER_ID)
                },
                9,
                new UUID[]{
                        UUID.fromString(SampleSource.SAMPLE_IMAGE_ID)
                }
        );

        Recipe differentIngredients = new Recipe(
                "Some title",
                SampleSource.SAMPLE_USER_ID,
                new Ingredient[]{},
                "Some description",
                1,
                new Rating[]{
                        new Rating(SampleSource.SAMPLE_RATING_ID, 1, "Some comment", SampleSource.SAMPLE_OTHER_USER_ID)
                },
                9,
                new UUID[]{
                        UUID.fromString(SampleSource.SAMPLE_IMAGE_ID)
                }
        );

        Recipe differentDescription = new Recipe(
                "Some title",
                SampleSource.SAMPLE_USER_ID,
                new Ingredient[]{
                        new Ingredient(1, "Some unit", "Some ingredient")
                },
                "Some different description",
                1,
                new Rating[]{
                        new Rating(SampleSource.SAMPLE_RATING_ID, 1, "Some comment", SampleSource.SAMPLE_OTHER_USER_ID)
                },
                9,
                new UUID[]{
                        UUID.fromString(SampleSource.SAMPLE_IMAGE_ID)
                }
        );

        Recipe differentDifficulty = new Recipe(
                "Some title",
                SampleSource.SAMPLE_USER_ID,
                new Ingredient[]{
                        new Ingredient(1, "Some unit", "Some ingredient")
                },
                "Some description",
                3,
                new Rating[]{
                        new Rating(SampleSource.SAMPLE_RATING_ID, 1, "Some comment", SampleSource.SAMPLE_OTHER_USER_ID)
                },
                9,
                new UUID[]{
                        UUID.fromString(SampleSource.SAMPLE_IMAGE_ID)
                }
        );

        Recipe differentRatings = new Recipe(
                "Some title",
                SampleSource.SAMPLE_USER_ID,
                new Ingredient[]{
                        new Ingredient(1, "Some unit", "Some ingredient")
                },
                "Some description",
                1,
                new Rating[]{},
                9,
                new UUID[]{
                        UUID.fromString(SampleSource.SAMPLE_IMAGE_ID)
                }
        );

        Recipe differentTime = new Recipe(
                "Some title",
                SampleSource.SAMPLE_USER_ID,
                new Ingredient[]{
                        new Ingredient(1, "Some unit", "Some ingredient")
                },
                "Some description",
                1,
                new Rating[]{
                        new Rating(SampleSource.SAMPLE_RATING_ID, 1, "Some comment", SampleSource.SAMPLE_OTHER_USER_ID)
                },
                8,
                new UUID[]{
                        UUID.fromString(SampleSource.SAMPLE_IMAGE_ID)
                }
        );

        Recipe differentImages = new Recipe(
                "Some title",
                SampleSource.SAMPLE_USER_ID,
                new Ingredient[]{
                        new Ingredient(1, "Some unit", "Some ingredient")
                },
                "Some description",
                1,
                new Rating[]{
                        new Rating(SampleSource.SAMPLE_RATING_ID, 1, "Some comment", SampleSource.SAMPLE_OTHER_USER_ID)
                },
                9,
                new UUID[]{}
        );

        Recipe differentIds = new Recipe(
                "Some title",
                SampleSource.SAMPLE_USER_ID,
                new Ingredient[]{
                        new Ingredient(1, "Some unit", "Some ingredient")
                },
                "Some description",
                1,
                new Rating[]{
                        new Rating(SampleSource.SAMPLE_RATING_ID, 1, "Some comment", SampleSource.SAMPLE_OTHER_USER_ID)
                },
                9,
                new UUID[]{
                        UUID.fromString(SampleSource.SAMPLE_IMAGE_ID)
                }
        );

        differentTitle.setId(SampleSource.getSampleUUIDs().get(0));
        differentUser.setId(SampleSource.getSampleUUIDs().get(0));
        differentIngredients.setId(SampleSource.getSampleUUIDs().get(0));
        differentDescription.setId(SampleSource.getSampleUUIDs().get(0));
        differentDifficulty.setId(SampleSource.getSampleUUIDs().get(0));
        differentRatings.setId(SampleSource.getSampleUUIDs().get(0));
        differentTime.setId(SampleSource.getSampleUUIDs().get(0));
        differentImages.setId(SampleSource.getSampleUUIDs().get(0));
        differentIds.setId(SampleSource.getSampleUUIDs().get(1));

        assertEquals(reference, reference);
        assertNotEquals(reference, differentTitle);
        assertNotEquals(reference, differentUser);
        assertNotEquals(reference, differentIngredients);
        assertNotEquals(reference, differentDescription);
        assertNotEquals(reference, differentDifficulty);
        assertNotEquals(reference, differentRatings);
        assertNotEquals(reference, differentTime);
        assertNotEquals(reference, differentImages);
        assertNotEquals(reference, differentIds);

        assertEquals(reference.hashCode(), reference.hashCode());
        assertNotEquals(reference.hashCode(), differentTitle);
        assertNotEquals(reference.hashCode(), differentUser.hashCode());
        assertNotEquals(reference.hashCode(), differentIngredients.hashCode());
        assertNotEquals(reference.hashCode(), differentDescription.hashCode());
        assertNotEquals(reference.hashCode(), differentDifficulty.hashCode());
        assertNotEquals(reference.hashCode(), differentRatings.hashCode());
        assertNotEquals(reference.hashCode(), differentTime.hashCode());
        assertNotEquals(reference.hashCode(), differentImages.hashCode());
        assertNotEquals(reference.hashCode(), differentIds.hashCode());
    }
}