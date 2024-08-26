package com.example.assignment1.controller;

import com.example.assignment1.dao.impl.CustomerDataProcess;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/customer", loadOnStartup = 1)
public class CustomerController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
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
        logger.info("DB Configured");

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
        logger.info("Customer Saved");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var cusId = req.getParameter("id");
        var dataProcess = new CustomerDataProcess();
        try (var writer = resp.getWriter()) {
            var customer = dataProcess.getCustomer(cusId, connection);
            resp.setContentType("application/json");
            var jsonb = JsonbBuilder.create();
            jsonb.toJson(customer, writer);
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
