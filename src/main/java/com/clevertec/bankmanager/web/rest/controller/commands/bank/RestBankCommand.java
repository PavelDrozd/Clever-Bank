package com.clevertec.bankmanager.web.rest.controller.commands.bank;

import com.clevertec.bankmanager.data.dto.BankDto;
import com.clevertec.bankmanager.service.BankService;
import com.clevertec.bankmanager.shared.exception.web.controller.ControllerIllegalArgumentException;
import com.clevertec.bankmanager.shared.util.mapper.GsonMapper;
import com.clevertec.bankmanager.web.rest.controller.commands.RestCommand;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

/**
 * This class is an implementation of RestCommand interface for
 * get bank from service and send it in JSON format.
 */
@RequiredArgsConstructor
public class RestBankCommand implements RestCommand {

    /** BankService is used to get objects from Service module. */
    private final BankService bankService;
    /** Gson used for mapping. */
    private final Gson gson = GsonMapper.INSTANCE.getInstance();

    /**
     * Method get bank from BankService, set in attribute bank and status, map to JSON.
     *
     * @param req expected HttpServletRequest.
     * @return String value of the command execution result to JSON.
     */
    @Override
    public String execute(HttpServletRequest req) {
        Long id = getValidParameter(req.getParameter("id"));
        BankDto bank = bankService.getById(id);
        req.setAttribute("bank", bank);
        req.setAttribute("status", 200);
        return gson.toJson(bank);
    }

    /**
     * Method check String parameter and parse it to Long
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
