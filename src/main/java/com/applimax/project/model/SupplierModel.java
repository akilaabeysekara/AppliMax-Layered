package com.applimax.project.model;

import com.applimax.project.dto.SupplierDTO;
import com.applimax.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierModel {
    public ArrayList<SupplierDTO> getAllSuppliers() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM supplier");
        ArrayList<SupplierDTO> list = new ArrayList<>();

        while (resultSet.next()) {
            SupplierDTO supplierDTO = new SupplierDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            );
            list.add(supplierDTO);
        }
        return list;
    }


    public String getNextId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT supplier_id FROM supplier ORDER BY supplier_id DESC LIMIT 1");
        String prefix = "SU";

        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String numberPart = lastId.substring(2);
            try {
                int lastNumber = Integer.parseInt(numberPart);
                int nextNumber = lastNumber + 1;
                return String.format("%s%03d", prefix, nextNumber);
            } catch (NumberFormatException e) {
                return prefix + "001";
            }
        }
        return prefix + "001";
    }

    public boolean saveSupplier(SupplierDTO supplierDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO supplier VALUES (?,?,?,?,?,?)",
                supplierDTO.getSupplierId(),
                supplierDTO.getName(),
                supplierDTO.getNic(),
                supplierDTO.getPhoneNo(),
                supplierDTO.getEmail(),
                supplierDTO.getAddress()
        );
    }

    public boolean updateSupplier(SupplierDTO supplierDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE supplier SET name=?, nic=?, phone_no=?, email=?, address=? WHERE supplier_id=?",
                supplierDTO.getName(),
                supplierDTO.getNic(),
                supplierDTO.getPhoneNo(),
                supplierDTO.getEmail(),
                supplierDTO.getAddress(),
                supplierDTO.getSupplierId()
        );
    }

    public boolean deleteSupplier(String supplierId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "DELETE FROM supplier WHERE supplier_id = ?",
                supplierId
        );
    }

    public ArrayList<String> getAllSupplierIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT supplier_id FROM supplier"
        );
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }

    public String findNameById(String supplierId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT name FROM supplier WHERE supplier_id=?",
                supplierId
        );
        if (rst.next()) {
            return rst.getString(1);
        }
        return "";
    }
}