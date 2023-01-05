package com.mealtiger.backend.database.model.image_metadata;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "imagemetadata")
public class ImageMetadata {

    @Id
    String uuid;

    @Indexed
    String userId;

    @PersistenceCreator
    public ImageMetadata(String uuid, String userId) {
        this.uuid = uuid;
        this.userId = userId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

        if (!getUuid().equals(that.getUuid())) return false;
        return getUserId().equals(that.getUserId());
    }

    @Override
    public int hashCode() {
        int result = getUuid().hashCode();
        result = 31 * result + getUserId().hashCode();
        return result;
    }
}
