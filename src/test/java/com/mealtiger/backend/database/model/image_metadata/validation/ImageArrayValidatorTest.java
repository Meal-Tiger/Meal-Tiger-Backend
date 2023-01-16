package com.mealtiger.backend.database.model.image_metadata.validation;

import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.rest.Helper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static com.mealtiger.backend.SampleSource.SAMPLE_IMAGE_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("unit")
class ImageArrayValidatorTest {
    private static final String NON_EXISTENT_IMAGE_ID = "f2076c35-e8d1-4d34-82f1-0de7f370efbd";

    @Test
    void imageValidationTest() throws IOException {
        Configurator configurator = mock(Configurator.class);
        when(configurator.getString("Image.imagePath")).thenReturn("testImages/");

        Path imagePath = Path.of("testImages/", SAMPLE_IMAGE_ID);
        Files.createDirectories(imagePath);

        ImageArrayValidator validator = new ImageArrayValidator(configurator);

        assertTrue(validator.isValid(new UUID[]{UUID.fromString(SAMPLE_IMAGE_ID)}, null));
        assertFalse(validator.isValid(new UUID[]{UUID.fromString(NON_EXISTENT_IMAGE_ID)}, null));

        Helper.deleteFile(imagePath.getParent());
    }
}
