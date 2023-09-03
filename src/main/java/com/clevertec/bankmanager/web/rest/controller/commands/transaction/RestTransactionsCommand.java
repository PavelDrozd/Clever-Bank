package com.clevertec.bankmanager.web.rest.controller.commands.transaction;

import com.clevertec.bankmanager.data.dto.TransactionDto;
import com.clevertec.bankmanager.service.TransactionService;
import com.clevertec.bankmanager.shared.util.mapper.GsonMapper;
import com.clevertec.bankmanager.web.rest.controller.commands.RestCommand;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * This class is an implementation of RestCommand interface for
 * get all transactions from service and send it in JSON format.
 */
@RequiredArgsConstructor
public class RestTransactionsCommand implements RestCommand {

    /** TransactionService is used to get objects from Service module. */
    private final TransactionService transactionService;
    /** Gson used for mapping. */
    private final Gson gson = GsonMapper.INSTANCE.getInstance();

    /**
     * Method getting all transactions from TransactionService,
     * set in attribute transaction and status and map to JSON.
     *
     * @param req expected HttpServletRequest.
     * @return String value of the command execution result to JSON.
     */
    @Override
    public String execute(HttpServletRequest req) {
        List<TransactionDto> transactions = transactionService.getAll();
        req.setAttribute("transactions", transactions);
        req.setAttribute("status", 200);
        return gson.toJson(transactions);
    }
}
