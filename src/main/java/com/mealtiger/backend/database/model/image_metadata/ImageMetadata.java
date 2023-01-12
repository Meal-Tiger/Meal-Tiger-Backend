package com.mealtiger.backend.database.model.image_metadata;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "image-metadata")
public class ImageMetadata {

    @Id
    String id;

    @Indexed
    String userId;

    @PersistenceCreator
    public ImageMetadata(String id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageMetadata that = (ImageMetadata) o;

        if (!getId().equals(that.getId())) return false;
        return getUserId().equals(that.getUserId());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getUserId().hashCode();
        return result;
    }
}
