package com.clevertec.bankmanager.web.rest.controller.commands.user;

import com.clevertec.bankmanager.data.dto.UserDto;
import com.clevertec.bankmanager.service.UserService;
import com.clevertec.bankmanager.shared.exception.web.controller.ControllerIOException;
import com.clevertec.bankmanager.shared.util.mapper.GsonMapper;
import com.clevertec.bankmanager.shared.util.reader.DataInputStreamReader;
import com.clevertec.bankmanager.web.rest.controller.commands.RestCommand;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * This class is an implementation of RestCommand interface for
 * create user by service and send it in JSON format.
 */
@RequiredArgsConstructor
public class RestCreateUserCommand implements RestCommand {

    /** UserService is used to get objects from Service module. */
    private final UserService userService;
    /** Gson used for mapping. */
    private final Gson gson = GsonMapper.INSTANCE.getInstance();

    /**
     * Method create user by UserService, set in attribute user and status, map to JSON.
     *
     * @param req expected HttpServletRequest.
     * @return String value of the command execution result to JSON.
     */
    @Override
    public String execute(HttpServletRequest req) {
        String userJson = readJson(req);
        UserDto user = processUserJson(userJson);
        UserDto created = userService.create(user);
        req.setAttribute("user", created);
        req.setAttribute("status", 201);
        return gson.toJson(created);
    }

    /**
     * Method read user from HttpServletRequest.
     *
     * @param req expected HttpServletRequest.
     * @return String value of JSON.
     */
    private String readJson(HttpServletRequest req) {
        String json;
        try {
            json = DataInputStreamReader.getString(req.getInputStream());
        } catch (IOException e) {
            throw new ControllerIOException(e);
        }
        return json;
    }

    /**
     * Method mapping to UserDto from String value of JSON.
     *
     * @param userJson String value of JSON.
     * @return object type of UserDto.
     */
    private UserDto processUserJson(String userJson) {
        return gson.fromJson(userJson, UserDto.class);
    }
}
