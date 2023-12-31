package com.clevertec.bankmanager.web.rest.controller.commands.account;

import com.clevertec.bankmanager.data.dto.AccountDto;
import com.clevertec.bankmanager.service.AccountService;
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
 * create account by service and send it in JSON format.
 */
@RequiredArgsConstructor
public class RestCreateAccountCommand implements RestCommand {

    /** AccountService is used to get objects from Service module. */
    private final AccountService accountService;
    /** Gson used for mapping. */
    private final Gson gson = GsonMapper.INSTANCE.getInstance();

    /**
     * Method create account by AccountService, set in attribute account and status, map to JSON.
     *
     * @param req expected HttpServletRequest.
     * @return String value of the command execution result to JSON.
     */
    @Override
    public String execute(HttpServletRequest req) {
        String accountJson = readJson(req);
        AccountDto account = processAccountJson(accountJson);
        AccountDto created = accountService.create(account);
        req.setAttribute("account", created);
        req.setAttribute("status", 201);
        return gson.toJson(created);
    }

    /**
     * Method read account from HttpServletRequest.
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
     * Method mapping to AccountDto from String value of JSON.
     *
     * @param accountJson String value of JSON.
     * @return object type of AccountDto.
     */
    private AccountDto processAccountJson(String accountJson) {
        return gson.fromJson(accountJson, AccountDto.class);
    }
}
