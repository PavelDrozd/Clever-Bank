package com.clevertec.bankmanager.web.rest.controller.commands.bank;

import com.clevertec.bankmanager.data.dto.BankDto;
import com.clevertec.bankmanager.service.BankService;
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
 * update bank in service and send it in JSON format.
 */
@RequiredArgsConstructor
public class RestUpdateBankCommand implements RestCommand {

    /** BankService is used to get objects from Service module. */
    private final BankService bankService;
    /** Gson used for mapping. */
    private final Gson gson = GsonMapper.INSTANCE.getInstance();

    /**
     * Method update bank in BankService, update and set in attribute bank and status, map to JSON.
     *
     * @param req expected HttpServletRequest.
     * @return String value of the command execution result to JSON.
     */
    @Override
    public String execute(HttpServletRequest req) {
        String bankJson = readJson(req);
        BankDto bank = processBankJson(bankJson);
        BankDto updated = bankService.update(bank);
        req.setAttribute("bank", updated);
        req.setAttribute("status", 200);
        return gson.toJson(updated);
    }

    /**
     * Method read bank from HttpServletRequest.
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
     * Method mapping to BankDto from String value of JSON.
     *
     * @param bankJson String value of JSON.
     * @return object type of BankDto.
     */
    private BankDto processBankJson(String bankJson) {
        return gson.fromJson(bankJson, BankDto.class);
    }
}
