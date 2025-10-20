package com.applimax.project.controller;

import com.applimax.project.dto.InventoryDTO;
import com.applimax.project.model.InventoryModel;
import com.applimax.project.model.ItemModel;
import com.applimax.project.model.SupplierModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InventoryPageController implements Initializable {

    @FXML private Button btnDelete, btnReport, btnReset, btnSave, btnUpdate;
    @FXML private ComboBox<String> cmbItemId, cmbSupplierId;
    @FXML private TableColumn<InventoryDTO, String> colDate, colId, colName, colItemName;
    @FXML private TableColumn<InventoryDTO, Integer> colQty;
    @FXML private TableColumn<InventoryDTO, Double> colUnitPrice;
    @FXML private Label lblId, lblItemName, lblSupplierName;
    @FXML private TableView<InventoryDTO> tblInventory;
    @FXML private TextField txtQtyOnStock, txtUnitPrice;

    private final ItemModel itemModel = new ItemModel();
    private final SupplierModel supplierModel = new SupplierModel();
    private final InventoryModel inventoryModel = new InventoryModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadItemIds();
            loadSupplierIds();
            loadTable();
            generateNextId();
            initializeColumns();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadItemIds() throws SQLException, ClassNotFoundException {
        cmbItemId.setItems(FXCollections.observableArrayList(itemModel.getAllItemIds()));
    }

    private void loadSupplierIds() throws SQLException, ClassNotFoundException {
        cmbSupplierId.setItems(FXCollections.observableArrayList(supplierModel.getAllSupplierIds()));
    }

    private void loadTable() throws SQLException, ClassNotFoundException {
        ArrayList<InventoryDTO> inventoryList = inventoryModel.getAllInventory();
        tblInventory.setItems(FXCollections.observableArrayList(inventoryList));
    }

    private void generateNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(inventoryModel.getNextId());
    }

    private void initializeColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("receivedDate"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
    }

    @FXML
    void cmbItemIdOnAction(ActionEvent event) {
        String itemId = cmbItemId.getValue();
        try {
            String itemName = itemModel.findNameById(itemId);
            lblItemName.setText(itemName);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }    }

    @FXML
    void cmbSupplierIdOnAction(ActionEvent event) {
        String supplierId = cmbSupplierId.getValue();
        try {
            String supplierName = supplierModel.findNameById(supplierId);
            lblSupplierName.setText(supplierName);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
}
    @FXML
    void btnInventoryDeleteOnAction(ActionEvent event) {
        String inventoryId = lblId.getText();
        try {
            boolean isDeleted = inventoryModel.deleteInventory(inventoryId);
            if (isDeleted) {
                loadTable();
                resetFields();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnInventoryReportOnAction(ActionEvent event) {
        System.out.println("Report generation is not implemented yet.");
    }

    @FXML
    void btnInventoryResetOnAction(ActionEvent event) {
        resetFields();
    }

    private void resetFields() {
        try {
            generateNextId();
            cmbItemId.setValue(null);
            cmbSupplierId.setValue(null);
            lblItemName.setText("");
            lblSupplierName.setText("");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnInventorySaveOnAction(ActionEvent event) {
        try {
            InventoryDTO inventoryDTO = createInventoryFromInputs();
            boolean isSaved = inventoryModel.saveInventory(inventoryDTO);
            if (isSaved) {
                loadTable();
                resetFields();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnInventoryUpdateOnAction(ActionEvent actionEvent) {
        try {
            InventoryDTO inventoryDTO = createInventoryFromInputs();
            boolean isUpdated = inventoryModel.updateInventory(inventoryDTO);
            if (isUpdated) {
                loadTable();
                resetFields();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private InventoryDTO createInventoryFromInputs() {
        String inventoryId = lblId.getText();
        String itemName = lblItemName.getText();
        String supplierName = lblSupplierName.getText();
        LocalDateTime receivedDate = LocalDateTime.now();

        return new InventoryDTO(
                inventoryId,
                itemName,
                supplierName,
                receivedDate
        );
    }

    @FXML
    void onClickTable(MouseEvent event) {
        InventoryDTO selectedItem = tblInventory.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            lblId.setText(selectedItem.getInventoryId());
            lblItemName.setText(selectedItem.getItemName());
            lblSupplierName.setText(selectedItem.getSupplierName());
        }

    }

    @FXML
    void setTotal(KeyEvent event) {
        try {
            double price = Double.parseDouble(txtUnitPrice.getText());
            int qty = Integer.parseInt(txtQtyOnStock.getText());
            double total = price * qty;
            System.out.println("Total: " + total);
        } catch (NumberFormatException e) {
        }
    }
}
