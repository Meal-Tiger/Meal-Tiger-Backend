package com.mealtiger.backend;

import com.mealtiger.backend.database.model.recipe.Ingredient;
import com.mealtiger.backend.database.model.recipe.Rating;
import com.mealtiger.backend.database.model.recipe.Recipe;

import java.util.List;
import java.util.UUID;

public class SampleSource {

    public static final String SAMPLE_USER_ID = "123e4567-e89b-12d3-a456-42661417400";
    public static final String SAMPLE_OTHER_USER_ID = "bc2ef248-91a5-4f06-86f1-726b412170ca";
    public static final String SAMPLE_IMAGE_ID = "9120d6cd-8820-464e-9bb1-d2fa049ce57b";
    public static final String SAMPLE_RATING_ID = "c8fd03f0-23b3-443f-9af9-c69fd72c8b4f";

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
                                new Rating(getSampleUUIDs().get(0), 4, "Pretty tasty - but too much sugar.", SAMPLE_USER_ID),
                                new Rating(getSampleUUIDs().get(1), 3, "I'm allergic...", SAMPLE_USER_ID)
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
                                new Rating(getSampleUUIDs().get(0), 1, "Did you know that cashew shells are actually poisonous. " +
                                        "Thus workers are harmed when peeling them. Please, use other nuts!", SAMPLE_USER_ID),
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
                                new Rating(getSampleUUIDs().get(0), 2, "Way too much for one person!", SAMPLE_USER_ID),
                                new Rating(getSampleUUIDs().get(1), 5, "Could be more, but really tasty!", SAMPLE_USER_ID)
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
                                new Rating(getSampleUUIDs().get(0), 1, "Too much sugar!", SAMPLE_USER_ID),
                                new Rating(getSampleUUIDs().get(1), 3, "", SAMPLE_USER_ID),
                                new Rating(getSampleUUIDs().get(2), 5, "My favorite!", SAMPLE_USER_ID)
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
                                new Rating(getSampleUUIDs().get(0), 4, "Very good!", SAMPLE_USER_ID),
                                new Rating(getSampleUUIDs().get(1), 3, "Bit too much sugar for me!",SAMPLE_USER_ID)
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
                                new Rating(getSampleUUIDs().get(0),2, "Meh!", SAMPLE_USER_ID),
                                new Rating(getSampleUUIDs().get(1), 1, "Yuck!", SAMPLE_USER_ID)
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
                                new Rating(getSampleUUIDs().get(0), 3, "Meh!", SAMPLE_USER_ID),
                                new Rating(getSampleUUIDs().get(1),1, "Yuck!", SAMPLE_USER_ID)
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
                                new Rating(getSampleUUIDs().get(0), 3, "It's okay", SAMPLE_USER_ID),
                                new Rating(getSampleUUIDs().get(1), 2, "",SAMPLE_USER_ID),
                                new Rating(getSampleUUIDs().get(2), 1, "Worst I have ever eaten!", SAMPLE_USER_ID)
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
                                new Rating(getSampleUUIDs().get(0), 4, "Genius invention - especially for college students!", SAMPLE_USER_ID)
                        },
                        15,
                        new UUID[]{}
                )
        );
    }

    public static List<Recipe> getSampleRecipes(int amount) {
        return getSampleRecipes().subList(0, amount);
    }

    public static List<String> getSampleUUIDs() {
        return List.of(
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
