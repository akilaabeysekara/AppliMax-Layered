package com.applimax.project.dao.custom.impl;

import com.applimax.project.dao.custom.OrderDetailDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDetailDAOImpl implements OrderDetailDAO {
    @Override
    public boolean save(Object dto) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Object dto) throws SQLException {
        return false;
    }

    @Override
    public String getNextId() throws SQLException {
        return "";
    }

    @Override
    public String getLastId() throws SQLException {
        return "";
    }

    @Override
    public Optional findById(String selectedId) throws SQLException {
        return Optional.empty();
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        return null;
    }

    @Override
    public boolean delete(String ID) throws SQLException {
        return false;
    }

    @Override
    public List getAll() throws SQLException {
        return List.of();
    }
}