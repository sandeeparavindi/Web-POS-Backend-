package com.example.flowers.dao;

import com.example.flowers.dto.ItemDTO;
import com.example.flowers.dto.OrderDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderDAOIMPL implements OrderDAO{
    private final DataSource dataSource;

    public OrderDAOIMPL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void saveOrder(OrderDTO order) throws SQLException {
        String insertOrderSQL = "INSERT INTO orders (order_id, order_date, customer_id, total, discount, subtotal, cash, balance) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement orderStmt = connection.prepareStatement(insertOrderSQL)) {
            orderStmt.setString(1, order.getOrderId());
            orderStmt.setDate(2, java.sql.Date.valueOf(order.getOrderDate()));
            orderStmt.setString(3, order.getCustomerId());
            orderStmt.setDouble(4, order.getTotal());
            orderStmt.setDouble(5, order.getDiscount());
            orderStmt.setDouble(6, order.getSubTotal());
            orderStmt.setDouble(7, order.getCash());
            orderStmt.setDouble(8, order.getBalance());
            orderStmt.executeUpdate();
        }
    }

    @Override
    public void saveOrderItem(String orderId, ItemDTO item) throws SQLException {
        String insertOrderItemSQL = "INSERT INTO order_items (order_id, item_code, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement orderItemStmt = connection.prepareStatement(insertOrderItemSQL)) {
            orderItemStmt.setString(1, orderId);
            orderItemStmt.setString(2, item.getCode());
            orderItemStmt.setInt(3, item.getQty());
            orderItemStmt.setDouble(4, item.getPrice());
            orderItemStmt.executeUpdate();
        }
    }

    @Override
    public void updateItemQuantity(ItemDTO item) throws SQLException {
        String updateItemQtySQL = "UPDATE items SET qty = qty - ? WHERE code = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement updateItemStmt = connection.prepareStatement(updateItemQtySQL)) {
            updateItemStmt.setInt(1, item.getQty());
            updateItemStmt.setString(2, item.getCode());
            updateItemStmt.executeUpdate();
        }
    }

    @Override
    public boolean checkCustomerExists(String customerId) throws SQLException {
        String customerCheckSQL = "SELECT 1 FROM customer WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement checkCustomerStmt = connection.prepareStatement(customerCheckSQL)) {
            checkCustomerStmt.setString(1, customerId);
            var rs = checkCustomerStmt.executeQuery();
            return rs.next();
        }
    }

    @Override
    public int getItemQuantity(String itemCode) throws SQLException {
        String itemQtyCheckSQL = "SELECT qty FROM items WHERE code = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement itemQtyCheckStmt = connection.prepareStatement(itemQtyCheckSQL)) {
            itemQtyCheckStmt.setString(1, itemCode);
            var rs = itemQtyCheckStmt.executeQuery();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }
}
