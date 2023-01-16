package com.mealtiger.backend.database.repository;

import com.mealtiger.backend.database.model.user.UserMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserMetadataRepository extends MongoRepository<UserMetadata, String> {

}
