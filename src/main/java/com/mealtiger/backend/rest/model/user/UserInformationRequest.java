package com.mealtiger.backend.rest.model.user;

import com.mealtiger.backend.database.model.image_metadata.validation.ImageExists;
import com.mealtiger.backend.database.model.user.UserMetadata;
import com.mealtiger.backend.rest.model.Request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserInformationRequest implements Request<UserMetadata> {

    @NotNull
    @NotEmpty
    private final String username;

    @ImageExists
    private final String profilePictureId;


    public UserInformationRequest(String username, String profilePictureId) {
        this.username = username;
        this.profilePictureId = profilePictureId;
    }

    // GETTERS

    public String getUsername() {
        return username;
    }

    public String getProfilePictureId() {
        return profilePictureId;
    }

    @Override
    public UserMetadata toEntity() {
        return new UserMetadata(null, username, profilePictureId);
    }
}
