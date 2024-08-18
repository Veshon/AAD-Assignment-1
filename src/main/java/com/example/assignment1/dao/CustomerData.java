package com.example.assignment1.dao;

import com.example.assignment1.dto.CustomerDTO;

import java.sql.Connection;
import java.sql.SQLException;

public interface CustomerData{
    CustomerDTO getCustomer(String studentId, Connection connection) throws SQLException;
    boolean saveCustomer(CustomerDTO studentDTO,Connection connection);
    boolean deleteCustomer(String studentId,Connection connection);
    boolean updateCustomer(String studentId,CustomerDTO student,Connection connection);
}
