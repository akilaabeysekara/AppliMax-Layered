package com.applimax.project.controller;

import com.applimax.project.dto.tm.BestSellingTM;
import com.applimax.project.model.*;
import com.applimax.project.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;

public class OpenPageController {

    @FXML
    private TableColumn<BestSellingTM, String> colBrand;

    @FXML
    private TableColumn<BestSellingTM, String> colCategory;

    @FXML
    private TableColumn<BestSellingTM, String> colName;

    @FXML
    private TableColumn<BestSellingTM, String> colPrice;

    @FXML
    private TableColumn<BestSellingTM, String> colQtyOnHand;

    @FXML
    private Label lblTotalCustomers;

    @FXML
    private Label lblTotalItems;

    @FXML
    private Label lblTotalOrders;

    @FXML
    private Label lblTotalSales;

    @FXML
    private TableView<BestSellingTM> tblItemOnOpenPage;

    private final BestSellingModel bestSellingModel = new BestSellingModel();
    private final ItemModel itemModel=new ItemModel();
    private final CustomerModel customerModel=new CustomerModel();
    private final OrderModel orderModel=new OrderModel();
    private final SalesModel salesModel=new SalesModel();

    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        try {
            tblItemOnOpenPage.setItems(FXCollections.observableArrayList(bestSellingModel.getBestSellingItems()));
            setlables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setlables() throws SQLException, ClassNotFoundException {
        lblTotalItems.setText(itemModel.getItemCount());
        lblTotalCustomers.setText(customerModel.getCustomerCount());
        lblTotalOrders.setText(orderModel.getOrderCount());
        lblTotalSales.setText(salesModel.getSalesCount());
    }

    @FXML
    void onClickTable(MouseEvent event) {

    }

}