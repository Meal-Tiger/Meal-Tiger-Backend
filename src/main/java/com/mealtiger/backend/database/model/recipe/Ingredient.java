package com.mealtiger.backend.database.model.recipe;

/**
 * This record serves as a model for an ingredient.
 *
 * @param amount Amount of the chosen unit, e.g. 100 grams
 * @param unit   Unit of the ingredient, e.g. milliliters
 * @param name   Name of the ingredient, e.g. flour
 */
public record Ingredient(int amount, String unit, String name) {
}
