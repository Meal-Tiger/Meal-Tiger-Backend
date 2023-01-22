package com.mealtiger.backend.database.model.image_metadata.validation;

import com.mealtiger.backend.configuration.Configurator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This Validator is used to validate whether a single image exists yet or not.
 */
public class ImageValidator implements ConstraintValidator<ImageExists, String> {

    private final Configurator configurator;

    @SuppressWarnings("unused")
    public ImageValidator() {
        this.configurator = new Configurator();
    }

    /**
     * Package-local constructor for unit tests.
     */
    ImageValidator(Configurator configurator) {
        this.configurator = configurator;
    }

    @Override
    public void initialize(ImageExists constaintAnnotation) {
        ConstraintValidator.super.initialize(constaintAnnotation);
    }

    /**
     * Checks if the image of given UUID exists.
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     *
     * @return true if image exists, false if it does not exist.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() == 0) return true;

        String imagePath = configurator.getString("Image.imagePath");

        return Files.exists(Path.of(imagePath, value));
    }

}
