package com.mealtiger.backend.configuration.exceptions;

/**
 * This exception may be thrown when a given property cannot be accessed.
 *
 * @author Lucca Greschner
 */
public class ConfigPropertyException extends RuntimeException {

    public ConfigPropertyException(String property) {
        super("Error when trying to retrieve config property " + property + "!");
    }
}
