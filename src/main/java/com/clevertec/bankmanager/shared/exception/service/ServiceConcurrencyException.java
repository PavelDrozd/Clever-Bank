package com.clevertec.bankmanager.shared.exception.service;

public class ServiceConcurrencyException extends ServiceException {

    public ServiceConcurrencyException() {
        super();
    }

    public ServiceConcurrencyException(String message) {
        super(message);
    }

    public ServiceConcurrencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceConcurrencyException(Throwable cause) {
        super(cause);
    }

    protected ServiceConcurrencyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
