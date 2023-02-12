package com.mealtiger.backend.database.model.user;

import com.mealtiger.backend.SampleSource;
import com.mealtiger.backend.rest.model.user.UserInformationResponse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.mealtiger.backend.SampleSource.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

    /**
     * Tests the equals- and hashCode-methods
     */
    @Test
    void equalsHashCodeTest() {
        UserMetadata reference = new UserMetadata(SAMPLE_USER_ID, "Sample username", SAMPLE_IMAGE_ID);
        UserMetadata differentUserID = new UserMetadata(SAMPLE_OTHER_USER_ID, "Sample username", SAMPLE_IMAGE_ID);
        UserMetadata differentUsername = new UserMetadata(SAMPLE_USER_ID, "Sample other username", SAMPLE_IMAGE_ID);
        UserMetadata differentImageID = new UserMetadata(SAMPLE_USER_ID, "Sample username", SampleSource.getSampleUUIDs().get(0));

        assertEquals(reference, reference);
        assertNotEquals(reference, differentUserID);
        assertNotEquals(reference, differentUsername);
        assertNotEquals(reference, differentImageID);

        assertEquals(reference.hashCode(), reference.hashCode());
        assertNotEquals(reference.hashCode(), differentUserID.hashCode());
        assertNotEquals(reference.hashCode(), differentUsername.hashCode());
        assertNotEquals(reference.hashCode(), differentImageID.hashCode());
    }
}
