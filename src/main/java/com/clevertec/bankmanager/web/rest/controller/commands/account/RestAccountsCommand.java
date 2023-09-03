package com.clevertec.bankmanager.web.rest.controller.commands.account;

import com.clevertec.bankmanager.data.dto.AccountDto;
import com.clevertec.bankmanager.service.AccountService;
import com.clevertec.bankmanager.shared.util.mapper.GsonMapper;
import com.clevertec.bankmanager.web.rest.controller.commands.RestCommand;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * This class is an implementation of RestCommand interface for
 * get all accounts from service and send it in JSON format.
 */
@RequiredArgsConstructor
public class RestAccountsCommand implements RestCommand {

    /** AccountService is used to get objects from Service module. */
    private final AccountService accountService;
    /** Gson used for mapping. */
    private final Gson gson = GsonMapper.INSTANCE.getInstance();

    /**
     * Method getting all accounts from AccountService, set in attribute account and status, map to JSON.
     *
     * @param req expected HttpServletRequest.
     * @return String value of the command execution result to JSON.
     */
    @Override
    public String execute(HttpServletRequest req) {
        List<AccountDto> accounts = accountService.getAll();
        req.setAttribute("accounts", accounts);
        req.setAttribute("status", 200);
        return gson.toJson(accounts);
    }
}
