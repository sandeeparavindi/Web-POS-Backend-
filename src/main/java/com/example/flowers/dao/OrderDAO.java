package com.example.flowers.dao;

import com.example.flowers.dto.ItemDTO;
import com.example.flowers.dto.OrderDTO;

import java.sql.SQLException;

public interface OrderDAO {
    void saveOrder(OrderDTO order) throws SQLException;
    void saveOrderItem(String orderId, ItemDTO item) throws SQLException;
    void updateItemQuantity(ItemDTO item) throws SQLException;
    boolean checkCustomerExists(String customerId) throws SQLException;
    int getItemQuantity(String itemCode) throws SQLException;
}
