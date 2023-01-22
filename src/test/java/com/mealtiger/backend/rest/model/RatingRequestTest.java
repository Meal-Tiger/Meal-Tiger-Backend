package com.mealtiger.backend.rest.model;

import com.mealtiger.backend.rest.model.rating.RatingRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unit")
class RatingRequestTest {

    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
    }

    @Test
    void ratingDTOValidationTest() {
        RatingRequest testRating = new RatingRequest();
        testRating.setRatingValue(1);

        assertTrue(validator.validate(testRating).isEmpty());

        testRating.setRatingValue(2);

        assertTrue(validator.validate(testRating).isEmpty());

        testRating.setRatingValue(3);

        assertTrue(validator.validate(testRating).isEmpty());

        testRating.setRatingValue(4);

        assertTrue(validator.validate(testRating).isEmpty());

        testRating.setRatingValue(5);

        assertTrue(validator.validate(testRating).isEmpty());

        // NEGATIVE TESTS

        // VALUE TOO LOW

        testRating.setRatingValue(0);

        assertFalse(validator.validate(testRating).isEmpty());

        // VALUE TOO HIGH

        testRating.setRatingValue(6);

        assertFalse(validator.validate(testRating).isEmpty());
    }

}
