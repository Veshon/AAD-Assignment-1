package com.example.assignment1.dao;

public class OrderDataProcess {
    static String SAVE_ORDER = "INSERT INTO orders (id,itemId,cusId,qtyOnHand,cusName,unitPrice,description,qty,total) VALUES (?,?,?,?,?,?,?,?,?)";
    static String GET_ORDER = "select * from orders where id = ?";
    static String UPDATE_ORDER = "update orders set description=?, qty=?, unitPrice=? where id = ?";
    static String DELETE_ORDER = "delete from orders where id = ?";
}
