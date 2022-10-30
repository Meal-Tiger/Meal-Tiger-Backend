package com.mealtiger.backend.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to indicate a method that returns a config property.
 *
 * @see com.mealtiger.backend.configuration.Configurator
 * @author Lucca Greschner
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConfigNode {
    /**
     * Represents a property from a config file. Example: Database.mongoDBURL
     */
    String name();
}
