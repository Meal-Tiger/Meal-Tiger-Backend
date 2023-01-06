package com.mealtiger.backend.database.model.recipe;

public class Rating {

    private final int ratingValue;

    private String userId;

    public Rating(int ratingValue, String userId) {
        this.ratingValue = ratingValue;
        this.userId = userId;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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
