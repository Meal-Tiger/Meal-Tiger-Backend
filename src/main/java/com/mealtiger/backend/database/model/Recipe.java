package com.mealtiger.backend.database.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This class serves as a model for the recipes stored in database.
 *
 * @author Sebastian Maier
 */

@Document
public class Recipe {

    @Id
    private String id;
    @Indexed(direction = IndexDirection.ASCENDING)
    private String title;

    public Recipe(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Id: " + id + ", Title: " + title;
    }
}
