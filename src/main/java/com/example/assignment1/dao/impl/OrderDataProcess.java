package com.example.assignment1.dao.impl;

import com.example.assignment1.dao.OrderData;
import com.example.assignment1.dto.OrderDTO;

import java.sql.Connection;
import java.sql.SQLException;

public class OrderDataProcess implements OrderData {
    static String SAVE_ORDER = "INSERT INTO orders (id,itemId,cusId,qtyOnHand,cusName,unitPrice,description,qty,total) VALUES (?,?,?,?,?,?,?,?,?)";
//    static String GET_ORDER = "select * from orders where id = ?";
//    static String UPDATE_ORDER = "update orders set description=?, qty=?, unitPrice=? where id = ?";
    static String DELETE_ORDER = "delete from orders where id = ?";


    @Override
    public boolean saveOrder(OrderDTO orderDTO, Connection connection) {
        try {
            var ps = connection.prepareStatement(SAVE_ORDER);
            ps.setString(1, orderDTO.getId());
            ps.setString(2, orderDTO.getCusId());
            ps.setString(3, orderDTO.getItemId());
            ps.setString(4, orderDTO.getCusName());
            ps.setString(5, orderDTO.getQtyOnHand());
            ps.setString(6, orderDTO.getDescription());
            ps.setString(7, orderDTO.getQty());
            ps.setString(8, orderDTO.getUnitPrice());
            ps.setString(9, orderDTO.getTotal());
            return ps.executeUpdate() !=0;

        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean deleteOrder(String orderId, Connection connection) {
        try {
            var ps = connection.prepareStatement(DELETE_ORDER);
            ps.setString(1, orderId);
            return ps.executeUpdate()!=0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
