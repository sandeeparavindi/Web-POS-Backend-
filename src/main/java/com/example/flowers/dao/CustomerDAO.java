package com.example.flowers.dao;

import com.example.flowers.dto.CustomerDTO;

import java.sql.Connection;

public sealed interface CustomerDAO permits CustomerDAOIMPL {
    String saveCustomer(CustomerDTO customer, Connection connection)throws Exception;
    boolean deleteCustomer(String id, Connection connection)throws Exception;
    boolean updateCustomer(String id,CustomerDTO customer,Connection connection)throws Exception;
    CustomerDTO getCustomer(String id, Connection connection)throws Exception;
}
