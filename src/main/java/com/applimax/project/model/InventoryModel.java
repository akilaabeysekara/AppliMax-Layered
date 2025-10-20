package com.applimax.project.model;

import com.applimax.project.dto.InventoryDTO;
import com.applimax.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class InventoryModel {
    public ArrayList<InventoryDTO> getAllInventory() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
                "SELECT i.inventory_id, itm.name, s.name, itm.price, itm.quantity, i.received_date " +
                        "FROM inventory i " +
                        "JOIN item itm ON i.item_id = itm.item_id " +
                        "JOIN supplier s ON i.supplier_id = s.supplier_id"
        );

        ArrayList<InventoryDTO> list = new ArrayList<>();

        while (resultSet.next()) {
            InventoryDTO inventoryDTO = new InventoryDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getInt(5),
                    resultSet.getObject(6, LocalDateTime.class)
            );
            list.add(inventoryDTO);
        }
        return list;
    }


    public String getNextId() throws SQLException, ClassNotFoundException {
        String prefix = "IN";

        while (true) {
            ResultSet resultSet = CrudUtil.execute(
                    "SELECT MAX(CAST(SUBSTRING(inventory_id, 3) AS UNSIGNED)) FROM inventory"
            );

            if (resultSet.next()) {
                Integer lastNumber = resultSet.getObject(1, Integer.class);
                int nextNumber = (lastNumber == null) ? 1 : lastNumber + 1;
                String newId = String.format("%s%03d", prefix, nextNumber);

                // Verify the ID doesn't exist (double-check)
                ResultSet checkExists = CrudUtil.execute(
                        "SELECT 1 FROM inventory WHERE inventory_id = ?",
                        newId
                );

                if (!checkExists.next()) {
                    return newId;
                }
            } else {
                return prefix + "001";
            }
        }
    }


    public boolean saveInventory(InventoryDTO dto) throws SQLException, ClassNotFoundException {
        ResultSet checkExists = CrudUtil.execute(
                "SELECT 1 FROM inventory WHERE inventory_id = ?",
                dto.getInventoryId()
        );

        if (checkExists.next()) {
            dto.setInventoryId(getNextId());
        }

        return CrudUtil.execute(
                "INSERT INTO inventory (inventory_id, item_id, supplier_id, received_date) VALUES (?, ?, ?, ?)",
                dto.getInventoryId(),
                getItemIdByName(dto.getItemName()),
                getSupplierIdByName(dto.getSupplierName()),
                dto.getReceivedDate()
        );
    }
    public boolean updateInventory(InventoryDTO dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE inventory SET item_id = ?, supplier_id = ?, received_date = ? WHERE inventory_id = ?",
                getItemIdByName(dto.getItemName()),
                getSupplierIdByName(dto.getSupplierName()),
                dto.getReceivedDate(),
                dto.getInventoryId()
        );
    }
    private String getItemIdByName(String itemName) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT item_id FROM item WHERE name = ?", itemName);
        if (rs.next()) return rs.getString(1);
        throw new SQLException("Item name not found: " + itemName);
    }

    private String getSupplierIdByName(String supplierName) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT supplier_id FROM supplier WHERE name = ?", supplierName);
        if (rs.next()) return rs.getString(1);
        throw new SQLException("Supplier name not found: " + supplierName);
    }

    public boolean deleteInventory(String inventoryId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "DELETE FROM inventory WHERE inventory_id = ?",
                inventoryId
        );
    }

    public ArrayList<String> getAllInventoryIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT inventory_id FROM inventory"
        );
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }

    public String findNameById(String inventoryId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT name FROM inventory WHERE inventory_id=?",
                inventoryId
        );
        if (rst.next()) {
            return rst.getString(1);
        }
        return "";
    }
}