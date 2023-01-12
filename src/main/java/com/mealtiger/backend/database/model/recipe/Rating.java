package com.mealtiger.backend.database.model.recipe;

import com.mealtiger.backend.rest.model.QueriedObject;
import com.mealtiger.backend.rest.model.Response;
import com.mealtiger.backend.rest.model.rating.RatingResponse;

/**
 * This class serves as the model for a rating.
 */
public class Rating implements QueriedObject {

    private String id;

    private final int ratingValue;

    private final String comment;

    private String userId;

    public Rating(String id, int ratingValue, String comment, String userId) {
        this.id = id;
        this.ratingValue = ratingValue;
        this.comment = comment;
        this.userId = userId;
    }

    // GETTER

    public String getId() {
        return id;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public String getComment() {
        return comment;
    }

    public String getUserId() {
        return userId;
    }

    // SETTER

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // EQUALS, HASHCODE, TOSTRING

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rating rating = (Rating) o;

        if (getRatingValue() != rating.getRatingValue()) return false;
        return getUserId().equals(rating.getUserId());
    }

    @Override
    public int hashCode() {
        int result = getRatingValue();
        result = 31 * result + getUserId().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "ratingValue=" + ratingValue +
                ", userId='" + userId + '\'' +
                '}';
    }

    @Override
    public Response toResponse() {
        return new RatingResponse(id, getUserId(), getRatingValue(), getComment());
    }
}
