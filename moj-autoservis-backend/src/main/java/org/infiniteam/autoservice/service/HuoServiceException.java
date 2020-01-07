package org.infiniteam.autoservice.service;

public class HuoServiceException extends Exception {

    public HuoServiceException() {
    }

    public HuoServiceException(String message) {
        super(message);
    }

    public HuoServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public HuoServiceException(Throwable cause) {
        super(cause);
    }

    public HuoServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
