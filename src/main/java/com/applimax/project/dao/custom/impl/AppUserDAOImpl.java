package com.applimax.project.dao.custom.impl;

import com.applimax.project.dao.SQLUtil;
import com.applimax.project.dao.custom.AppUserDAO;
import com.applimax.project.entity.AppUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppUserDAOImpl implements AppUserDAO {
    @Override
    public boolean save(Object dto) throws SQLException, ClassNotFoundException {
        AppUser entity = (AppUser) dto;
        return SQLUtil.execute(
                "INSERT INTO app_user (user_id, employee_id, username, password, email) VALUES (?, ?, ?, ?, ?)",
                entity.getUserId(),
                entity.getEmployeeId(),
                entity.getUserName(),
                entity.getPassword(),
                entity.getEmail()
        );
    }

    @Override
    public boolean update(Object dto) throws SQLException, ClassNotFoundException {
        AppUser entity = (AppUser) dto;
        return SQLUtil.execute(
                "UPDATE app_user SET employee_id=?, username=?, password=?, email=? WHERE user_id=?",
                entity.getEmployeeId(),
                entity.getUserName(),
                entity.getPassword(),
                entity.getEmail(),
                entity.getUserId()
        );
    }

    @Override
    public String getNextId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT user_id FROM app_user ORDER BY user_id DESC LIMIT 1");
        if (resultSet.next()) {
            String lastId = resultSet.getString("user_id");
            int newId = Integer.parseInt(lastId.replace("U", "")) + 1;
            return String.format("U%03d", newId);
        }
        return "U001";
    }

    @Override
    public String getLastId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT user_id FROM app_user ORDER BY user_id DESC LIMIT 1");
        if (resultSet.next()) {
            return resultSet.getString("user_id");
        }
        return null;
    }

    @Override
    public Optional findById(String selectedId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM app_user WHERE user_id=?", selectedId);
        if (resultSet.next()) {
            return Optional.of(new AppUser(
                    resultSet.getString("user_id"),
                    resultSet.getString("employee_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("email")
            ));
        }
        return Optional.empty();
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException, ClassNotFoundException {
        ArrayList<String> ids = new ArrayList<>();
        ResultSet resultSet = SQLUtil.execute("SELECT user_id FROM app_user");
        while (resultSet.next()) {
            ids.add(resultSet.getString("user_id"));
        }
        return ids;
    }

    @Override
    public boolean delete(String ID) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("DELETE FROM app_user WHERE user_id=?", ID);
    }

    @Override
    public List getAll() throws SQLException, ClassNotFoundException {
        List<AppUser> users = new ArrayList<>();
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM app_user");
        while (resultSet.next()) {
            users.add(new AppUser(
                    resultSet.getString("user_id"),
                    resultSet.getString("employee_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("email")
            ));
        }
        return users;
    }
}