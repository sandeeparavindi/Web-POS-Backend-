package com.example.flowers.dao;

import com.example.flowers.dao.custom.ItemDAO;
import com.example.flowers.dto.ItemDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public  class ItemDAOIMPL implements ItemDAO {
    public static String SAVE_ITEM = "INSERT INTO items (code,description,price,qty) VALUES(?,?,?,?)";

    @Override
    public String saveItem(ItemDTO item, Connection connection) throws Exception {
        try {
            var ps = connection.prepareStatement(SAVE_ITEM);
            ps.setString(1, item.getCode());
            ps.setString(2, item.getDescription());
            ps.setString(3, String.valueOf(item.getPrice()));
            ps.setString(4, String.valueOf(item.getQty()));
            if(ps.executeUpdate() != 0){
                return "Item Save Successfully";
            }else {
                return "Failed to Save Item";
            }
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public boolean deleteItem(String code, Connection connection) throws Exception {
        String query = "DELETE FROM items WHERE code = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, code);
            return ps.executeUpdate()>0;
        }
    }

    @Override
    public boolean updateItem(String code, ItemDTO item, Connection connection) throws Exception {
        String query = "UPDATE items SET description = ?, price = ?, qty = ? WHERE code = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, item.getDescription());
            ps.setString(2, String.valueOf(item.getPrice()));
            ps.setString(3, String.valueOf(item.getQty()));
            ps.setString(4, code);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<ItemDTO> getAllItems(Connection connection) throws SQLException {
        List<ItemDTO> items = new ArrayList<>();
        String query = "SELECT * FROM items";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ItemDTO item = new ItemDTO();
                item.setCode(rs.getString("code"));
                item.setDescription(rs.getString("description"));
                item.setPrice(Double.parseDouble(rs.getString("price")));
                item.setQty(Integer.parseInt(rs.getString("qty")));
                items.add(item);
            }
        }
        return items;
    }
}
