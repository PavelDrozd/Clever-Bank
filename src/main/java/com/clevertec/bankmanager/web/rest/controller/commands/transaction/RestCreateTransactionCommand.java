package com.clevertec.bankmanager.web.rest.controller.commands.transaction;

import com.clevertec.bankmanager.data.dto.TransactionDto;
import com.clevertec.bankmanager.service.TransactionService;
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
public class RestCreateTransactionCommand implements RestCommand {

    /** TransactionService is used to get objects from Service module. */
    private final TransactionService transactionService;
    /** Gson used for mapping. */
    private final Gson gson = GsonMapper.INSTANCE.getInstance();

    /**
     * Method create transaction by UserService, set in attribute transaction and status, map to JSON.
     *
     * @param req expected HttpServletRequest.
     * @return String value of the command execution result to JSON.
     */
    @Override
    public String execute(HttpServletRequest req) {
        String transactionJson = readJson(req);
        TransactionDto transaction = processUserJson(transactionJson);
        TransactionDto created = transactionService.create(transaction);
        req.setAttribute("transaction", created);
        req.setAttribute("status", 201);
        return gson.toJson(created);
    }

    /**
     * Method read transaction from HttpServletRequest.
     *
     * @param req expected HttpServletRequest.
     * @return String value of JSON.
     */
    private static String readJson(HttpServletRequest req) {
        String json;
        try {
            json = DataInputStreamReader.getString(req.getInputStream());
        } catch (IOException e) {
            throw new ControllerIOException(e);
        }
        return json;
    }

    /**
     * Method mapping to TransactionDto from String value of JSON.
     *
     * @param transactionJson String value of JSON.
     * @return object type of TransactionDto.
     */
    private TransactionDto processUserJson(String transactionJson) {
        return gson.fromJson(transactionJson, TransactionDto.class);
    }
}
