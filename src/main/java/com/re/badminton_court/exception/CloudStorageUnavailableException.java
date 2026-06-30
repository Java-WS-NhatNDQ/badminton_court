package com.re.badminton_court.exception;

import org.springframework.http.HttpStatus;

public class CloudStorageUnavailableException extends CloudStorageException {

    public CloudStorageUnavailableException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE);
    }

    public CloudStorageUnavailableException(String message, Throwable cause) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE, cause);
    }
}
