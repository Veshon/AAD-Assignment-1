package com.example.assignment1.dao;

import com.example.assignment1.dto.CustomerDTO;
import com.example.assignment1.dto.ItemDTO;

import java.sql.Connection;
import java.sql.SQLException;

public class ItemDataProcess implements ItemData{

    static String SAVE_ITEM = "INSERT INTO ITEM (code,description,qty,price) VALUES (?,?,?,?)";
    static String GET_ITEM = "select * from item where code = ?";
    static String UPDATE_ITEM = "update item set description=?, qty=?, price=? where code = ?";
    static String DELETE_ITEM = "delete from item where code = ?";

    @Override
    public ItemDTO getItem(String itemCode, Connection connection) throws SQLException {
        var itemDTO = new ItemDTO();
        try {
            var ps = connection.prepareStatement(GET_ITEM);
            ps.setString(1, itemCode);
            var resultSet = ps.executeQuery();
            while (resultSet.next()) {
                itemDTO.setCode(resultSet.getString("code"));
                itemDTO.setDescription(resultSet.getString("description"));
                itemDTO.setQty(resultSet.getString("qty"));
                itemDTO.setPrice(resultSet.getString("price"));

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return itemDTO;
    }

    @Override
    public boolean saveItem(ItemDTO itemDTO, Connection connection) {
        try {
            var ps = connection.prepareStatement(SAVE_ITEM);
            ps.setString(1, itemDTO.getCode());
            ps.setString(2, itemDTO.getDescription());
            ps.setString(3, itemDTO.getQty());
            ps.setString(4, itemDTO.getPrice());
            return ps.executeUpdate() !=0;

        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean deleteItem(String itemId, Connection connection) {
        return false;
    }

    @Override
    public boolean updateItem(String itemId, ItemDTO updateItem, Connection connection) {
        return false;
    }
}
