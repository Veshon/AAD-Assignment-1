package com.example.assignment1.dao;

<<<<<<< HEAD
import com.example.assignment1.dto.OrderDTO;

import java.sql.Connection;
=======
import com.example.assignment1.dto.ItemDTO;
import com.example.assignment1.dto.OrderDTO;

import java.sql.Connection;
import java.sql.SQLException;
>>>>>>> origin/master

public interface OrderData {
//    OrderDTO getItem(String orderId, Connection connection) throws SQLException;
    boolean saveOrder(OrderDTO orderDTO, Connection connection);
    boolean deleteOrder(String orderId,Connection connection);
//    boolean updateOrder(String orderId,OrderDTO updateOrder,Connection connection);
}
