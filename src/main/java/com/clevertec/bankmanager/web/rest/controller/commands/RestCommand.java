package com.clevertec.bankmanager.web.rest.controller.commands;


import javax.servlet.http.HttpServletRequest;

/**
 * Interface for rest commands.
 */
public interface RestCommand {

    /**
     * Method executing rest controller command.
     *
     * @param req expected HttpServletRequest.
     * @return String value of the command execution result.
     */
    String execute(HttpServletRequest req);

}
