package com.applimax.project.model;

import com.applimax.project.dto.SupplierPaymentDTO;
import com.applimax.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;

public class SupplierPaymentModel {

    public ArrayList<SupplierPaymentDTO> getAllSupplierPayments() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM supplier_expenses");
        ArrayList<SupplierPaymentDTO> list = new ArrayList<>();

        while (resultSet.next()) {
            SupplierPaymentDTO dto = new SupplierPaymentDTO(
                    resultSet.getString("expenses_id"),
                    resultSet.getString("supplier_id"),
                    resultSet.getDouble("pay_amount"),
                    resultSet.getString("payment_method"),
                    resultSet.getDate("payment_date").toLocalDate()
            );
            list.add(dto);
        }
        return list;
    }

    public boolean saveSupplierPayment(SupplierPaymentDTO dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO supplier_expenses (expenses_id, supplier_id, pay_amount, payment_method, payment_date) VALUES (?,?,?,?,?)",
                dto.getExpensesId(),
                dto.getSupplierId(),
                dto.getPayAmount(),
                dto.getPaymentMethod(),
                Date.valueOf(dto.getPaymentDate())
        );
    }

    public boolean updateSupplierPayment(SupplierPaymentDTO dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE supplier_expenses SET supplier_id=?, pay_amount=?, payment_method=?, payment_date=? WHERE expenses_id=?",
                dto.getSupplierId(),
                dto.getPayAmount(),
                dto.getPaymentMethod(),
                Date.valueOf(dto.getPaymentDate()),
                dto.getExpensesId()
        );
    }

    public boolean deleteSupplierPayment(String expensesId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "DELETE FROM supplier_expenses WHERE expenses_id=?",
                expensesId
        );
    }

    public ArrayList<String> getAllExpensesIds() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT expenses_id FROM supplier_expenses");
        ArrayList<String> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(resultSet.getString(1));
        }
        return list;
    }

    public String getNextExpensesId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT expenses_id FROM supplier_expenses ORDER BY expenses_id DESC LIMIT 1");
        String prefix = "EX";

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
}
