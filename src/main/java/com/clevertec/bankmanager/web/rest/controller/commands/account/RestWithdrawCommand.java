package com.clevertec.bankmanager.web.rest.controller.commands.account;

import com.clevertec.bankmanager.data.dto.AccountDto;
import com.clevertec.bankmanager.service.AccountService;
import com.clevertec.bankmanager.shared.exception.web.controller.ControllerIOException;
import com.clevertec.bankmanager.shared.util.mapper.GsonMapper;
import com.clevertec.bankmanager.shared.util.reader.DataInputStreamReader;
import com.clevertec.bankmanager.web.rest.controller.commands.RestCommand;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * This class is an implementation of RestCommand interface for
 * withdraw from account by service and send it in JSON format.
 */
@RequiredArgsConstructor
public class RestWithdrawCommand implements RestCommand {

    /** AccountService is used to get objects from Service module. */
    private final AccountService accountService;
    /** Gson used for mapping. */
    private final Gson gson = GsonMapper.INSTANCE.getInstance();

    /**
     * Method withdraw from account by AccountService, update and set in attribute account and status, map to JSON.
     *
     * @param req expected HttpServletRequest.
     * @return String value of the command execution result to JSON.
     */
    @Override
    public String execute(HttpServletRequest req) {
        JsonObject jsonObject = readJsonObject(req);
        Long id = jsonObject.getAsJsonPrimitive("id").getAsLong();
        Double value = jsonObject.getAsJsonPrimitive("value").getAsDouble();
        AccountDto updated = accountService.withdraw(id, value);
        req.setAttribute("account", updated);
        req.setAttribute("status", 200);
        return gson.toJson(updated);
    }

    /**
     * Method read account from HttpServletRequest.
     *
     * @param req expected HttpServletRequest.
     * @return object type of JsonObject.
     */
    private JsonObject readJsonObject(HttpServletRequest req) {
        String json;
        JsonObject jsonObject;
        try {
            json = DataInputStreamReader.getString(req.getInputStream());
            jsonObject = gson.fromJson(json, JsonObject.class);
        } catch (IOException e) {
            throw new ControllerIOException(e);
        }
        return jsonObject;
    }
}
