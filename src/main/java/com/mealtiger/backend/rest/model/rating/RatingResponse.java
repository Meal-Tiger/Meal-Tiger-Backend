package com.mealtiger.backend.rest.model.rating;

import com.mealtiger.backend.rest.model.Response;

public class RatingResponse implements Response {

    // FIELDS

    private final String id;

    private final int ratingValue;

    private final String comment;

    private final String userId;

    // CONSTRUCTOR

    public RatingResponse(String id, String userId, int ratingValue, String comment) {
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


}
