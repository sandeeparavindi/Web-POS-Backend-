package com.example.flowers.bo.custom;

import com.example.flowers.dto.CustomerDTO;

import java.sql.Connection;
import java.util.List;

public interface CustomerBO {
    String saveCustomer(CustomerDTO customer, Connection connection)throws Exception;
    boolean deleteCustomer(String id, Connection connection)throws Exception;
    boolean updateCustomer(String id,CustomerDTO customer,Connection connection)throws Exception;
}
