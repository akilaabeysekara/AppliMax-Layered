package com.applimax.project.model;

import com.applimax.project.dto.tm.SalaryTM;
import com.applimax.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SalaryModel {

    public ArrayList<SalaryTM> getAllSalary() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM salary");
        ArrayList<SalaryTM> list = new ArrayList<>();

        while (resultSet.next()) {
            SalaryTM salaryTM = new SalaryTM(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDate(4),
                    resultSet.getDouble(5),
                    resultSet.getDouble(6),
                    resultSet.getDouble(7)
            );
            list.add(salaryTM);
        }
        return list;
    }

    public String getNextId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT supplier_id FROM supplier ORDER BY supplier_id DESC LIMIT 1");
        String prefix = "SL";

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

    public boolean saveSalary(SalaryTM salaryTM) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO salary VALUES (?,?,?,?,?,?,?)",
                salaryTM.getSalary_id(),
                salaryTM.getName(),
                salaryTM.getAttendance_id(),
                salaryTM.getSalary_date(),
                salaryTM.getBasic_salary(),
                salaryTM.getAllowances(),
                salaryTM.getTotal_salary()
        );
    }

    public boolean updateSalary(SalaryTM salaryTM) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE salary SET name=?, attendance_id=?, salary_date=?, basic_salary=?, allowances=?, total_salary=? WHERE salary_id=?",
                salaryTM.getName(),
                salaryTM.getAttendance_id(),
                salaryTM.getSalary_date(),
                salaryTM.getBasic_salary(),
                salaryTM.getAllowances(),
                salaryTM.getTotal_salary(),
                salaryTM.getSalary_id()
        );
    }

    public boolean deleteSalary(String salaryId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "DELETE FROM salary WHERE salary_id=?",
                salaryId
        );
    }

    public SalaryTM findById(String selectedId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM salary WHERE salary_id=?",
                selectedId
        );
        if (resultSet.next()) {
            return new SalaryTM(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDate(4),
                    resultSet.getDouble(5),
                    resultSet.getDouble(6),
                    resultSet.getDouble(7)
            );
        }
        return null;
    }

}