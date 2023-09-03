package com.clevertec.bankmanager.web.rest.controller.commands.bank;

import com.clevertec.bankmanager.data.dto.BankDto;
import com.clevertec.bankmanager.service.BankService;
import com.clevertec.bankmanager.shared.util.mapper.GsonMapper;
import com.clevertec.bankmanager.web.rest.controller.commands.RestCommand;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * This class is an implementation of RestCommand interface for
 * get all banks from service and send it in JSON format.
 */
@RequiredArgsConstructor
public class RestBanksCommand implements RestCommand {

    /** BankService is used to get objects from Service module. */
    private final BankService bankService;
    /** Gson used for mapping. */
    private final Gson gson = GsonMapper.INSTANCE.getInstance();

    /**
     * Method getting all banks from BankService, set in attribute banks and status, map to JSON.
     *
     * @param req expected HttpServletRequest.
     * @return String value of the command execution result to JSON.
     */
    @Override
    public String execute(HttpServletRequest req) {
        List<BankDto> banks = bankService.getAll();
        req.setAttribute("banks", banks);
        req.setAttribute("status", 200);
        return gson.toJson(banks);
    }
}
