package com.mealtiger.backend.database.model.image_metadata;

import com.mealtiger.backend.SampleSource;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.mealtiger.backend.SampleSource.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Tag("unit")
class ImageMetadataTest {

    /**
     * Tests the equals- and hashCode-methods
     */
    @Test
    void equalsHashCodeTest() {
        ImageMetadata reference = new ImageMetadata(SAMPLE_IMAGE_ID, SAMPLE_USER_ID);
        ImageMetadata differentUserID = new ImageMetadata(SAMPLE_IMAGE_ID, SAMPLE_OTHER_USER_ID);
        ImageMetadata differentImageID = new ImageMetadata(SampleSource.getSampleUUIDs().get(0), SAMPLE_USER_ID);

        assertEquals(reference, reference);
        assertNotEquals(reference, differentUserID);
        assertNotEquals(reference, differentImageID);

        assertEquals(reference.hashCode(), reference.hashCode());
        assertNotEquals(reference.hashCode(), differentUserID.hashCode());
        assertNotEquals(reference.hashCode(), differentImageID.hashCode());
    }
}
