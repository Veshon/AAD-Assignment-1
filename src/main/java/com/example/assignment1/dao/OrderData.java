package com.example.assignment1.dao;

import com.example.assignment1.dto.ItemDTO;
import com.example.assignment1.dto.OrderDTO;

import java.sql.Connection;
import java.sql.SQLException;

public interface OrderData {
    ItemDTO getItem(String orderId, Connection connection) throws SQLException;
    boolean saveItem(OrderDTO orderDTO, Connection connection);
    boolean deleteItem(String orderId,Connection connection);
    boolean updateItem(String orderId,OrderDTO updateOrder,Connection connection);
}
