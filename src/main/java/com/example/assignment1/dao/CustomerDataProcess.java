package com.example.assignment1.dao;

import com.example.assignment1.dto.CustomerDTO;

import java.sql.Connection;
import java.sql.SQLException;

public class CustomerDataProcess implements CustomerData {

    static String SAVE_CUSTOMER = "INSERT INTO CUSTOMER (id,name,address,mobile) VALUES (?,?,?,?)";
    static String GET_CUSTOMER = "select * from customer where id = ?";
    static String UPDATE_CUSTOMER = "update customer set name=?, address=?, mobile=? where id = ?";
    static String DELETE_CUSTOMER = "delete from customer where id = ?";

    @Override
    public CustomerDTO getCustomer(String customerId, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public boolean saveCustomer(CustomerDTO customerDTO, Connection connection) {
        try {
            var ps = connection.prepareStatement(SAVE_CUSTOMER);
            ps.setString(1, customerDTO.getId());
            ps.setString(2, customerDTO.getName());
            ps.setString(3, customerDTO.getAddress());
            ps.setString(4, customerDTO.getMobile());
            return ps.executeUpdate() !=0;

        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean deleteCustomer(String customerId, Connection connection) {
        return false;
    }

    @Override
    public boolean updateCustomer(String customerId, CustomerDTO customer, Connection connection) {
        return false;
    }
}
