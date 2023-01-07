package com.mealtiger.backend.rest.model.recipe;

public class RatingResponse {

    private final double ratingValue;

    public RatingResponse(double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public double getRatingValue() {
        return ratingValue;
    }
}
