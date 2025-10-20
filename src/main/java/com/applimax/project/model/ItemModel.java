package com.applimax.project.model;

import com.applimax.project.dto.ItemDTO;
import com.applimax.project.dto.OrderDetailDTO;
import com.applimax.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.applimax.project.model.AppUserModel.getString;


public class ItemModel {
    public ArrayList<ItemDTO> getAllItem() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item");

        ArrayList<ItemDTO> list = new ArrayList<>();

        while (resultSet.next()) {
            ItemDTO itemDTO = new ItemDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            );

            list.add(itemDTO);
        }
        return list;
    }

    public String getNextId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT item_id FROM item ORDER BY item_id DESC LIMIT 1");
        char tableChar = 'I';
        return getString(resultSet, tableChar);
    }

    public boolean saveItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException {

        return CrudUtil.execute(
                "INSERT INTO item VALUES (?,?,?,?,?,?)",
                itemDTO.getItemId(),
                itemDTO.getItemName(),
                itemDTO.getItemCategory(),
                itemDTO.getQtyOnHand(),
                itemDTO.getItemBrand(),
                itemDTO.getItemPrice()

        );
    }

    public boolean updateItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE item SET name=?, category=?,quantity=?, brand=?, price=? WHERE item_id=?",
                itemDTO.getItemName(),
                itemDTO.getItemCategory(),
                itemDTO.getQtyOnHand(),
                itemDTO.getItemBrand(),
                itemDTO.getItemPrice(),
                itemDTO.getItemId()

        );
    }

    public boolean deleteItem(String itemId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "DELETE FROM item WHERE item_id = ?",
                itemId
        );
    }

    public boolean reduceqty(OrderDetailDTO orderDetailDTO) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE item SET quantity=quantity-? WHERE item_id=?";
        return CrudUtil.execute(sql, orderDetailDTO.getQuantity(), orderDetailDTO.getItemId());


    }

    public ArrayList<String> getAllItemIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT item_id FROM item"
        );
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }

    public String findNameById(String itemId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT name FROM item WHERE item_id=?",
                itemId
        );
        if (rst.next()) {
            return rst.getString(1);
        }
        return "";
    }

    public ItemDTO findById(String selectedId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM item WHERE item_id=?",
                selectedId
        );
        if (resultSet.next()) {
            return new ItemDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            );
        }
        return null;
    }

    public String getItemCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM item");
        if (resultSet.next()) {
            return String.valueOf(resultSet.getInt(1));
        }
        return "0";
    }
}
