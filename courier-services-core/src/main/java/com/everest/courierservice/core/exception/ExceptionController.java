package com.everest.courierservice.core.exception;

import lombok.extern.slf4j.Slf4j;

import static java.net.HttpURLConnection.*;

@Slf4j
public class ExceptionController {

    public ExceptionController() {
    }

    static String errorResponse = null;
    public static String getErrorResponse(Exception e) {
        if (e instanceof InputValidationException) {
            log.error("in input validation exception");
            errorResponse = HTTP_BAD_REQUEST + " - " + e.getMessage();
        } else if (e instanceof ServiceException) {
            log.error("in service exception");
            errorResponse = HTTP_INTERNAL_ERROR + " - " + e.getMessage();
        } else {
            log.error("in generic exception");
            errorResponse = HTTP_INTERNAL_ERROR + " - " + e.getMessage();
        }
        return errorResponse;
    }

}
