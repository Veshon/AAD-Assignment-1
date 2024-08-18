package com.example.assignment1.dao;

import com.example.assignment1.dto.CustomerDTO;
import com.example.assignment1.dto.ItemDTO;

import java.sql.Connection;
import java.sql.SQLException;

public interface ItemData {
    ItemDTO getItem(String itemCode, Connection connection) throws SQLException;
    boolean saveItem(ItemDTO itemDTO, Connection connection);
    boolean deleteItem(String itemCode,Connection connection);
    boolean updateItem(String itemCode,ItemDTO updateItem,Connection connection);
}
