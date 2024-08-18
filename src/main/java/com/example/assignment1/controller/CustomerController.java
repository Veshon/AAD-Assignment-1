package com.example.assignment1.controller;

import com.example.assignment1.dao.CustomerDataProcess;
import com.example.assignment1.dto.CustomerDTO;
import com.example.assignment1.util.UtilProcess;
import jakarta.json.JsonException;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/customer", loadOnStartup = 1)
public class CustomerController extends HttpServlet {

    Connection connection;

    static String SAVE_CUSTOMER = "INSERT INTO CUSTOMER (id,name,address,mobile) VALUES (?,?,?,?)";
    static String GET_CUSTOMER = "select * from customer where id = ?";
    static String UPDATE_CUSTOMER = "update customer set name=?, address=?, mobile=? where id = ?";
    static String DELETE_CUSTOMER = "delete from customer where id = ?";

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
            CustomerDTO customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);
            customerDTO.setId(UtilProcess.generateId());
            var saveData = new CustomerDataProcess();
            if(saveData.saveCustomer(customerDTO, connection)){
                writer.write("Customer Saved");
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
        var cusId = req.getParameter("id");
        var dataProcess = new CustomerDataProcess();
        try (var writer = resp.getWriter()) {
            var student = dataProcess.getCustomer(cusId, connection);
            resp.setContentType("application/json");
            var jsonb = JsonbBuilder.create();
            jsonb.toJson(student, writer); // toJson method ekata pass krnw student propperties tika. Itpsse write krnw.
            //Finally clienta send krnw.
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
            var ps = this.connection.prepareStatement(UPDATE_CUSTOMER);
            var cusId = req.getParameter("c_id");
            Jsonb jsonb = JsonbBuilder.create();
            var customerDataProcess = new CustomerDataProcess();
            var updateCustomer = jsonb.fromJson(req.getReader(), CustomerDTO.class);

            if (customerDataProcess.updateCustomer(cusId, updateCustomer, connection)) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                writer.write("Customer Updated");

            } else {
                writer.write("Not Updated");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var cusId = req.getParameter("c_id");
        var customerDataProcess = new CustomerDataProcess();
        try (var writer = resp.getWriter()) {
            if (customerDataProcess.deleteCustomer(cusId, connection)){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                writer.write("Customer Deleted");
            }else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Not Deleted");
            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }
}
