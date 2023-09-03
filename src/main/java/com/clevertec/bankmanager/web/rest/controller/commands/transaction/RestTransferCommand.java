package com.clevertec.bankmanager.web.rest.controller.commands.transaction;

import com.clevertec.bankmanager.data.dto.TransactionDto;
import com.clevertec.bankmanager.service.TransactionService;
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
 * transfer transaction from service and send it in JSON format.
 */
@RequiredArgsConstructor
public class RestTransferCommand implements RestCommand {

    /** TransactionService is used to get objects from Service module. */
    private final TransactionService transactionService;
    /** Gson used for mapping. */
    private final Gson gson = GsonMapper.INSTANCE.getInstance();

    /**
     * Method use transfer amount from one account to second account by AccountService,
     * update and set in attribute transaction and status, map to JSON.
     *
     * @param req expected HttpServletRequest.
     * @return String value of the command execution result to JSON.
     */
    @Override
    public String execute(HttpServletRequest req) {
        JsonObject jsonObject = readJsonObject(req);
        Long senderAccountId = jsonObject.getAsJsonPrimitive("senderAccountId").getAsLong();
        Long recipientAccountId = jsonObject.getAsJsonPrimitive("recipientAccountId").getAsLong();
        Double value = jsonObject.getAsJsonPrimitive("value").getAsDouble();
        TransactionDto transaction = transactionService.transfer(senderAccountId, recipientAccountId, value);
        req.setAttribute("transaction", transaction);
        req.setAttribute("status", 200);
        return gson.toJson(transaction);
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
