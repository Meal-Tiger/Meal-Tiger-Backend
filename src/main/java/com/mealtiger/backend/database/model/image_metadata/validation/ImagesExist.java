package com.mealtiger.backend.database.model.image_metadata.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * This annotation is used to mark fields that may be validated by ImageValidator.
 */
@Target( { ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageArrayValidator.class)
public @interface ImagesExist {

    String message() default "Image does not exist yet!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
