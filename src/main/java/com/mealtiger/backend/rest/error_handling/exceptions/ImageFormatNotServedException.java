package com.mealtiger.backend.rest.error_handling.exceptions;

public class ImageFormatNotServedException extends RuntimeException {

    public ImageFormatNotServedException(String message) {
        super(message);
    }
}
