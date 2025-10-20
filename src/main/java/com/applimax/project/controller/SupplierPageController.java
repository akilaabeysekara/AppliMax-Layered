package com.applimax.project.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import com.applimax.project.dto.SupplierDTO;
import com.applimax.project.model.SupplierModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierPageController implements Initializable {

    @FXML
    private TableColumn<?, ?> btnAddress;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReport;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<SupplierDTO, String> colEmail;

    @FXML
    private TableColumn<SupplierDTO, String> colId;

    @FXML
    private TableColumn<SupplierDTO, String> colName;

    @FXML
    private TableColumn<SupplierDTO, String> colNic;

    @FXML
    private TableColumn<SupplierDTO, String> colPhone;

    @FXML
    private Label lblId;

    @FXML
    private TableView<SupplierDTO> tblSupplier;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtPhone;

    private final SupplierModel supplierModel = new SupplierModel();

    private final String namePattern = "^[A-Za-z ]+$";
    private final String nicPattern = "^(\\d{9}[vVxX])|(\\d{12})$";
    private final String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private final String phonePattern = "^\\d{10}$";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        btnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load supplier data.");
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
        txtNic.clear();
        txtPhone.clear();
        txtEmail.clear();
        txtAddress.clear();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(supplierModel.getNextId());
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        tblSupplier.setItems(FXCollections.observableArrayList(supplierModel.getAllSuppliers()));
    }

    @FXML
    void btnSupplierDeleteOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = supplierModel.deleteSupplier(lblId.getText());
                if (isDeleted) {
                    resetPage();
                    showInfo("Supplier deleted successfully.");
                } else {
                    showError("Failed to delete supplier.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error occurred while deleting supplier.");
            }
        }
    }

    @FXML
    void btnSupplierReportOnAction(ActionEvent event) {
    }

    @FXML
    void btnSupplierResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        resetPage();
    }

    @FXML
    void btnSupplierSaveOnAction(ActionEvent event) {
        SupplierDTO supplier = extractAndValidateInput();
        if (supplier == null) return;

        try {
            boolean isSaved = supplierModel.saveSupplier(supplier);
            if (isSaved) {
                resetPage();
                showInfo("Supplier saved successfully!");
            } else {
                showError("Failed to save supplier.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error occurred while saving supplier.");
        }
    }

    @FXML
    void btnSupplierUpdateOnAction(ActionEvent event) {
        SupplierDTO supplier = extractAndValidateInput();
        if (supplier == null) return;

        try {
            boolean isUpdated = supplierModel.updateSupplier(supplier);
            if (isUpdated) {
                resetPage();
                showInfo("Supplier updated successfully!");
            } else {
                showError("Failed to update supplier.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error occurred while updating supplier.");
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        SupplierDTO selected = tblSupplier.getSelectionModel().getSelectedItem();

        if (selected != null) {
            lblId.setText(selected.getSupplierId());
            txtName.setText(selected.getName());
            txtNic.setText(selected.getNic());
            txtPhone.setText(selected.getPhoneNo());
            txtEmail.setText(selected.getEmail());
            txtAddress.setText(selected.getAddress());

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    @FXML
    void setTotal(KeyEvent event) {
    }

    private SupplierDTO extractAndValidateInput() {
        String id = lblId.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();
        String address = txtAddress.getText();

        boolean valid = true;

        if (!name.matches(namePattern)) {
            txtName.setStyle("-fx-border-color: red");
            valid = false;
        }
        if (!nic.matches(nicPattern)) {
            txtNic.setStyle("-fx-border-color: red");
            valid = false;
        }
        if (!phone.matches(phonePattern)) {
            txtPhone.setStyle("-fx-border-color: red");
            valid = false;
        }
        if (!email.matches(emailPattern)) {
            txtEmail.setStyle("-fx-border-color: red");
            valid = false;
        }
        if (address.isEmpty()) {
            txtAddress.setStyle("-fx-border-color: red");
            valid = false;
        }

        if (!valid) {
            return null;
        }

        return new SupplierDTO(id, name, nic, phone, email, address);
    }

    private void showInfo(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).show();
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }
}