package com.everest.courierservice.core.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionController {

    public ExceptionController() {
    }

    static String errorResponse = null;
    public static String getErrorResponse(Exception e) {
        if (e instanceof InputValidationException) {
            log.error("in input validation exception");
            errorResponse = "400 - " + e.getMessage();
        } else if (e instanceof NotFoundException) {
            log.error("in not found exceptiom with exception");
            errorResponse = "404 - " + e.getMessage();
        } else {
            log.error("in generic exceptiom with exception");
            errorResponse = "500 - " + e.getMessage();
        }
        return errorResponse;
    }

}
