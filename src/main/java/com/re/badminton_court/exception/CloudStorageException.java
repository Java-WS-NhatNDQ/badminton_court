package com.re.badminton_court.exception;

import org.springframework.http.HttpStatus;

public class CloudStorageException extends RuntimeException {
    private final HttpStatus status;

    public CloudStorageException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public CloudStorageException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
