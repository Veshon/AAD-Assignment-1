package com.example.assignment1.controller;

import com.example.assignment1.dao.impl.OrderDataProcess;
import com.example.assignment1.dto.OrderDTO;
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

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/order",loadOnStartup = 1)

public class OrderController extends HttpServlet {
    Connection connection;

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            var ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/customerRegistration");
            this.connection =  pool.getConnection();
        }catch (NamingException | SQLException e){
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
            OrderDTO orderDTO = jsonb.fromJson(req.getReader(), OrderDTO.class);
            orderDTO.setId(UtilProcess.generateId());
            var saveData = new OrderDataProcess();
            if(saveData.saveOrder(orderDTO, connection)){
                writer.write("Order Saved");
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var orderId = req.getParameter("id");
        var orderDataProcess = new OrderDataProcess();
        try (var writer = resp.getWriter()) {
            if (orderDataProcess.deleteOrder(orderId, connection)){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                writer.write("Order Deleted");
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
