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
            Jsonb jsonb = JsonbBuilder.create(); // Creating jasonb object
            CustomerDTO customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class); // fromJson method eken request
            // eka read krgena bind karanawa StudentDTO class ekata.
            // Finally dto eka assign krgnnaw variable ekkt.
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
        System.out.println("Hey");
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
