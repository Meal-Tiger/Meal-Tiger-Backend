package com.mealtiger.backend.rest.model;

import com.mealtiger.backend.database.model.user.UserMetadata;
import com.mealtiger.backend.rest.model.user.UserInformationRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static com.mealtiger.backend.SampleSource.SAMPLE_IMAGE_ID;
import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class UserInformationRequestTest {

    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
    }

    /**
     * This tests the conversion of UserInformationRequest to UserMetadata.
     */
    @Test
    void toEntityTest() {
        UserInformationRequest userInformationRequest = new UserInformationRequest("Sample username", SAMPLE_IMAGE_ID);

        UserMetadata userMetadata = userInformationRequest.toEntity();

        assertEquals("Sample username", userMetadata.getUsername());
        assertEquals(SAMPLE_IMAGE_ID, userMetadata.getProfilePictureId());
        assertNull(userMetadata.getUserId());
    }

    /**
     * This tests the validation of UserInformationRequest.
     */
    @Test
    void validationTest() {
        UserInformationRequest userInformationRequest = new UserInformationRequest("Sample username", null);
        assertTrue(validator.validate(userInformationRequest).isEmpty());

        // PROFILE PICTURE EMPTY
        userInformationRequest = new UserInformationRequest("Sample username", "");
        assertTrue(validator.validate(userInformationRequest).isEmpty());

        // USERNAME EMPTY
        userInformationRequest = new UserInformationRequest("", null);
        assertFalse(validator.validate(userInformationRequest).isEmpty());

        // USERNAME NULL
        userInformationRequest = new UserInformationRequest(null, null);
        assertFalse(validator.validate(userInformationRequest).isEmpty());

        // NON-EXISTENT IMAGE
        userInformationRequest = new UserInformationRequest("Sample username", "Test");
        assertFalse(validator.validate(userInformationRequest).isEmpty());
    }

}
