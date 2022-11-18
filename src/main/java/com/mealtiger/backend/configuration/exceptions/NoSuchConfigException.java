package com.mealtiger.backend.configuration.exceptions;

/**
 * This exception may be thrown when a given config does not exist.
 *
 * @author Lucca Greschner
 */
public class NoSuchConfigException extends RuntimeException {
    public NoSuchConfigException(String config) {
        super("Could not find the following config: " + config);
    }
}
