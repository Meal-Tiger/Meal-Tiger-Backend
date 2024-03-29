package com.mealtiger.backend.database.model.image_metadata.validation;

import com.mealtiger.backend.configuration.Configurator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * This Validator is used to validate whether images exist yet or not.
 */
public class ImageArrayValidator implements ConstraintValidator<ImagesExist, UUID[]> {

    private final Configurator configurator;

    @SuppressWarnings("unused")
    public ImageArrayValidator() {
        this.configurator = new Configurator();
    }

    /**
     * Package-local constructor for unit tests.
     */
    ImageArrayValidator(Configurator configurator) {
        this.configurator = configurator;
    }

    @Override
    public void initialize(ImagesExist constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Checks if the images of given UUIDs exist.
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     *
     * @return true if all images exist, false if at least one does not exist.
     */
    @Override
    public boolean isValid(UUID[] value, ConstraintValidatorContext context) {
        if (value == null) return true;

        for (UUID imageUUID : value) {
            String imagePath = configurator.getString("Image.imagePath");

            if (!Files.exists(Path.of(imagePath, imageUUID.toString()))) {
                return false;
            }
        }
        return true;
    }
}
