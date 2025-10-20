package com.applimax.project.model;

import com.applimax.project.dto.tm.BestSellingTM;
import com.applimax.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BestSellingModel {
    public ArrayList<BestSellingTM> getBestSellingItems() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT i.name, i.category, i.brand, i.price, i.quantity " +
                "FROM item i " +
                "INNER JOIN order_detail od ON i.item_id = od.item_id " +
                "GROUP BY i.item_id " +
                "ORDER BY SUM(od.quantity) DESC " +
                "LIMIT 5");

        ArrayList<BestSellingTM> list = new ArrayList<>();

        while (resultSet.next()) {
            BestSellingTM bestSellingTM = new BestSellingTM(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
            list.add(bestSellingTM);
        }
        return list;
    }
}
