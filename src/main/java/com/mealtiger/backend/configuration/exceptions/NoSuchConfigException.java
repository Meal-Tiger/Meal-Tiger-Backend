package com.mealtiger.backend.configuration.exceptions;

public class NoSuchConfigException extends Exception {
    public NoSuchConfigException(String config) {
        super("Could not find the following config: " + config);
    }
}
