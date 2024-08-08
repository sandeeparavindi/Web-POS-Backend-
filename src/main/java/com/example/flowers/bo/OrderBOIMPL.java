package com.example.flowers.bo;

import com.example.flowers.bo.custom.OrderBO;
import com.example.flowers.dao.custom.OrderDAO;
import com.example.flowers.dto.ItemDTO;
import com.example.flowers.dto.OrderDTO;

import java.sql.SQLException;

public class OrderBOIMPL implements OrderBO {
    private final OrderDAO orderDAO;

    public OrderBOIMPL(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }
    @Override
    public void placeOrder(OrderDTO order) throws SQLException {
        if (!orderDAO.checkCustomerExists(order.getCustomerId())) {
            throw new SQLException("Customer ID not found");
        }

        for (ItemDTO item : order.getItems()) {
            int availableQty = orderDAO.getItemQuantity(item.getCode());
            if (availableQty < item.getQty()) {
                throw new SQLException("Insufficient stock for item " + item.getCode());
            }
        }

        orderDAO.saveOrder(order);

        for (ItemDTO item : order.getItems()) {
            orderDAO.saveOrderItem(order.getOrderId(), item);
            orderDAO.updateItemQuantity(item);
        }
    }
}
