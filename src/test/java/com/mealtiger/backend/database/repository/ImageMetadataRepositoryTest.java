package com.mealtiger.backend.database.repository;

import com.mealtiger.backend.BackendApplication;
import com.mealtiger.backend.SampleSource;
import com.mealtiger.backend.database.model.image_metadata.ImageMetadata;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.mealtiger.backend.SampleSource.SAMPLE_IMAGE_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {BackendApplication.class}
)
@Tag("integration")
class ImageMetadataRepositoryTest {

    @Autowired
    private ImageMetadataRepository imageMetadataRepository;

    @BeforeEach
    @AfterEach
    void beforeAfterEach() {
        imageMetadataRepository.deleteAll();
    }

    @Test
    void findImageMetadatasByUserIdTest() {
        imageMetadataRepository.saveAll(List.of(
                new ImageMetadata(null, SampleSource.getSampleUUIDs().get(0)),
                new ImageMetadata(SAMPLE_IMAGE_ID, SampleSource.getSampleUUIDs().get(1)),
                new ImageMetadata(null, SampleSource.getSampleUUIDs().get(2)),
                new ImageMetadata(null, SampleSource.getSampleUUIDs().get(3)),
                new ImageMetadata(null, SampleSource.getSampleUUIDs().get(4)),
                new ImageMetadata(null, SampleSource.getSampleUUIDs().get(5)),
                new ImageMetadata(null, SampleSource.getSampleUUIDs().get(6)),
                new ImageMetadata(null, SampleSource.getSampleUUIDs().get(7))
        ));

        assertEquals(SAMPLE_IMAGE_ID, imageMetadataRepository.findImageMetadatasByUserId(SampleSource.getSampleUUIDs().get(1)).get(0).getId());
    }


}
