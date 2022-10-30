package com.mealtiger.backend.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to indicate a config-class
 *
 * @see com.mealtiger.backend.configuration.Configurator
 * @author Lucca Greschner
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Config {

    /**
     * Represents the name of the config.
     * Attention: This may not contain the character '.'!
     */
    String name();

    /**
     * Represents the path the config file is located at.
     */
    String configPath();
}
