package com.mealtiger.backend.rest.model.rating;

import com.mealtiger.backend.database.model.recipe.Rating;
import com.mealtiger.backend.rest.model.Request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@SuppressWarnings("unused")
public class RatingRequest implements Request<Rating> {

    @Min(value = 1, message = "Minimum rating is 1!")
    @Max(value = 5, message = "Maximum rating is 5!")
    private int ratingValue;

    private String comment;

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public Rating toEntity() {
        return new Rating(null, getRatingValue(), getComment(), null);
    }
}
