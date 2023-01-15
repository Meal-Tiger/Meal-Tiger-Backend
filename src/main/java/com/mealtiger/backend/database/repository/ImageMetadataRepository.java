package com.mealtiger.backend.database.repository;

import com.mealtiger.backend.database.model.image_metadata.ImageMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageMetadataRepository extends MongoRepository<ImageMetadata, String> {

    /**
     * Find ImageMetadata by its user id.
     */
    List<ImageMetadata> findImageMetadatasByUserId(String userId);
}
