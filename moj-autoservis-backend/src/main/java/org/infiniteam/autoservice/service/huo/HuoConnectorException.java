package org.infiniteam.autoservice.service.huo;

public class HuoConnectorException extends Exception {

    public HuoConnectorException() {
    }

    public HuoConnectorException(String message) {
        super(message);
    }

    public HuoConnectorException(String message, Throwable cause) {
        super(message, cause);
    }

    public HuoConnectorException(Throwable cause) {
        super(cause);
    }

    public HuoConnectorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
