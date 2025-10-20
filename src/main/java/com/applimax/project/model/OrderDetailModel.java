package com.applimax.project.model;

import com.applimax.project.dto.OrderDetailDTO;
import com.applimax.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class OrderDetailModel {
    private final ItemModel itemModel = new ItemModel();

    public ArrayList<OrderDetailDTO> getAllOrerDetail() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM order_detail WHERE order_id = ?;");

        ArrayList<OrderDetailDTO> list = new ArrayList<>();

        while (resultSet.next()) {
            OrderDetailDTO orderDetailDTO = new OrderDetailDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4)
            );

            list.add(orderDetailDTO);
        }
        return list;
    }

    public boolean saveOrderDetailList(ArrayList<OrderDetailDTO> cartList) throws SQLException, ClassNotFoundException {
        for (OrderDetailDTO orderDetailDTO:cartList){

            boolean isDetailSaved = saveOrderDetailList(orderDetailDTO);
            if(!isDetailSaved){
                return false;
            }
            boolean isUpdate = itemModel.reduceqty(orderDetailDTO);
            if(!isUpdate){
                return false;
            }

        }
        return true;
    }

    private boolean saveOrderDetailList(OrderDetailDTO orderDetailDTO) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO order_detail (order_id, item_id, quantity, unit_price) VALUES (?,?,?,?)";
        return CrudUtil.execute(sql, orderDetailDTO.getOrderId(), orderDetailDTO.getItemId(),
                orderDetailDTO.getQuantity(), orderDetailDTO.getUnitPrice());
    }
}
