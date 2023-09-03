package com.clevertec.bankmanager.web.rest.controller.commands.user;


import com.clevertec.bankmanager.data.dto.UserDto;
import com.clevertec.bankmanager.service.UserService;
import com.clevertec.bankmanager.shared.exception.web.controller.ControllerIllegalArgumentException;
import com.clevertec.bankmanager.shared.util.mapper.GsonMapper;
import com.clevertec.bankmanager.web.rest.controller.commands.RestCommand;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

/**
 * This class is an implementation of RestCommand interface for
 * get user from service and send it in JSON format.
 */
@RequiredArgsConstructor
public class RestUserCommand implements RestCommand {

    /** UserService is used to get objects from Service module. */
    private final UserService userService;
    /** Gson used for mapping. */
    private final Gson gson = GsonMapper.INSTANCE.getInstance();

    /**
     * Method get user from UserService, set in attribute user and status, map to JSON.
     *
     * @param req expected HttpServletRequest.
     * @return String value of the command execution result to JSON.
     */
    @Override
    public String execute(HttpServletRequest req) {
        Long id = getValidParameter(req.getParameter("id"));
        UserDto user = userService.getById(id);
        req.setAttribute("user", user);
        req.setAttribute("status", 200);
        return gson.toJson(user);
    }

    /**
     * Method check String parameter and parse it to Long.
     *
     * @param parameter expected String value.
     * @return Long number of parameter.
     */
    private Long getValidParameter(String parameter) {
        if (parameter == null || parameter.isEmpty() || parameter.matches("\\D")) {
            throw new ControllerIllegalArgumentException("Required parameter is not valid");
        }
        return Long.parseLong(parameter);
    }
}
