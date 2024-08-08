package com.example.flowers.bo;

import com.example.flowers.bo.custom.CustomerBO;
import com.example.flowers.dao.CustomerDAOIMPL;
import com.example.flowers.dto.CustomerDTO;

import java.sql.Connection;

public class CustomerBOIMPL implements CustomerBO {
    @Override
    public String saveCustomer(CustomerDTO customer, Connection connection) throws Exception {
        var studentDAOIMPL = new CustomerDAOIMPL();
        return studentDAOIMPL.saveCustomer(customer, connection);
    }


    @Override
    public boolean deleteCustomer(String id, Connection connection) throws Exception {
        var customerDAOImpl = new CustomerDAOIMPL();
        return customerDAOImpl.deleteCustomer(id,connection);
    }


    @Override
    public boolean updateCustomer(String id, CustomerDTO customer, Connection connection) throws Exception {
        var customerDAOImpl = new CustomerDAOIMPL();
        return customerDAOImpl.updateCustomer(id, customer, connection);
    }
}
