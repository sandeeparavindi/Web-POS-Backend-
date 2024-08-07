package com.example.flowers.dao;

import com.example.flowers.dto.CustomerDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class CustomerDAOIMPL implements CustomerDAO {
    public static String SAVE_CUSTOMER = "INSERT INTO customer (id,name,address,mobile) VALUES(?,?,?,?)";


    @Override
    public String saveCustomer(CustomerDTO customer, Connection connection) throws Exception {
        try {
            var ps = connection.prepareStatement(SAVE_CUSTOMER);
            ps.setString(1, customer.getId());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getMobile());
            if(ps.executeUpdate() != 0){
                return "Customer Save Successfully";
            }else {
                return "Failed to Save Customer";
            }
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public boolean deleteCustomer(String id, Connection connection) throws Exception {
        return false;
    }

    @Override
    public boolean updateCustomer(String id, CustomerDTO customer, Connection connection) throws SQLException {
        String query = "UPDATE customer SET name = ?, address = ?, mobile = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getMobile());
            ps.setString(4, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public CustomerDTO getCustomer(String id, Connection connection) throws Exception {
        return null;
    }

    @Override
    public List<CustomerDTO> getAllCustomers(Connection connection) throws SQLException {
        List<CustomerDTO> customers = new ArrayList<>();
        String query = "SELECT * FROM customer";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CustomerDTO customer = new CustomerDTO();
                customer.setId(rs.getString("id"));
                customer.setName(rs.getString("name"));
                customer.setAddress(rs.getString("address"));
                customer.setMobile(rs.getString("mobile"));
                customers.add(customer);
            }
        }
        return customers;
    }

}
