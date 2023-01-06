package com.mealtiger.backend.rest.model.recipe;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@SuppressWarnings("unused")
public class RatingDTO {

    @Min(value = 1, message = "Minimum rating is 1!")
    @Max(value = 5, message = "Maximum rating is 5!")
    private int ratingValue;

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public int getRatingValue() {
        return ratingValue;
    }
}
