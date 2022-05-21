package com.everest.courierservice.core.exception;

public class NotFoundException extends Exception {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

}
