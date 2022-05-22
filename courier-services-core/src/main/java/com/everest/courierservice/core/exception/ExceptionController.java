package com.everest.courierservice.core.exception;

import lombok.extern.slf4j.Slf4j;

import static com.everest.courierservice.core.constants.HttpStatusConstants.*;

@Slf4j
public class ExceptionController {

    public ExceptionController() {
    }

    static String errorResponse = null;
    public static String getErrorResponse(Exception e) {
        if (e instanceof InputValidationException) {
            log.error("in input validation exception");
            errorResponse = BAD_REQUEST_STATUS_CODE + " - " + e.getMessage();
        } else if (e instanceof ServiceException) {
            log.error("in not found exceptiom with exception");
            errorResponse = UNKNOW_ERROR_STATUS_CODE + " - " + e.getMessage();
        } else {
            log.error("in generic exceptiom with exception");
            errorResponse = UNKNOW_ERROR_STATUS_CODE + " - " + e.getMessage();
        }
        return errorResponse;
    }

}
