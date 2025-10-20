package com.applimax.project.controller;

import com.applimax.project.db.DBConnection;
import com.applimax.project.dto.EmployeeDTO;
import com.applimax.project.model.EmployeeModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeRegistrationController implements Initializable {

    @FXML
    private TableColumn<EmployeeDTO, String> colAddress;

    @FXML
    private Button btnDelete, btnReport, btnReset, btnSave, btnUpdate;

    @FXML
    private TableColumn<EmployeeDTO, String> colEmail, colId, colName, colNic, colPhone, colRole;

    @FXML
    private Label lblId;

    @FXML
    private TableView<EmployeeDTO> tblEmployee;

    @FXML
    private TextField txtAddress, txtEmail, txtName, txtNic, txtPhone, txtRole;

    private final EmployeeModel employeeModel = new EmployeeModel();

    // Validation patterns
    private final String namePattern = "^[A-Za-z ]+$";
    private final String nicPattern = "^(\\d{9}[vVxX])|(\\d{12})$";
    private final String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private final String phonePattern = "^\\d{10}$";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        try {
            resetPage();
        } catch (Exception e) {
            showError("Failed to load employee data.");
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
        txtEmail.clear();
        txtPhone.clear();
        txtAddress.clear();
        txtRole.clear();
        resetFieldStyles();
    }

    private void resetFieldStyles() {
        String defaultStyle = "-fx-background-radius: 3; -fx-background-color: rgba(216,216,255,0.88); -fx-border-width: 0 0 2 0; -fx-border-color: black;";
        txtName.setStyle(defaultStyle);
        txtNic.setStyle(defaultStyle);
        txtEmail.setStyle(defaultStyle);
        txtPhone.setStyle(defaultStyle);
        txtAddress.setStyle(defaultStyle);
        txtRole.setStyle(defaultStyle);
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        tblEmployee.setItems(FXCollections.observableArrayList(employeeModel.getAllEmployee()));
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(employeeModel.getNextId());
    }

    @FXML
    void btnEmployeeSaveOnAction(ActionEvent event) {
        EmployeeDTO employee = extractAndValidateInput();
        if (employee == null) return;

        try {
            if (employeeModel.saveEmployee(employee)) {
                resetPage();
                showInfo("Employee saved successfully!");
            } else {
                showError("Failed to save employee.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error occurred while saving employee.");
        }
    }

    @FXML
    void btnEmployeeUpdateOnAction(ActionEvent event) {
        EmployeeDTO employee = extractAndValidateInput();
        if (employee == null) return;

        try {
            if (employeeModel.updateEmployee(employee)) {
                resetPage();
                showInfo("Employee updated successfully!");
            } else {
                showError("Failed to update employee.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error occurred while updating employee.");
        }
    }

    @FXML
    void btnEmployeeDeleteOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                if (employeeModel.deleteEmployee(lblId.getText())) {
                    resetPage();
                    showInfo("Employee deleted successfully.");
                } else {
                    showError("Failed to delete employee.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error occurred while deleting employee.");
            }
        }
    }

    @FXML
    void btnEmployeeResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        resetPage();
    }

    @FXML
    void btnEmployeeReportOnAction(ActionEvent event) {
        showInfo("Report generation feature is not implemented yet.");
    }

    @FXML
    void onClickTable(MouseEvent event) {
        EmployeeDTO selected = tblEmployee.getSelectionModel().getSelectedItem();
        if (selected != null) {
            lblId.setText(selected.getEmployeeId());
            txtName.setText(selected.getName());
            txtNic.setText(selected.getNic());
            txtEmail.setText(selected.getEmail());
            txtPhone.setText(selected.getPhoneNo());
            txtAddress.setText(selected.getAddress());
            txtRole.setText(selected.getRole());

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    private EmployeeDTO extractAndValidateInput() {
        String id = lblId.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();
        String role = txtRole.getText();

        resetFieldStyles();

        boolean valid = true;

        if (!name.matches(namePattern)) {
            txtName.setStyle("-fx-border-color: red;");
            valid = false;
        }
        if (!nic.matches(nicPattern)) {
            txtNic.setStyle("-fx-border-color: red;");
            valid = false;
        }
        if (!email.matches(emailPattern)) {
            txtEmail.setStyle("-fx-border-color: red;");
            valid = false;
        }
        if (!phone.matches(phonePattern)) {
            txtPhone.setStyle("-fx-border-color: red;");
            valid = false;
        }
        if (address.isBlank()) {
            txtAddress.setStyle("-fx-border-color: red;");
            valid = false;
        }

        return valid ? new EmployeeDTO(id, name, nic, email, phone, address, role) : null;
    }

    private void showInfo(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).show();
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }

    public void setTotal(KeyEvent keyEvent) {
    }

    public void generateAllCustomerReport(ActionEvent actionEvent) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/report/Employee.jrxml")
            );

            Connection connection = DBConnection.getInstance().getConnection();

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("p_date", LocalDate.now().toString());

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    connection
            );

            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to generate employee report.");
        }
    }
    }

