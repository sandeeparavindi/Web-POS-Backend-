package com.example.flowers.bo;

import com.example.flowers.dao.CustomerDAOIMPL;
import com.example.flowers.dto.CustomerDTO;

import java.sql.Connection;
import java.util.List;

public class CustomerBOIMPL implements CustomerBO{
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

    @Override
    public CustomerDTO getCustomer(String id, Connection connection) throws Exception {
        return null;
    }
}
