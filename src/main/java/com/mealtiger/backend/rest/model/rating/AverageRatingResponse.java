package com.mealtiger.backend.rest.model.rating;

public class AverageRatingResponse {

    private final double ratingValue;

    public AverageRatingResponse(double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public double getRatingValue() {
        return ratingValue;
    }
}
