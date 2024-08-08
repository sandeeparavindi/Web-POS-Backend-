package com.example.flowers.controller;

import com.example.flowers.bo.OrderBO;
import com.example.flowers.bo.OrderBOIMPL;
import com.example.flowers.dao.OrderDAO;
import com.example.flowers.dao.OrderDAOIMPL;
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
    private OrderBO orderBO;

    @Override
    public void init() throws ServletException {
        try {
            var ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/cusReg");
            OrderDAO orderDAO = new OrderDAOIMPL(pool);
            this.orderBO = new OrderBOIMPL(orderDAO);
        } catch (NamingException e) {
            logger.error("DB connection not initialized", e);
            throw new ServletException("DB connection not initialized", e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getContentType() == null || !request.getContentType().toLowerCase().startsWith("application/json")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try (var reader = request.getReader()) {
            Jsonb jsonb = JsonbBuilder.create();
            OrderDTO order = jsonb.fromJson(reader, OrderDTO.class);

            if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order data");
                return;
            }

            orderBO.placeOrder(order);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Order placed successfully");

        } catch (SQLException e) {
            logger.error("Error while placing order", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to place order: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to parse request", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to parse request: " + e.getMessage());
        }
    }

}
