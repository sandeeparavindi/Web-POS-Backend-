package com.example.flowers.bo.custom;

import com.example.flowers.dto.CustomerDTO;
import com.example.flowers.dto.ItemDTO;

import java.sql.Connection;

public interface ItemBO {
    String saveItem(ItemDTO item, Connection connection)throws Exception;
    boolean deleteItem(String code, Connection connection)throws Exception;
    boolean updateItem(String code,ItemDTO item,Connection connection)throws Exception;
}
