package com.mealtiger.backend.database.model.recipe;

import com.mealtiger.backend.SampleSource;
import com.mealtiger.backend.rest.model.rating.RatingRequest;
import com.mealtiger.backend.rest.model.rating.RatingResponse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class RatingTest {

    @Test
    void ratingToResponseTest() {
        Rating rating = new Rating(SampleSource.SAMPLE_RATING_ID, 3, "Test", SampleSource.SAMPLE_USER_ID);
        RatingResponse response = (RatingResponse) rating.toResponse();

        assertEquals(rating.getId(), response.getId());
        assertEquals(rating.getRatingValue(), response.getRatingValue());
        assertEquals(rating.getComment(), response.getComment());
        assertEquals(rating.getUserId(), response.getUserId());
    }

    @Test
    void requestToRatingTest() {
        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.setRatingValue(4);
        ratingRequest.setComment("Test comment");

        Rating rating = ratingRequest.toEntity();

        assertEquals(ratingRequest.getRatingValue(), rating.getRatingValue());
        assertEquals(ratingRequest.getComment(), rating.getComment());
        assertNull(rating.getUserId());
    }

    @Test
    void equalsHashCodeTest() {
        Rating reference = new Rating(SampleSource.SAMPLE_RATING_ID, 2, "Some comment", SampleSource.SAMPLE_USER_ID);
        Rating differentId = new Rating(SampleSource.getSampleUUIDs().get(0), 2, "Some comment", SampleSource.SAMPLE_USER_ID);
        Rating differentRatingValue = new Rating(SampleSource.SAMPLE_RATING_ID, 3, "Some comment", SampleSource.SAMPLE_USER_ID);
        Rating differentUserId = new Rating(SampleSource.SAMPLE_RATING_ID, 2, "Some comment", SampleSource.SAMPLE_OTHER_USER_ID);

        assertEquals(reference, reference);
        assertEquals(reference, differentId);
        assertNotEquals(reference, differentRatingValue);
        assertNotEquals(reference, differentUserId);

        assertEquals(reference.hashCode(), reference.hashCode());
        assertEquals(reference.hashCode(), differentId.hashCode());
        assertNotEquals(reference.hashCode(), differentRatingValue.hashCode());
        assertNotEquals(reference.hashCode(), differentUserId.hashCode());
    }

}
