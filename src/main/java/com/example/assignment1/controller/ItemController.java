package com.example.assignment1.controller;

import com.example.assignment1.dao.CustomerDataProcess;
import com.example.assignment1.dao.ItemDataProcess;
import com.example.assignment1.dto.CustomerDTO;
import com.example.assignment1.dto.ItemDTO;
import com.example.assignment1.util.UtilProcess;
import jakarta.json.JsonException;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/item",loadOnStartup = 1)
public class ItemController extends HttpServlet {
    Connection connection;

    static String SAVE_ITEM = "INSERT INTO ITEM (code,description,qty,price) VALUES (?,?,?,?)";
    static String GET_ITEM = "select * from item where code = ?";
    static String UPDATE_ITEM = "update item set description=?, qty=?, price=? where code = ?";
    static String DELETE_ITEM = "delete from item where code = ?";

    @Override
    public void init() throws ServletException {
        var driverClass = getServletContext().getInitParameter("driver-class");
        var dbUrl = getServletContext().getInitParameter("dbURL");
        var dbUserName = getServletContext().getInitParameter("dbUserName");
        var password = getServletContext().getInitParameter("dbPassword");
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            this.connection = DriverManager.getConnection(dbUrl, dbUserName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            }else {
                writer.write("Not Saved");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
