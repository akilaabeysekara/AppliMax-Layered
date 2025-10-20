package com.applimax.project.controller;

import com.applimax.project.dto.ItemDTO;
import com.applimax.project.model.ItemModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemPageController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<ItemDTO, String> colBrand;

    @FXML
    private TableColumn<ItemDTO, String> colCategory;

    @FXML
    private TableColumn<ItemDTO, String> colId;

    @FXML
    private TableColumn<ItemDTO, String> colName;

    @FXML
    private TableColumn<ItemDTO, String> colPrice;

    @FXML
    private TableColumn<ItemDTO, Integer> colQtyOnHand;

    @FXML
    private Label lblId;

    @FXML
    private TableView<ItemDTO> tblItem;

    @FXML
    private TextField txtBrand;

    @FXML
    private TextField txtCategory;

    @FXML
    private TextField txtQtyOnHand;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPrice;

    private final ItemModel itemModel = new ItemModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("itemCategory"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("itemBrand"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        try {
            resetPage();
        } catch (Exception e) {
            showError("Failed to load item data.");
        }
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
        loadTableData();
        clearFields();

        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        btnSave.setDisable(false);
    }

    private void clearFields() {
        txtName.clear();
        txtCategory.clear();
        txtBrand.clear();
        txtPrice.clear();
        txtQtyOnHand.clear();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(itemModel.getNextId());
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        tblItem.setItems(FXCollections.observableArrayList(itemModel.getAllItem()));
    }

    public void onClickTable(MouseEvent mouseEvent) {
        ItemDTO selected = tblItem.getSelectionModel().getSelectedItem();

        if (selected != null) {
            lblId.setText(selected.getItemId());
            txtName.setText(selected.getItemName());
            txtCategory.setText(selected.getItemCategory());
            txtBrand.setText(selected.getItemBrand());
            txtPrice.setText(selected.getItemPrice());
            txtQtyOnHand.setText(String.valueOf(selected.getQtyOnHand()));

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    @FXML
    void btnItemDeleteOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this item?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = itemModel.deleteItem(lblId.getText());
                if (isDeleted) {
                    resetPage();
                    showInfo("Item deleted successfully.");
                } else {
                    showError("Cannot delete item. It may be referenced in other records.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error occurred while deleting item.");
            }
        }
    }

    @FXML
    void btnItemResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        resetPage();
    }

    @FXML
    void btnItemSaveOnAction(ActionEvent event) {
        String itemId = lblId.getText();
        String itemName = txtName.getText();
        String itemCategory = txtCategory.getText();
        String qtyText = txtQtyOnHand.getText();
        String itemBrand = txtBrand.getText();
        String itemPrice = txtPrice.getText();

        if (itemId.isEmpty() || itemName.isEmpty() || itemCategory.isEmpty() ||
                qtyText.isEmpty() || itemBrand.isEmpty() || itemPrice.isEmpty()) {
            showError("Please fill in all fields before saving.");
            return;
        }

        int qtyOnHand;

        try {
            qtyOnHand = Integer.parseInt(qtyText);
        } catch (NumberFormatException e) {
            showError("Quantity must be a valid number.");
            return;
        }

        ItemDTO dto = new ItemDTO(itemId, itemName, itemCategory, qtyOnHand, itemBrand, itemPrice);

        try {
            boolean isSaved = itemModel.saveItem(dto);
            if (isSaved) {
                resetPage();
                showInfo("Item saved successfully!");
            } else {
                showError("Failed to save item.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error occurred while saving item.");
        }
    }


    @FXML
    void btnItemUpdateOnAction(ActionEvent event) {
        String itemId = lblId.getText();
        String itemName = txtName.getText();
        String itemCategory = txtCategory.getText();
        int itemQtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
        String itemBrand = txtBrand.getText();
        String itemPrice = txtPrice.getText();

        ItemDTO dto = new ItemDTO(itemId, itemName, itemCategory,itemQtyOnHand ,itemBrand, itemPrice);

        try {
            boolean isUpdated = itemModel.updateItem(dto);
            if (isUpdated) {
                resetPage();
                showInfo("Item updated successfully!");
            } else {
                showError("Failed to update item.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error occurred while updating item.");
        }
    }

    private void showInfo(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).show();
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }
}
