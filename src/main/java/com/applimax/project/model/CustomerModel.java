package com.applimax.project.model;

import com.applimax.project.dto.CustomerDTO;
import com.applimax.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class CustomerModel {
    public ArrayList<CustomerDTO> getAllCustomer() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer");

        ArrayList<CustomerDTO> list = new ArrayList<>();

        while (resultSet.next()) {
            CustomerDTO customerDTO = new CustomerDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            );

            list.add(customerDTO);
        }
        return list;
    }

    public String getNextId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT customer_id FROM customer ORDER BY customer_id DESC LIMIT 1");
        char tableChar = 'C';
        return getString(resultSet, tableChar);
    }

    public boolean saveCustomer(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {

        return CrudUtil.execute(
                "INSERT INTO customer VALUES (?,?,?,?,?,?)",
                customerDTO.getCustomerId(),
                customerDTO.getCustomerName(),
                customerDTO.getCustomerNic(),
                customerDTO.getCustomerEmail(),
                customerDTO.getCustomerPhone(),
                customerDTO.getCustomerAddress()
        );
    }

    public boolean updateCustomer(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE customer SET name=?, nic=?, email=?, phone_no=?, address=? WHERE customer_id=?",
                customerDTO.getCustomerName(),
                customerDTO.getCustomerNic(),
                customerDTO.getCustomerEmail(),
                customerDTO.getCustomerPhone(),
                customerDTO.getCustomerAddress(),
                customerDTO.getCustomerId()
        );
    }

    public boolean deleteCustomer(String customerId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "DELETE FROM customer WHERE customer_id = ?",
                customerId
        );
    }

    public ArrayList<String> getAllCustomerIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT customer_id FROM customer"
        );
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }

    public String findNameById(String customerId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT name FROM customer WHERE customer_id=?",
                customerId
        );
        if (rst.next()) {
            return rst.getString(1);
        }
        return "";
    }

    public String getCustomerCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM customer");
        if (resultSet.next()) {
            return String.valueOf(resultSet.getInt(1));
        }
        return "0";
    }
}
