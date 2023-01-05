package com.mealtiger.backend.rest.error_handling.exceptions;

public class InvalidRequestFormatException extends RuntimeException {

    public InvalidRequestFormatException(String message) {
        super(message);
    }
}
