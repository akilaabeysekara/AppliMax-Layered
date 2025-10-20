package com.applimax.project.model;

import com.applimax.project.dto.tm.SalesTM;
import com.applimax.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SalesModel {

    public ArrayList<SalesTM> getAllSales() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT o.order_id, i.name, i.quantity, od.quantity, od.unit_price, " +
                "(od.quantity * od.unit_price), o.order_date FROM order_table o " +
                "INNER JOIN order_detail od ON o.order_id = od.order_id " +
                "INNER JOIN item i ON od.item_id = i.item_id");

        ArrayList<SalesTM> list = new ArrayList<>();

        while (resultSet.next()) {
            SalesTM salesTM = new SalesTM(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getInt(4),
                    resultSet.getDouble(5),
                    resultSet.getDouble(6),
                    resultSet.getString(7)
            );
            list.add(salesTM);
        }
        return list;
    }

    public ArrayList<SalesTM> getSalesByItem(String itemId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT o.order_id, i.name, i.quantity, od.quantity, od.unit_price, " +
                "(od.quantity * od.unit_price), o.date FROM order_table o " +
                "INNER JOIN order_detail od ON o.order_id = od.order_id " +
                "INNER JOIN item i ON od.item_id = i.item_id " +
                "WHERE od.item_id = ?", itemId);

        ArrayList<SalesTM> list = new ArrayList<>();

        while (resultSet.next()) {
            SalesTM salesTM = new SalesTM(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getInt(4),
                    resultSet.getDouble(5),
                    resultSet.getDouble(6),
                    resultSet.getString(7)
            );
            list.add(salesTM);
        }
        return list;
    }

    public String getSalesCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM order_detail");
        if (resultSet.next()) {
            return String.valueOf(resultSet.getInt(1));
        }
        return "0";
    }
}