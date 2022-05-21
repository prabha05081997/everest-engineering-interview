package com.everest.courierservice.core.exception;

public final class ServiceException extends Exception {

    private static final long serialVersionUID = 3265776478790920872L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);

    }

    public ServiceException(String message) {
        super(message);

    }

    public ServiceException(Throwable cause) {
        super(cause);

    }

}
