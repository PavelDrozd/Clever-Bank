package com.clevertec.bankmanager.web.rest.controller.commands.user;

import com.clevertec.bankmanager.data.dto.UserDto;
import com.clevertec.bankmanager.service.UserService;
import com.clevertec.bankmanager.shared.util.mapper.GsonMapper;
import com.clevertec.bankmanager.web.rest.controller.commands.RestCommand;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * This class is an implementation of RestCommand interface for
 * get all users from service and send it in JSON format.
 */
@RequiredArgsConstructor
public class RestUsersCommand implements RestCommand {

    /** UserService is used to get objects from Service module. */
    private final UserService userService;
    /** Gson used for mapping. */
    private final Gson gson = GsonMapper.INSTANCE.getInstance();

    /**
     * Method getting all users from UserService, set in attribute users and status, map to JSON.
     *
     * @param req expected HttpServletRequest.
     * @return String value of the command execution result to JSON.
     */
    @Override
    public String execute(HttpServletRequest req) {
        List<UserDto> users = userService.getAll();
        req.setAttribute("users", users);
        req.setAttribute("status", 200);
        return gson.toJson(users);
    }
}
