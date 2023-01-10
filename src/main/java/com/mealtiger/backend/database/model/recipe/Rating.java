package com.mealtiger.backend.database.model.recipe;

/**
 * This class serves as the model for a rating.
 */
public class Rating {

    private final int ratingValue;

    private final String comment;

    private String userId;

    public Rating(int ratingValue, String comment, String userId) {
        this.ratingValue = ratingValue;
        this.comment = comment;
        this.userId = userId;
    }

    // GETTER

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
}
