package com.mealtiger.backend.configuration.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to indicate a method that returns a config property.
 *
 * @author Lucca Greschner
 * @see com.mealtiger.backend.configuration.Configurator
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConfigNode {
    /**
     * Represents a property from a config file. Example: Database.mongoDBURL
     */
    String name();

    /**
     * If changed an environment variable of this name will override the config setting
     */
    String envKey() default "";

    /**
     * Spring properties associated with the config node.
     */
    String[] springProperties() default {};
}
