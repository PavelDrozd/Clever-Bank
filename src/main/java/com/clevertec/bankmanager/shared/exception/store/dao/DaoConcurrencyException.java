package com.clevertec.bankmanager.shared.exception.store.dao;

public class DaoConcurrencyException extends DaoException {

    public DaoConcurrencyException() {
        super();
    }

    public DaoConcurrencyException(String message) {
        super(message);
    }

    public DaoConcurrencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoConcurrencyException(Throwable cause) {
        super(cause);
    }

    protected DaoConcurrencyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
