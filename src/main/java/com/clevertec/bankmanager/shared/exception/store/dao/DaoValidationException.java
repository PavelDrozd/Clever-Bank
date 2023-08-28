package com.clevertec.bankmanager.shared.exception.store.dao;

public class DaoValidationException extends DaoException{

    public DaoValidationException() {
        super();
    }

    public DaoValidationException(String message) {
        super(message);
    }

    public DaoValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoValidationException(Throwable cause) {
        super(cause);
    }

    protected DaoValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
