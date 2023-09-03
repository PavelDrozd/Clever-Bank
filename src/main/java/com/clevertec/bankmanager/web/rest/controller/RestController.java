package com.clevertec.bankmanager.web.rest.controller;

import com.clevertec.bankmanager.data.dto.ErrorDto;
import com.clevertec.bankmanager.service.executor.CashbackScheduleExecutorService;
import com.clevertec.bankmanager.shared.exception.web.controller.ControllerException;
import com.clevertec.bankmanager.shared.exception.web.controller.ControllerIOException;
import com.clevertec.bankmanager.shared.exception.web.controller.ControllerInvalidCommandException;
import com.clevertec.bankmanager.shared.util.mapper.GsonMapper;
import com.clevertec.bankmanager.store.dao.connection.DataSourceManager;
import com.clevertec.bankmanager.web.rest.controller.commands.RestCommand;
import com.clevertec.bankmanager.web.rest.controller.factory.RestCommandFactory;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * RestController used for processing http methods and send response.
 * This class is a Servlet, extends HttpServlet.
 */
@WebServlet("/api/*")
@Slf4j
public class RestController extends HttpServlet {

    /** Gson used for mapping */
    private final Gson gson = GsonMapper.INSTANCE.getInstance();

    /**
     * HTTP method GET.
     *
     * @param req  an {@link HttpServletRequest} object that
     *             contains the request the client has made
     *             of the servlet
     * @param resp an {@link HttpServletResponse} object that
     *             contains the response the servlet sends
     *             to the client
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        log.debug("CONTROLLER METHOD DO GET");
        process(req, resp);
    }

    /**
     * HTTP method POST.
     *
     * @param req  an {@link HttpServletRequest} object that
     *             contains the request the client has made
     *             of the servlet
     * @param resp an {@link HttpServletResponse} object that
     *             contains the response the servlet sends
     *             to the client
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        log.debug("CONTROLLER METHOD DO POST");
        process(req, resp);
    }

    /**
     * HTTP method PUT.
     *
     * @param req  the {@link HttpServletRequest} object that
     *             contains the request the client made of
     *             the servlet
     * @param resp the {@link HttpServletResponse} object that
     *             contains the response the servlet returns
     *             to the client
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        log.debug("CONTROLLER METHOD DO PUT");
        process(req, resp);
    }

    /**
     * HTTP method DELETE.
     *
     * @param req  the {@link HttpServletRequest} object that
     *             contains the request the client made of
     *             the servlet
     * @param resp the {@link HttpServletResponse} object that
     *             contains the response the servlet returns
     *             to the client
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        log.debug("CONTROLLER METHOD DO DELETE");
        process(req, resp);
    }

    /** Servlet destroy method close CashbackScheduleExecutorService and DataSource. */
    @Override
    public void destroy() {
        CashbackScheduleExecutorService.getInstance().shutdown();
        DataSourceManager.INSTANCE.close();
        log.info("SERVLET DESTROYED");
    }

    /**
     * Method process request and response.
     *
     * @param req  the {@link HttpServletRequest} object that
     *             contains the request the client made of
     *             the servlet
     * @param resp the {@link HttpServletResponse} object that
     *             contains the response the servlet returns
     *             to the client
     */
    private void process(HttpServletRequest req, HttpServletResponse resp) {
        try {
            RestCommand commandInstance = processRequest(req);
            sendResponse(req, resp, commandInstance);
        } catch (Exception e) {
            log.error("CONTROLLER EXCEPTION " + e.getMessage());
            toErrorPage(req, resp, e);
        }
    }

    /**
     * Method process request to create commandInstance.
     *
     * @param req the {@link HttpServletRequest} object that
     *            contains the request the client made of
     *            the servlet
     * @return commandInstance of RestCommand.
     */
    private RestCommand processRequest(HttpServletRequest req) {
        String[] pathInfo = req.getPathInfo().split("/");
        String command = pathInfo[1];
        log.debug("CONTROLLER COMMAND: " + command);
        checkPathInfo(pathInfo);
        RestCommand commandInstance = RestCommandFactory.INSTANCE.getCommand(command);
        checkCommandInstance(commandInstance);
        return commandInstance;
    }

    /**
     * Method checks whether the command exists.
     *
     * @param pathInfo Array of Strings from path.
     */
    private void checkPathInfo(String[] pathInfo) {
        if (pathInfo[1] == null) {
            log.error("CONTROLLER INVALID COMMAND EXCEPTION: INVALID LINK");
            throw new ControllerInvalidCommandException("Invalid link");
        }
    }

    /**
     * Method checks whether the command instance exists.
     *
     * @param commandInstance object type of RestCommand.
     */
    private void checkCommandInstance(RestCommand commandInstance) {
        if (commandInstance == null) {
            log.error("CONTROLLER INVALID COMMAND EXCEPTION: INVALID COMMAND");
            throw new ControllerInvalidCommandException("Invalid command");
        }
    }

    /**
     * Method for process response.
     *
     * @param req             the {@link HttpServletRequest} object that
     *                        contains the request the client made of
     *                        the servlet
     * @param resp            the {@link HttpServletResponse} object that
     *                        contains the response the servlet returns
     *                        to the client
     * @param commandInstance object type of RestCommand.
     */
    private void sendResponse(HttpServletRequest req, HttpServletResponse resp, RestCommand commandInstance) {
        String jsonObject = commandInstance.execute(req);
        resp.setContentType("application/json");
        resp.setStatus((int) req.getAttribute("status"));
        writeResponse(resp, jsonObject);
    }

    /**
     * Method get response writer and write JSON into response.
     *
     * @param resp       the {@link HttpServletResponse} object that
     *                   contains the response the servlet returns
     *                   to the client
     * @param jsonObject String type of JSON.
     */
    private void writeResponse(HttpServletResponse resp, String jsonObject) {
        PrintWriter out = null;
        try {
            out = resp.getWriter();
        } catch (IOException e) {
            log.error("CONTROLLER IO EXCEPTION: " + e.getMessage());
            throw new ControllerIOException(e);
        }
        out.print(jsonObject);
        out.flush();
    }

    /**
     * Method find cause of exception and write into response.
     *
     * @param req  the {@link HttpServletRequest} object that
     *             contains the request the client made of
     *             the servlet
     * @param resp the {@link HttpServletResponse} object that
     *             contains the response the servlet returns
     *             to the client
     * @param e    object type of Exception.
     */
    private void toErrorPage(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        try {
            ErrorDto error = new ErrorDto();
            error.setError(e.getClass().getSimpleName());
            error.setMessage(e.getMessage());
            error.setStatus(500);
            resp.setStatus(500);
            PrintWriter out = resp.getWriter();
            out.print(gson.toJson(error));
            out.flush();
        } catch (IOException ex) {
            log.error("CONTROLLER IO EXCEPTION: " + ex.getMessage());
            throw new ControllerException(ex);
        }
    }

}
