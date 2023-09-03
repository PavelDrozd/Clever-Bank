package com.clevertec.bankmanager.web.rest.controller.commands.bank;

import com.clevertec.bankmanager.data.dto.MessageDto;
import com.clevertec.bankmanager.service.BankService;
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
 * delete bank by service.
 */
@RequiredArgsConstructor
public class RestDeleteBankCommand implements RestCommand {

    /** BankService is used to get objects from Service module. */
    private final BankService bankService;
    /** Gson used for mapping. */
    private final Gson gson = GsonMapper.INSTANCE.getInstance();

    /**
     * Method delete bank by BankService, set in attribute message and map to JSON.
     *
     * @param req expected HttpServletRequest.
     * @return String value of the command execution result to JSON.
     */
    @Override
    public String execute(HttpServletRequest req) {
        JsonObject jsonId = readJsonObject(req);
        Long id = jsonId.getAsJsonPrimitive("id").getAsLong();
        bankService.delete(id);
        req.setAttribute("status", 204);
        MessageDto message = new MessageDto();
        message.setStatus(204);
        message.setMessage("Successfully deleted");
        return gson.toJson(message);
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
