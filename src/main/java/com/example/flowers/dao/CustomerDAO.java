package com.example.flowers.dao;

import com.example.flowers.dto.CustomerDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public sealed interface CustomerDAO permits CustomerDAOIMPL {
    String saveCustomer(CustomerDTO customer, Connection connection)throws Exception;
    boolean deleteCustomer(String id, Connection connection)throws Exception;
    boolean updateCustomer(String id,CustomerDTO customer,Connection connection)throws Exception;
    List<CustomerDTO> getAllCustomers(Connection connection) throws SQLException;

}
