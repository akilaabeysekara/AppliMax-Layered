package com.applimax.project.controller;

import com.applimax.project.db.DBConnection;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import com.applimax.project.dto.CustomerDTO;
import com.applimax.project.model.CustomerModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerPageController implements Initializable {
    public Label lblId;
    public TextField txtName;
    public TextField txtNic;
    public TextField txtEmail;
    public TextField txtPhone;
    public TextField txtAddress;

    public TableView<CustomerDTO> tblCustomer;
    public TableColumn<CustomerDTO, String> colId;
    public TableColumn<CustomerDTO, String> colName;
    public TableColumn<CustomerDTO, String> colNic;
    public TableColumn<CustomerDTO, String> colEmail;
    public TableColumn<CustomerDTO, String> colPhone;
    public TableColumn<CustomerDTO, String> colAddress;

    public Button btnDelete;
    public Button btnUpdate;
    public Button btnSave;

    private final CustomerModel customerModel = new CustomerModel();

    // Validation patterns
    private final String namePattern = "^[A-Za-z ]+$";
    private final String nicPattern = "^(\\d{9}[vVxX])|(\\d{12})$";
    private final String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private final String phonePattern = "^\\d{10}$";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("customerNic"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("customerEmail"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load customer data.");
        }
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
        loadTableData();

        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        btnSave.setDisable(false);

       clearFields();
    }

    private void clearFields() {
        txtName.clear();
        txtNic.clear();
        txtEmail.clear();
        txtPhone.clear();
        txtAddress.clear();
    }

    private void resetFieldStyles() {
        txtName.setStyle("-fx-background-radius: 3; -fx-background-color: rgba(216,216,255,0.88); -fx-border-width: 0 0 2 0; -fx-border-color: black;");
        txtNic.setStyle("-fx-background-radius: 3; -fx-background-color: rgba(216,216,255,0.88); -fx-border-width: 0 0 2 0; -fx-border-color: black;");
        txtEmail.setStyle("-fx-background-radius: 3; -fx-background-color: rgba(216,216,255,0.88); -fx-border-width: 0 0 2 0; -fx-border-color: black;");
        txtPhone.setStyle("-fx-background-radius: 3; -fx-background-color: rgba(216,216,255,0.88); -fx-border-width: 0 0 2 0; -fx-border-color: black;");
        txtAddress.setStyle("-fx-background-radius: 3; -fx-background-color: rgba(216,216,255,0.88); -fx-border-width: 0 0 2 0; -fx-border-color: black;");
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        tblCustomer.setItems(FXCollections.observableArrayList(customerModel.getAllCustomer()));
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(customerModel.getNextId());
    }

    public void btnCustomerSaveOnAction(ActionEvent event) {
        CustomerDTO customer = extractAndValidateInput();
        if (customer == null) return;

        try {
            boolean isSaved = customerModel.saveCustomer(customer);
            if (isSaved) {
                resetPage();
                showInfo("Customer saved successfully!");
            } else {
                showError("Failed to save customer.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error occurred while saving customer.");
        }
    }

    public void btnCustomerUpdateOnAction(ActionEvent event) {
        CustomerDTO customer = extractAndValidateInput();
        if (customer == null) return;

        try {
            boolean isUpdated = customerModel.updateCustomer(customer);
            if (isUpdated) {
                resetPage();
                showInfo("Customer updated successfully!");
            } else {
                showError("Failed to update customer.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error occurred while updating customer.");
        }
    }

    public void btnCustomerDeleteOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = customerModel.deleteCustomer(lblId.getText());
                if (isDeleted) {
                    resetPage();
                    showInfo("Customer deleted successfully.");
                } else {
                    showError("Failed to delete customer.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error occurred while deleting customer.");
            }
        }
    }

    public void btnCustomerResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        resetPage();
    }

    public void btnCustomerGenerateReport(ActionEvent event) {
            try {
                JasperReport jasperReport = JasperCompileManager.compileReport(
                        getClass().getResourceAsStream("/report/AllCustomerReport.jrxml")
                );

                Connection connection = DBConnection.getInstance().getConnection();

                Map<String, Object> parameters = new HashMap<>();

                JasperPrint jasperPrint = JasperFillManager.fillReport(
                        jasperReport,
                        parameters,
                        connection
                );

                JasperViewer.viewReport(jasperPrint, false);


            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    public void onClickTable(MouseEvent event) {
        CustomerDTO selected = tblCustomer.getSelectionModel().getSelectedItem();

        if (selected != null) {
            lblId.setText(selected.getCustomerId());
            txtName.setText(selected.getCustomerName());
            txtNic.setText(selected.getCustomerNic());
            txtEmail.setText(selected.getCustomerEmail());
            txtPhone.setText(selected.getCustomerPhone());
            txtAddress.setText(selected.getCustomerAddress());

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    private CustomerDTO extractAndValidateInput() {
        String id = lblId.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();

        resetFieldStyles();

        boolean valid = true;

        if (!name.matches(namePattern)) {
            txtName.setStyle("-fx-background-radius: 3; -fx-background-color: rgba(216,216,255,0.88); -fx-border-color: red;");
            valid = false;
        }
        if (!nic.matches(nicPattern)) {
            txtNic.setStyle("-fx-background-radius: 3; -fx-background-color: rgba(216,216,255,0.88);  -fx-border-color: red;");
            valid = false;
        }
        if (!email.matches(emailPattern)) {
            txtEmail.setStyle("-fx-background-radius: 3; -fx-background-color: rgba(216,216,255,0.88);  -fx-border-color: red;");
            valid = false;
        }
        if (!phone.matches(phonePattern)) {
            txtPhone.setStyle("-fx-background-radius: 3; -fx-background-color: rgba(216,216,255,0.88);  -fx-border-color: red;");
            valid = false;
        }
        if(address.isEmpty()){
            txtAddress.setStyle("-fx-background-radius: 3; -fx-background-color: rgba(216,216,255,0.88);  -fx-border-color: red;");
            valid = false;
        }

        if (!valid) {
            return null;
        }

        return new CustomerDTO(id, name, nic, email, phone, address);
    }

    private void showInfo(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).show();
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }
}
