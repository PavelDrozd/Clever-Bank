package com.clevertec.bankmanager.web.rest.controller.commands.bank;

import com.clevertec.bankmanager.data.dto.MessageDto;
import com.clevertec.bankmanager.service.BankService;
import com.clevertec.bankmanager.shared.exception.web.controller.ControllerIllegalArgumentException;
import com.clevertec.bankmanager.shared.util.mapper.GsonMapper;
import com.clevertec.bankmanager.web.rest.controller.commands.RestCommand;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

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
        Long id = getValidParameter(req.getParameter("id"));
        bankService.delete(id);
        req.setAttribute("status", 204);
        MessageDto message = new MessageDto();
        message.setStatus(204);
        message.setMessage("Successfully deleted");
        return gson.toJson(message);
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
