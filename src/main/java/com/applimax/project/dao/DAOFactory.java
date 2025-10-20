package com.applimax.project.dao;

import com.applimax.project.dao.custom.impl.*;
import com.applimax.project.dao.custom.impl.*;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {

    }

    public static DAOFactory getInstance() {
        return daoFactory == null ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOTypes {
        APP_USER, ITEM, ORDER, SALES, SALARY, CUSTOMER, EMPLOYEE, SUPPLIER, INVENTORY, ATTENDANCE, BEST_SELLING, ORDER_DETAIL, FORGOT_PASSWORD, SUPPLIER_PAYMENT
    }

    @SuppressWarnings("unchecked")
    public <T extends SuperDAO> T getDAO(DAOTypes type) {
        return switch (type) {
            case APP_USER -> (T) new AppUserDAOImpl();
            case ITEM -> (T) new ItemDAOImpl();
            case ORDER -> (T) new OrderDAOImpl();
            case SALES -> (T) new SalesDAOImpl();
            case SALARY -> (T) new SalaryDAOImpl();
            case CUSTOMER -> (T) new CustomerDAOImpl();
            case EMPLOYEE -> (T) new EmployeeDAOImpl();
            case SUPPLIER -> (T) new SupplierDAOImpl();
            case INVENTORY -> (T) new InventoryDAOImpl();
            case ATTENDANCE -> (T) new AttendanceDAOImpl();
            case BEST_SELLING -> (T) new BestSellingDAOImpl();
            case ORDER_DETAIL -> (T) new OrderDetailDAOImpl();
            case FORGOT_PASSWORD -> (T) new ForgotPasswordDAOImpl();
            case SUPPLIER_PAYMENT -> (T) new SupplierPaymentDAOImpl();
        };
    }
}