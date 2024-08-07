package com.example.flowers.controller;

import com.example.flowers.bo.CustomerBOIMPL;
import com.example.flowers.dao.CustomerDAOIMPL;
import com.example.flowers.dto.CustomerDTO;
import com.example.flowers.util.Util;
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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = "/customer", loadOnStartup = 4)
public class Customer extends HttpServlet {
    static Logger logger = LoggerFactory.getLogger(Customer.class);
    Connection connection;

    @Override
    public void init() throws ServletException {
        logger.info("Init method invoked");
        try {
            var ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/cusReg");
            this.connection = pool.getConnection();
            logger.debug("Connection initialized",this.connection);

        }catch (SQLException | NamingException e){
            logger.error("DB connection not init" );
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("Request not matched with the criteria");
        }
        try (var writer = resp.getWriter()){
            Jsonb jsonb = JsonbBuilder.create();
            var customerBOIMPL = new CustomerBOIMPL();
            CustomerDTO customer = jsonb.fromJson(req.getReader(), CustomerDTO.class);
            logger.info("Invoke idGenerate()");
            customer.setId(Util.idGenerate());
            //Save data in the DB
            writer.write(customerBOIMPL.saveCustomer(customer,connection));
            logger.info("Customer saved successfully");
            resp.setStatus(HttpServletResponse.SC_CREATED);
        }catch (Exception e){
            logger.error("Connection failed");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();
            var customerDAOImpl = new CustomerDAOIMPL();
            List<CustomerDTO> customers = customerDAOImpl.getAllCustomers(connection);
            String json = jsonb.toJson(customers);
            writer.write(json);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        if(req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
//            logger.error("Request not matched with the criteria");
//            return;
//        }
//        try (var writer = resp.getWriter()) {
//            Jsonb jsonb = JsonbBuilder.create();
//            var customerBOIMPL = new CustomerBOIMPL();
//            CustomerDTO customer = jsonb.fromJson(req.getReader(), CustomerDTO.class);
//            String id = req.getParameter("id");
//            if(customerBOIMPL.updateCustomer(id, customer, connection)) {
//                writer.write("Customer updated successfully");
//                resp.setStatus(HttpServletResponse.SC_OK);
//            } else {
//                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            }
//        } catch (Exception e) {
//            logger.error("Update failed");
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            e.printStackTrace();
//        }
//    }

//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        try (var writer = resp.getWriter()) {
//            var studentBOIMPL = new CustomerBOIMPL();
//
//            // Extract customer ID from the path
//            String pathInfo = req.getPathInfo();
//            String customerId = pathInfo != null ? pathInfo.substring(1) : null; // Remove leading '/'
//
//            if (customerId == null) {
//                writer.write("Customer ID is missing");
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                return;
//            }
//
//            Jsonb jsonb = JsonbBuilder.create();
//            CustomerDTO customer = jsonb.fromJson(req.getReader(), CustomerDTO.class);
//
//            if (studentBOIMPL.updateCustomer(customerId, customer, connection)) {
//                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
//            } else {
//                writer.write("Update failed");
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
@Override
protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        logger.error("Request not matched with the criteria");
        return;
    }

    try (var writer = resp.getWriter()) {
        Jsonb jsonb = JsonbBuilder.create();
        var customerBOIMPL = new CustomerBOIMPL();
        CustomerDTO customer = jsonb.fromJson(req.getReader(), CustomerDTO.class);
        String customerId = req.getParameter("id");

        boolean result = customerBOIMPL.updateCustomer(customerId, customer, connection);
        if (result) {
            writer.write("Customer updated successfully");
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found");
        }
    } catch (Exception e) {
        logger.error("Update failed", e);
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
