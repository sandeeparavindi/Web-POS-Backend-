package com.example.flowers.bo;

import com.example.flowers.dto.OrderDTO;

import java.sql.SQLException;

public interface OrderBO {
    void placeOrder(OrderDTO order) throws SQLException;
}
