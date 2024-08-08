package com.example.flowers.controller;

import com.example.flowers.dto.ItemDTO;
import com.example.flowers.dto.OrderDTO;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/orders", loadOnStartup = 4)
public class Order extends HttpServlet {
    static Logger logger = LoggerFactory.getLogger(Order.class);
    Connection connection;

    @Override
    public void init() throws ServletException {
        logger.info("Init method invoked");
        try {
            var ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/cusReg");
            this.connection = pool.getConnection();
            logger.debug("Connection initialized: {}", this.connection);

        } catch (SQLException | NamingException e) {
            logger.error("DB connection not initialized", e);
            throw new ServletException("DB connection not initialized", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getContentType() == null || !request.getContentType().toLowerCase().startsWith("application/json")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("Request content type is not application/json");
            return;
        }

        try (var reader = request.getReader()) {
            Jsonb jsonb = JsonbBuilder.create();
            OrderDTO order = jsonb.fromJson(reader, OrderDTO.class);

            logger.debug("Received order: {}", order);

            if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order data");
                logger.error("Order data is invalid: {}", order);
                return;
            }

            // Validate customer existence
            String customerIdCheckSQL = "SELECT 1 FROM customer WHERE id = ?";
            try (PreparedStatement checkCustomerStmt = connection.prepareStatement(customerIdCheckSQL)) {
                checkCustomerStmt.setString(1, order.getCustomerId());
                var rs = checkCustomerStmt.executeQuery();
                if (!rs.next()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer ID not found");
                    logger.error("Customer ID {} not found", order.getCustomerId());
                    return;
                }
            }

            PreparedStatement orderStmt = null;
            PreparedStatement orderItemStmt = null;
            PreparedStatement updateItemStmt = null;
            PreparedStatement itemQtyCheckStmt = null;

            try {
                connection.setAutoCommit(false);

                // Insert order
                String insertOrderSQL = "INSERT INTO orders (order_id, order_date, customer_id, total, discount, subtotal, cash, balance) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                orderStmt = connection.prepareStatement(insertOrderSQL);
                orderStmt.setString(1, order.getOrderId());
                orderStmt.setDate(2, java.sql.Date.valueOf(order.getOrderDate()));
                orderStmt.setString(3, order.getCustomerId());
                orderStmt.setDouble(4, order.getTotal());
                orderStmt.setDouble(5, order.getDiscount());
                orderStmt.setDouble(6, order.getSubTotal());
                orderStmt.setDouble(7, order.getCash());
                orderStmt.setDouble(8, order.getBalance());
                orderStmt.executeUpdate();

                // Insert order items and update item quantity
                String insertOrderItemSQL = "INSERT INTO order_items (order_id, item_code, quantity, price) VALUES (?, ?, ?, ?)";
                orderItemStmt = connection.prepareStatement(insertOrderItemSQL);
                String itemQtyCheckSQL = "SELECT qty FROM items WHERE code = ?";
                itemQtyCheckStmt = connection.prepareStatement(itemQtyCheckSQL);

                for (ItemDTO item : order.getItems()) {
                    // Validate item existence and stock quantity
                    itemQtyCheckStmt.setString(1, item.getCode());
                    var itemRs = itemQtyCheckStmt.executeQuery();
                    if (!itemRs.next()) {
                        throw new SQLException("Item code " + item.getCode() + " not found");
                    }
                    int availableQty = itemRs.getInt(1);
                    if (availableQty < item.getQty()) {
                        throw new SQLException("Insufficient stock for item " + item.getCode());
                    }

                    // Insert order item
                    orderItemStmt.setString(1, order.getOrderId());
                    orderItemStmt.setString(2, item.getCode());
                    orderItemStmt.setInt(3, item.getQty());
                    orderItemStmt.setDouble(4, item.getPrice());
                    orderItemStmt.executeUpdate();

                    // Update item quantity
                    String updateItemQtySQL = "UPDATE items SET qty = qty - ? WHERE code = ?";
                    try (PreparedStatement uupdateItemStmt = connection.prepareStatement(updateItemQtySQL)) {
                        uupdateItemStmt.setInt(1, item.getQty());
                        uupdateItemStmt.setString(2, item.getCode());
                        uupdateItemStmt.executeUpdate();
                    }
                }

                connection.commit();
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Order placed successfully");

            } catch (SQLException e) {
                if (connection != null) {
                    try {
                        connection.rollback();
                    } catch (SQLException ex) {
                        logger.error("Failed to rollback transaction", ex);
                    }
                }
                logger.error("SQL Exception occurred while placing order", e);
                e.printStackTrace(); // Print stack trace for debugging
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Failed to place order: " + e.getMessage());

            } finally {
                try {
                    if (orderStmt != null) orderStmt.close();
                    if (orderItemStmt != null) orderItemStmt.close();
                    if (updateItemStmt != null) updateItemStmt.close();
                } catch (SQLException e) {
                    logger.error("Failed to close statement", e);
                }
            }

        } catch (Exception e) {
            logger.error("Failed to parse request", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Failed to parse request: " + e.getMessage());
        }
    }

}
