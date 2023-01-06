package com.mealtiger.backend.database.model.recipe;

import javax.validation.constraints.Min;
import java.util.Objects;

/**
 * This class serves as a model for an ingredient.
 *
 */
public class Ingredient {

    /**
     *  Amount of the chosen unit, e.g. 100 grams
     */
    @Min(value = 1, message = "Minimum amount is 1!")
    private int amount;
    /**
     * Unit of the ingredient, e.g. milliliters
     **/
    private String unit;
    /**
     * Name of the ingredient, e.g. flour
     */
    private String name;

    public Ingredient(int amount, String unit, String name) {
        this.amount = amount;
        this.unit = unit;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return amount == that.amount && unit.equals(that.unit) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, unit, name);
    }
}