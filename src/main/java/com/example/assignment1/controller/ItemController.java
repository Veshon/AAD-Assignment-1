package com.example.assignment1.controller;


import com.example.assignment1.dao.ItemDataProcess;
import com.example.assignment1.dto.ItemDTO;
import com.example.assignment1.util.UtilProcess;
import jakarta.json.JsonException;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/item",loadOnStartup = 1)
public class ItemController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    Connection connection;

    @Override
    public void init() throws ServletException {

        try {
            var ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customerRegistration");
            this.connection =  pool.getConnection();
        }catch (NamingException | SQLException e){
            e.printStackTrace();
        }
        logger.info("Server Started");
        logger.info("DB Configured (For Item Table)");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getContentType().toLowerCase().startsWith("application/json") || req.getContentType() == null) {
            //send error
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        // Save Data
        try (var writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();
            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);
            itemDTO.setCode(UtilProcess.generateId());
            var saveData = new ItemDataProcess();
            if(saveData.saveItem(itemDTO, connection)){
                writer.write("Item Saved");
                resp.setStatus(HttpServletResponse.SC_CREATED);
                logger.info("Item Saved");
            }else {
                writer.write("Not Saved");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                logger.error("Item didn't Saved");
            }
        } catch (JsonException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var itemCode = req.getParameter("code");
        var dataProcess = new ItemDataProcess();
        try (var writer = resp.getWriter()) {
            var item = dataProcess.getItem(itemCode, connection);
            resp.setContentType("application/json");
            var jsonb = JsonbBuilder.create();
            jsonb.toJson(item, writer);
            logger.info("Item Read");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getContentType().toLowerCase().startsWith("application/json") || req.getContentType() == null) {
            //send error
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        try (var writer = resp.getWriter()) {
            var itemCode = req.getParameter("code");
            Jsonb jsonb = JsonbBuilder.create();
            var itemDataProcess = new ItemDataProcess();
            var updateItem = jsonb.fromJson(req.getReader(), ItemDTO.class);

            if (itemDataProcess.updateItem(itemCode, updateItem, connection)) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                writer.write("Item Updated");
                logger.info("Item Updated");
            } else {
                writer.write("Not Updated");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                logger.error("Item didn't Update");
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var itemCode = req.getParameter("code");
        var itemDataProcess = new ItemDataProcess();
        try (var writer = resp.getWriter()) {
            if (itemDataProcess.deleteItem(itemCode, connection)){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                writer.write("Item Deleted");
                logger.info("Item Deleted");
            }else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Not Deleted");
                logger.error("Item didn't Delete");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }
}
