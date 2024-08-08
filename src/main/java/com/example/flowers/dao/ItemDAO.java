package com.example.flowers.dao;

import com.example.flowers.dto.ItemDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public sealed interface ItemDAO permits ItemDAOIMPL{
    String saveItem(ItemDTO item, Connection connection)throws Exception;
    boolean deleteItem(String code, Connection connection)throws Exception;
    boolean updateItem(String code,ItemDTO item,Connection connection)throws Exception;
    List<ItemDTO> getAllItems(Connection connection) throws SQLException;
}
