package com.applimax.project.controller;

import com.applimax.project.db.DBConnection;
import com.applimax.project.dto.tm.SalesTM;
import com.applimax.project.model.ItemModel;
import com.applimax.project.model.SalesModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;


import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SalesPageController implements Initializable {

    @FXML
    private Button btnReport;

    @FXML
    private ComboBox<String> cmbItemId;

    @FXML
    private TableColumn<SalesTM, String> colId;

    @FXML
    private TableColumn<SalesTM, String> colItemName;

    @FXML
    private TableColumn<SalesTM, Integer> colQtyLeftStock;

    @FXML
    private TableColumn<SalesTM, Integer> colQtySold;

    @FXML
    private TableColumn<SalesTM, Double> colSoldAmount;

    @FXML
    private TableColumn<SalesTM, String> colSoldDate;

    @FXML
    private TableColumn<SalesTM, Double> colUnitPrice;

    @FXML
    private Label lblId;

    @FXML
    private TableView<SalesTM> tblsales;

    private final SalesModel salesModel = new SalesModel();
    private final ItemModel itemModel = new ItemModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTableColumns();
        loadItemIds();
        loadAllSales();
    }

    private void loadTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("salesId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQtyLeftStock.setCellValueFactory(new PropertyValueFactory<>("qtyLeftStock"));
        colQtySold.setCellValueFactory(new PropertyValueFactory<>("qtySold"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colSoldAmount.setCellValueFactory(new PropertyValueFactory<>("soldAmount"));
        colSoldDate.setCellValueFactory(new PropertyValueFactory<>("soldDate"));

        try {
            tblsales.setItems(FXCollections.observableArrayList(salesModel.getAllSales()));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void loadItemIds() {
        try {
            cmbItemId.setItems(FXCollections.observableArrayList(itemModel.getAllItemIds()));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllSales() {
        try {
            tblsales.setItems(FXCollections.observableArrayList(salesModel.getAllSales()));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnSalesReportOnAction(ActionEvent event) {
        try {
            // Load and compile the Jasper report
            JasperReport report = (JasperReport) JRLoader.loadObjectFromFile("/report/AllSalesReport.jrxml");


            // Get database connection
            Connection connection = DBConnection.getInstance().getConnection();

            // Fill the report (no parameters)
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    report,
                    new HashMap<>(),
                    connection
            );

            // View the report
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void cmbItemIdOnAction(ActionEvent event) {
        String selectedId = cmbItemId.getValue();
        if (selectedId != null) {
            try {
                tblsales.setItems(FXCollections.observableArrayList(salesModel.getSalesByItem(selectedId)));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        SalesTM selected = tblsales.getSelectionModel().getSelectedItem();
        if (selected != null) {
            lblId.setText(selected.getSalesId());
        }
    }

}