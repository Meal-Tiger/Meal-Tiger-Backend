package com.mealtiger.backend.database.model.user;

import com.mealtiger.backend.rest.model.user.UserInformationResponse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.mealtiger.backend.SampleSource.SAMPLE_IMAGE_ID;
import static com.mealtiger.backend.SampleSource.SAMPLE_USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
class UserMetadataTest {

    /**
     * Tests the conversion of user metadata to UserInformationResponse
     */
    @Test
    void toResponseTest() {
        UserMetadata userMetadata = new UserMetadata(SAMPLE_USER_ID, "Sample username", SAMPLE_IMAGE_ID);
        UserInformationResponse userInformationResponse = (UserInformationResponse) userMetadata.toResponse();

        assertEquals(userMetadata.getUserId(), userInformationResponse.getUserId());
        assertEquals(userMetadata.getUsername(), userInformationResponse.getUsername());
        assertEquals(userMetadata.getProfilePictureId(), userInformationResponse.getProfilePictureId());
    }
}
