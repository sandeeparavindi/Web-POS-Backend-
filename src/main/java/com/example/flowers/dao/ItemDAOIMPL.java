package com.example.flowers.dao;

import com.example.flowers.dto.ItemDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public final class ItemDAOIMPL implements ItemDAO {
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
        return false;
    }

    @Override
    public boolean updateItem(String code, ItemDTO item, Connection connection) throws Exception {
        return false;
    }

    @Override
    public List<ItemDTO> getAllItems(Connection connection) throws SQLException {
        return null;
    }
}
