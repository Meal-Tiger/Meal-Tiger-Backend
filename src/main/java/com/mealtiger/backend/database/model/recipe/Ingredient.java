package com.mealtiger.backend.database.model.recipe;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotEmpty
    @NotNull
    private String unit;
    /**
     * Name of the ingredient, e.g. flour
     */
    @NotEmpty
    @NotNull
    private String name;

    public Ingredient(int amount, String unit, String name) {
        this.amount = amount;
        this.unit = unit;
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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