package com.example.flowers.bo;

import com.example.flowers.bo.custom.ItemBO;
import com.example.flowers.dao.ItemDAOIMPL;
import com.example.flowers.dto.ItemDTO;

import java.sql.Connection;

public class ItemBOIMPL implements ItemBO {
    @Override
    public String saveItem(ItemDTO item, Connection connection) throws Exception {
        var itemDAOIMPL = new ItemDAOIMPL();
        return itemDAOIMPL.saveItem(item, connection);
    }

    @Override
    public boolean deleteItem(String code, Connection connection) throws Exception {
        var itemDAOIMPL = new ItemDAOIMPL();
        return itemDAOIMPL.deleteItem(code,connection);
    }

    @Override
    public boolean updateItem(String code, ItemDTO item, Connection connection) throws Exception {
        var itemDAOIMPL = new ItemDAOIMPL();
        return itemDAOIMPL.updateItem(code, item, connection);
    }
}
