package com.mealtiger.backend.configuration.exceptions;

/**
 * This exception may be thrown when a given property does not exist.
 *
 * @author Lucca Greschner
 */
public class NoSuchPropertyException extends Exception{
    public NoSuchPropertyException(String property) {
        super("Could not find the following property: " + property);
    }
}
