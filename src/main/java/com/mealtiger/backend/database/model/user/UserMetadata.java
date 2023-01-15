package com.mealtiger.backend.database.model.user;

import com.mealtiger.backend.rest.model.QueriedObject;
import com.mealtiger.backend.rest.model.Response;
import com.mealtiger.backend.rest.model.user.UserInformationResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usermetadata")
public class UserMetadata implements QueriedObject {

        @Id
        private String userId;
        private final String username;
        private final String profilePictureId;

    @PersistenceCreator
    public UserMetadata(String userId, String username, String profilePictureId){
        this.userId= userId;
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public Response toResponse() {
        return new UserInformationResponse(this.userId, this.username, this.profilePictureId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserMetadata that = (UserMetadata) o;

        if (getUserId() != null ? !getUserId().equals(that.getUserId()) : that.getUserId() != null) return false;
        if (!getUsername().equals(that.getUsername())) return false;
        return getProfilePictureId().equals(that.getProfilePictureId());
    }

    @Override
    public int hashCode() {
        int result = getUserId() != null ? getUserId().hashCode() : 0;
        result = 31 * result + getUsername().hashCode();
        result = 31 * result + getProfilePictureId().hashCode();
        return result;
    }
}
