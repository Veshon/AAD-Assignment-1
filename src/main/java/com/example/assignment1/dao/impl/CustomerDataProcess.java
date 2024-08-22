package com.example.assignment1.dao.impl;

import com.example.assignment1.dao.CustomerData;
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
        var customerDTO = new CustomerDTO();
        try {
            var ps = connection.prepareStatement(GET_CUSTOMER);
            ps.setString(1, customerId);
            var resultSet = ps.executeQuery();
            while (resultSet.next()) {
                customerDTO.setId(resultSet.getString("id"));
                customerDTO.setName(resultSet.getString("name"));
                customerDTO.setAddress(resultSet.getString("address"));
                customerDTO.setMobile(resultSet.getString("mobile"));

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return customerDTO;
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
        try {
            var ps = connection.prepareStatement(DELETE_CUSTOMER);
            ps.setString(1, customerId);
            return ps.executeUpdate()!=0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateCustomer(String customerId, CustomerDTO updateCustomer, Connection connection) {
        try {
            var ps = connection.prepareStatement(UPDATE_CUSTOMER);
            ps.setString(1, updateCustomer.getName());
            ps.setString(2, updateCustomer.getAddress());
            ps.setString(3, updateCustomer.getMobile());
            ps.setString(4, customerId);
            return ps.executeUpdate()!=0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
