package com.clevertec.bankmanager.web.rest.controller.commands.transaction;

import com.clevertec.bankmanager.data.dto.TransactionDto;
import com.clevertec.bankmanager.service.TransactionService;
import com.clevertec.bankmanager.shared.exception.web.controller.ControllerIllegalArgumentException;
import com.clevertec.bankmanager.shared.util.mapper.GsonMapper;
import com.clevertec.bankmanager.web.rest.controller.commands.RestCommand;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

/**
 * This class is an implementation of RestCommand interface for
 * get transaction from service and send it in JSON format.
 */
@RequiredArgsConstructor
public class RestTransactionCommand implements RestCommand {

    /** TransactionService is used to get objects from Service module. */
    private final TransactionService transactionService;
    /** Gson used for mapping. */
    private final Gson gson = GsonMapper.INSTANCE.getInstance();

    /**
     * Method get transaction from TransactionService, set in attribute transaction and status, map to JSON.
     *
     * @param req expected HttpServletRequest.
     * @return String value of the command execution result to JSON.
     */
    @Override
    public String execute(HttpServletRequest req) {
        Long id = getValidParameter(req.getParameter("id"));
        TransactionDto transaction = transactionService.getById(id);
        req.setAttribute("transaction", transaction);
        req.setAttribute("status", 200);
        return gson.toJson(transaction);
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
