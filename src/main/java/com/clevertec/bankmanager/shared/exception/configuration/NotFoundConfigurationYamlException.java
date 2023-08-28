package com.clevertec.bankmanager.shared.exception.configuration;

public class NotFoundConfigurationYamlException extends ConfigurationException {

    public NotFoundConfigurationYamlException() {
        super();
    }

    public NotFoundConfigurationYamlException(String message) {
        super(message);
    }

    public NotFoundConfigurationYamlException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundConfigurationYamlException(Throwable cause) {
        super(cause);
    }

    protected NotFoundConfigurationYamlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
