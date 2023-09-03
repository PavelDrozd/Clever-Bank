package com.clevertec.bankmanager.web.rest.controller.factory;


import com.clevertec.bankmanager.web.rest.controller.commands.RestCommand;

/**
 * RestCommandFactory create commands for rest controller.
 * Enum are available globally, and used as a singleton.
 */
public enum RestCommandFactory {
    INSTANCE;

    /**
     * Method processes a String command value and uses RestCommandRegister to receive commands.
     *
     * @param command expected String command name.
     * @return object type of RestCommand.
     */
    public RestCommand getCommand(String command) {
        RestCommand commandInstance = RestCommandRegister.valueOf(command.toUpperCase()).getCommand();
        if (commandInstance == null) {
            commandInstance = RestCommandRegister.valueOf("ERROR").getCommand();
        }
        return commandInstance;
    }
}
