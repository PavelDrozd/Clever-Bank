package com.clevertec.bankmanager.web.rest.controller.commands.error;


import com.clevertec.bankmanager.data.dto.ErrorDto;
import com.clevertec.bankmanager.shared.util.mapper.GsonMapper;
import com.clevertec.bankmanager.web.rest.controller.commands.RestCommand;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;

/**
 * This class is an implementation of RestCommand interface for
 * create an error send it in JSON format.
 */
public class RestErrorCommand implements RestCommand {

    /** Gson used for mapping */
    private final Gson gson = GsonMapper.INSTANCE.getInstance();

    /**
     * Method create error, set in attribute and map to JSON.
     *
     * @param req expected HttpServletRequest.
     * @return String value of the command execution result to JSON.
     */
    @Override
    public String execute(HttpServletRequest req) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage("Something went wrong...");
        return gson.toJson(errorDto);
    }

}
