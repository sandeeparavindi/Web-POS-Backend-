package com.example.flowers.bo.custom;

import com.example.flowers.dto.OrderDTO;

import java.sql.SQLException;
import java.util.List;

public interface OrderBO {
    void placeOrder(OrderDTO order) throws SQLException;
    List<OrderDTO> getAllOrders() throws SQLException;
}
