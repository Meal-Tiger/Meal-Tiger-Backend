package com.mealtiger.backend.database.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtiger.backend.BackendApplication;
import com.mealtiger.backend.database.model.recipe.Ingredient;
import com.mealtiger.backend.database.model.recipe.Recipe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {BackendApplication.class}
)
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
    void findRecipesByTitleContainingIgnoreCaseTest(String randomcased){
        Recipe[] testRecipes = {
                new Recipe(
                        "Gebrannte Mandeln",
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Mandeln, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        5,
                        15
                ),
                new Recipe(
                        "Gebratene Cashewkerne",
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Cashewkerne, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        5,
                        15
                ),
                new Recipe(
                        "Toast Hawaii",
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Schinken"),
                                new Ingredient(10, "Scheiben", "Toastbrot"),
                                new Ingredient(10, "Scheiben", "Ananas"),
                                new Ingredient(10, "Scheiben", "Schmelzkäse")
                        },
                        "TestDescription",
                        1,
                        4,
                        15
                )
        };

        recipeRepository.saveAll(Arrays.asList(testRecipes));

        Pageable pageable = PageRequest.of(0,1);

        String compareTitle = "Gebrannte Mandeln";
        String foundTitle = recipeRepository.findRecipesByTitleContainingIgnoreCase(randomcased, pageable).getContent().get(0).getTitle();
        assertEquals(compareTitle, foundTitle);
    }

    /**
     * Testing the query parameter
     */

    @Test
    void findRecipesByTitleContainingTest() throws Exception {
        Recipe[] testRecipes = {
                new Recipe(
                        "Gebrannte Mandeln",
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Mandeln, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        5,
                        15
                ),
                new Recipe(
                        "Gebratene Cashewkerne",
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Cashewkerne, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        5,
                        15
                ),
                new Recipe(
                        "Toast Hawaii",
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Schinken"),
                                new Ingredient(10, "Scheiben", "Toastbrot"),
                                new Ingredient(10, "Scheiben", "Ananas"),
                                new Ingredient(10, "Scheiben", "Schmelzkäse")
                        },
                        "TestDescription",
                        1,
                        4,
                        15
                )
        };

        recipeRepository.saveAll(Arrays.asList(testRecipes));

        Pageable pageable = PageRequest.of(0,1);

        String foundTitle = recipeRepository.findRecipesByTitleContainingIgnoreCase("Gebrannte Mandeln", pageable).getContent().get(0).getTitle();
        assertEquals("Gebrannte Mandeln", foundTitle);
    }

    //Value sources

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