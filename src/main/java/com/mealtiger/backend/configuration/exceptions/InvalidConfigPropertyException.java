package com.mealtiger.backend.configuration.exceptions;

public class InvalidConfigPropertyException extends RuntimeException {

    public InvalidConfigPropertyException(String property, String explanation) {
        super("The config property " + property + " if of a wrong format: " + explanation);
    }
}
