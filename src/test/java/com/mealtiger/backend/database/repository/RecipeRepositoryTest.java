package com.mealtiger.backend.database.repository;

import com.mealtiger.backend.BackendApplication;
import com.mealtiger.backend.SampleSource;
import com.mealtiger.backend.database.model.recipe.Ingredient;
import com.mealtiger.backend.database.model.recipe.Rating;
import com.mealtiger.backend.database.model.recipe.Recipe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

import static com.mealtiger.backend.SampleSource.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {BackendApplication.class}
)
@Tag("integration")
class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    @AfterEach
    void beforeAfterEach() {
        recipeRepository.deleteAll();
    }

    @ParameterizedTest(name = "#{index} - Case insensitivity test with randomcased string {0}")
    @MethodSource("randomcasedStrings")
    void findRecipesByTitleContainingIgnoreCaseTest(String randomcased) {
        Recipe[] testRecipes = {
                new Recipe(
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
                ),
                new Recipe(
                        "Gebratene Cashewkerne",
                        SampleSource.SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Cashewkerne, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        new Rating[]{},
                        15,
                        new UUID[]{}
                ),
                new Recipe(
                        "Toast Hawaii",
                        SampleSource.SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Schinken"),
                                new Ingredient(10, "Scheiben", "Toastbrot"),
                                new Ingredient(10, "Scheiben", "Ananas"),
                                new Ingredient(10, "Scheiben", "Schmelzkäse")
                        },
                        "TestDescription",
                        1,
                        new Rating[]{new Rating(SAMPLE_RATING_ID, 1, "Sample", SampleSource.SAMPLE_OTHER_USER_ID)},
                        15,
                        new UUID[]{}
                )
        };

        recipeRepository.saveAll(Arrays.asList(testRecipes));

        Pageable pageable = PageRequest.of(0, 1);

        String compareTitle = "Gebrannte Mandeln";
        String foundTitle = recipeRepository.findRecipesByTitleContainingIgnoreCase(randomcased, pageable).getContent().get(0).getTitle();
        assertEquals(compareTitle, foundTitle);
    }

    //Value sources

    /**
     * Testing the query parameter
     */

    @Test
    void findRecipesByTitleContainingTest() {
        Recipe[] testRecipes = {
                new Recipe(
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
                ),
                new Recipe(
                        "Gebratene Cashewkerne",
                        SampleSource.SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Cashewkerne, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        new Rating[]{},
                        15,
                        new UUID[]{}
                ),
                new Recipe(
                        "Toast Hawaii",
                        SampleSource.SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Schinken"),
                                new Ingredient(10, "Scheiben", "Toastbrot"),
                                new Ingredient(10, "Scheiben", "Ananas"),
                                new Ingredient(10, "Scheiben", "Schmelzkäse")
                        },
                        "TestDescription",
                        1,
                        new Rating[]{},
                        15,
                        new UUID[]{}
                )
        };

        recipeRepository.saveAll(Arrays.asList(testRecipes));

        Pageable pageable = PageRequest.of(0, 1);

        String foundTitle = recipeRepository.findRecipesByTitleContainingIgnoreCase("Gebrannte Mandeln", pageable).getContent().get(0).getTitle();
        assertEquals("Gebrannte Mandeln", foundTitle);
    }

    @Test
    void findRecipeByRatings_IdTest() {
        Rating toBeFound = new Rating(SAMPLE_RATING_ID, 4, "Sample comment 1", SAMPLE_OTHER_USER_ID);

        Recipe[] testRecipes = {
                new Recipe(
                        "Gebrannte Mandeln",
                        SampleSource.SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Mandeln, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        new Rating[]{
                                toBeFound
                        },
                        15,
                        new UUID[]{}
                ),
                new Recipe(
                        "Gebratene Cashewkerne",
                        SampleSource.SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Cashewkerne, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        new Rating[]{
                                new Rating(SampleSource.getSampleUUIDs().get(0), 2, "Sample comment 2", SAMPLE_OTHER_USER_ID)
                        },
                        15,
                        new UUID[]{}
                ),
                new Recipe(
                        "Toast Hawaii",
                        SampleSource.SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Schinken"),
                                new Ingredient(10, "Scheiben", "Toastbrot"),
                                new Ingredient(10, "Scheiben", "Ananas"),
                                new Ingredient(10, "Scheiben", "Schmelzkäse")
                        },
                        "TestDescription",
                        1,
                        new Rating[]{
                                new Rating(SampleSource.getSampleUUIDs().get(1), 3, "Sample comment 3", SAMPLE_OTHER_USER_ID)
                        },
                        15,
                        new UUID[]{}
                )
        };

        recipeRepository.saveAll(Arrays.asList(testRecipes));

        Recipe foundRecipe = recipeRepository.findRecipeByRatings_Id(SAMPLE_RATING_ID);

        assertTrue(Arrays.asList(foundRecipe.getRatings()).contains(toBeFound));
    }

    @Test
    void findRecipesByUserId() {
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

        Pageable pageable = PageRequest.of(0, 9);

        Page<Recipe> foundRecipes = recipeRepository.findRecipesByUserId(SAMPLE_USER_ID, pageable);

        assertEquals(9, foundRecipes.getContent().size());
        assertEquals(9, foundRecipes.getTotalElements());
        assertEquals(0, foundRecipes.getNumber());
    }

    // VALUE SOURCES

    private static Stream<String> randomcasedStrings() {
        return Stream.of(
                "GEBRaNNTe mandEln",
                "gEbRaNNTe MANDElN",
                "GeBrAnNTe mANdeln",
                "gEBrANntE MAndEln",
                "GEBrANnte mAnDELN"
        );
    }
}