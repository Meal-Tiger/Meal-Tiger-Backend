package com.mealtiger.backend.rest.model.user;

import com.mealtiger.backend.rest.model.Response;

public class UserInformationResponse implements Response {

    private final String userId;

    private final String username;

    private final String profilePictureId;

    public UserInformationResponse(String userId, String username, String profilePictureId) {
        this.userId = userId;
        this.username = username;
        this.profilePictureId = profilePictureId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePictureId() {
        return profilePictureId;
    }
}
