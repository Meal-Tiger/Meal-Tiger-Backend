package com.mealtiger.backend.configuration.exceptions;

/**
 * This exception may be thrown when a given config cannot be loaded.
 *
 * @author Lucca Greschner
 */
public class ConfigLoadingException extends RuntimeException {
    public ConfigLoadingException(String msg) {
        super(msg);
    }

    public ConfigLoadingException(Exception e) {
        super("Could not load config: " + e.getMessage());
    }
}
