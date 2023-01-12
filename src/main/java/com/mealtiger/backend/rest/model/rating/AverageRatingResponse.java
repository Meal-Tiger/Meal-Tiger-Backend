package com.mealtiger.backend.rest.model.rating;

import com.mealtiger.backend.rest.model.Response;

/**
 * Response entity for an average rating.
 */
public class AverageRatingResponse implements Response {

    private final double ratingValue;

    public AverageRatingResponse(double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public double getRatingValue() {
        return ratingValue;
    }
}
