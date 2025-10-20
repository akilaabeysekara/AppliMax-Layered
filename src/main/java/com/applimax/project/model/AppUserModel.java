package com.applimax.project.model;

import com.applimax.project.dto.AppUserDTO;
import com.applimax.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AppUserModel {
    public boolean checkUser(String username, String password) throws SQLException, ClassNotFoundException {
        String sql = "select * from app_user where username = ? and password = ?";
        ResultSet rs = CrudUtil.execute(sql, username, password);
        return rs.next();
    }

    public ArrayList<AppUserDTO> getAllUsers() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM app_user");

        ArrayList<AppUserDTO> list = new ArrayList<>();

        while (resultSet.next()) {
            AppUserDTO appUserDTO = new AppUserDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );

            list.add(appUserDTO);
        }
        return list;
    }

    public String getNextId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT user_id FROM app_user ORDER BY user_id DESC LIMIT 1");
        char tableChar = 'U';
        return getString(resultSet, tableChar);
    }

    static String getString(ResultSet resultSet, char tableChar) throws SQLException {
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNUmberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNUmberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }

    public boolean saveUser(AppUserDTO appUserDTO) throws SQLException, ClassNotFoundException {

        return CrudUtil.execute(
                "INSERT INTO app_user VALUES (?,?,?,?,?)",
                appUserDTO.getUserId(),
                appUserDTO.getEmployeeId(),
                appUserDTO.getUserName(),
                appUserDTO.getPassword(),
                appUserDTO.getEmail()
        );
    }

    public boolean updateUser(AppUserDTO appUserDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE app_user SET employee_id=?, username=?, password=?, email=? WHERE user_id=?",
                appUserDTO.getEmployeeId(),
                appUserDTO.getUserName(),
                appUserDTO.getPassword(),
                appUserDTO.getEmail(),
                appUserDTO.getUserId()
        );
    }

    public boolean deleteUser(String userId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "DELETE FROM app_user WHERE user_id = ?",
                userId
        );
    }

    public ArrayList<String> getAllItemIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT user_id FROM app_user"
        );
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }

    public String findNameById(String userId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT username FROM app_user WHERE user_id=?",
                userId
        );
        if (rst.next()) {
            return rst.getString(1);
        }
        return "";
    }

    public AppUserDTO findById(String selectedId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM app_user WHERE user_id=?",
                selectedId
        );
        if (resultSet.next()) {
            return new AppUserDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
        }
        return null;
    }
}