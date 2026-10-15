package com.whoopmock.urlshortener.exceptions;

// return 500 response
public class FailedToCreateUrlException extends RuntimeException {
    public FailedToCreateUrlException(String errorMessage) {
        super(errorMessage);
    }
}
