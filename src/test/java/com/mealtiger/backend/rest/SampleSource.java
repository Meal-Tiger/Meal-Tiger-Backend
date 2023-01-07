package com.mealtiger.backend.rest;

import com.mealtiger.backend.database.model.recipe.Ingredient;
import com.mealtiger.backend.database.model.recipe.Rating;
import com.mealtiger.backend.database.model.recipe.Recipe;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class SampleSource {

    private static final String SAMPLE_USER_ID = "123e4567-e89b-12d3-a456-42661417400";

    public static List<Recipe> getSampleRecipes() {
        return List.of(
                new Recipe(
                        "Gebrannte Mandeln",
                        SAMPLE_USER_ID,
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
                        SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Cashewkerne, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        new Rating[]{},
                        15,
                        new UUID[]{UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()}
                ),
                new Recipe(
                        "Toast Hawaii",
                        SAMPLE_USER_ID,
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
                ),
                new Recipe(
                        "Gebrannte Mandeln",
                        SAMPLE_USER_ID,
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
                        SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Cashewkerne, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        new Rating[]{},
                        15,
                        new UUID[]{UUID.randomUUID(), UUID.randomUUID()}
                ),
                new Recipe(
                        "Toast Hawaii",
                        SAMPLE_USER_ID,
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
                ),
                new Recipe(
                        "Gebrannte Mandeln",
                        SAMPLE_USER_ID,
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
                        SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Cashewkerne, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        new Rating[]{},
                        15,
                        new UUID[]{UUID.randomUUID()}
                ),
                new Recipe(
                        "Toast Hawaii",
                        SAMPLE_USER_ID,
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
        );
    }

    public static List<Recipe> getSampleRecipesWithRatings() {
        return List.of(
                new Recipe(
                        "Gebrannte Mandeln",
                        SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Mandeln, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        new Rating[]{
                                new Rating(4, SAMPLE_USER_ID),
                                new Rating(3, SAMPLE_USER_ID)
                        },
                        15,
                        new UUID[]{}
                ),
                new Recipe(
                        "Gebratene Cashewkerne",
                        SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Cashewkerne, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        new Rating[]{
                                new Rating(1, SAMPLE_USER_ID),
                        },
                        15,
                        new UUID[]{UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()}
                ),
                new Recipe(
                        "Toast Hawaii",
                        SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Schinken"),
                                new Ingredient(10, "Scheiben", "Toastbrot"),
                                new Ingredient(10, "Scheiben", "Ananas"),
                                new Ingredient(10, "Scheiben", "Schmelzkäse")
                        },
                        "TestDescription",
                        1,
                        new Rating[]{
                                new Rating(2, SAMPLE_USER_ID),
                                new Rating(5, SAMPLE_USER_ID)
                        },
                        15,
                        new UUID[]{}
                ),
                new Recipe(
                        "Gebrannte Mandeln",
                        SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Mandeln, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        new Rating[]{
                                new Rating(1, SAMPLE_USER_ID),
                                new Rating(3, SAMPLE_USER_ID),
                                new Rating(5, SAMPLE_USER_ID)
                        },
                        15,
                        new UUID[]{}
                ),
                new Recipe(
                        "Gebratene Cashewkerne",
                        SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Cashewkerne, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        new Rating[]{
                                new Rating(4, SAMPLE_USER_ID),
                                new Rating(3, SAMPLE_USER_ID)
                        },
                        15,
                        new UUID[]{UUID.randomUUID(), UUID.randomUUID()}
                ),
                new Recipe(
                        "Toast Hawaii",
                        SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Schinken"),
                                new Ingredient(10, "Scheiben", "Toastbrot"),
                                new Ingredient(10, "Scheiben", "Ananas"),
                                new Ingredient(10, "Scheiben", "Schmelzkäse")
                        },
                        "TestDescription",
                        1,
                        new Rating[]{
                                new Rating(2, SAMPLE_USER_ID),
                                new Rating(1, SAMPLE_USER_ID)
                        },
                        15,
                        new UUID[]{}
                ),
                new Recipe(
                        "Gebrannte Mandeln",
                        SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Mandeln, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        new Rating[]{
                                new Rating(3, SAMPLE_USER_ID),
                                new Rating(1, SAMPLE_USER_ID)
                        },
                        15,
                        new UUID[]{}
                ),
                new Recipe(
                        "Gebratene Cashewkerne",
                        SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Cashewkerne, geschält"),
                                new Ingredient(200, "Gramm", "Zucker")
                        },
                        "TestDescription",
                        3,
                        new Rating[]{
                                new Rating(3, SAMPLE_USER_ID),
                                new Rating(2, SAMPLE_USER_ID),
                                new Rating(1, SAMPLE_USER_ID)
                        },
                        15,
                        new UUID[]{UUID.randomUUID()}
                ),
                new Recipe(
                        "Toast Hawaii",
                        SAMPLE_USER_ID,
                        new Ingredient[]{
                                new Ingredient(500, "Gramm", "Schinken"),
                                new Ingredient(10, "Scheiben", "Toastbrot"),
                                new Ingredient(10, "Scheiben", "Ananas"),
                                new Ingredient(10, "Scheiben", "Schmelzkäse")
                        },
                        "TestDescription",
                        1,
                        new Rating[]{
                                new Rating(4, SAMPLE_USER_ID)
                        },
                        15,
                        new UUID[]{}
                )
        );
    }

    public static List<Recipe> getSampleRecipes(int amount) {
        return getSampleRecipes().subList(0, amount);
    }

    public static Stream<String> getSampleUUIDs() {
        return Stream.of(
                "bc13688d-17ae-4cd8-a530-b55dbb3f4036",
                "de6f2e21-3b1e-4e8b-9610-22a7fb0f3006",
                "cb507f78-0045-4805-a1f1-ef426303467e",
                "84596e39-412c-4813-8c0b-77ce0975d107",
                "258b5fa2-fad0-4f24-b0f2-33be5dd05b4f",
                "fbabea20-bfab-454b-afbe-a6696093c2bc",
                "23457a18-3f09-4b0e-aa10-4b497713a6bb",
                "58000261-5048-4031-9cec-42afc9296409",
                "58f12972-f0ca-405f-a286-dae6c52d7046",
                "fffa5f32-c451-4f06-91e6-2857a8982eb5"
        );
    }
}
