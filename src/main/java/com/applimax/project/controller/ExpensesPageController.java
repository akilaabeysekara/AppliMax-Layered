package com.applimax.project.controller;

import com.applimax.project.dto.SupplierPaymentDTO;
import com.applimax.project.model.SupplierModel;
import com.applimax.project.model.SupplierPaymentModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ExpensesPageController {

    @FXML
    private Button btnDelete, btnReport, btnReset, btnSave, btnUpdate;

    @FXML
    private ComboBox<String> cmbPayMethod, cmbSelectMonth, cmbSupplierId;

    @FXML
    private TableColumn<SupplierPaymentDTO, String> colId, colName, colPayMethod;
    @FXML
    private TableColumn<SupplierPaymentDTO, Double> colPayAmount;
    @FXML
    private TableColumn<SupplierPaymentDTO, LocalDate> colPayDate;

    @FXML
    private DatePicker dpDate;

    @FXML
    private Label lblId, lblSupplierName;

    @FXML
    private TableView<SupplierPaymentDTO> tblSalary;

    @FXML
    private TextField txtPayAmount;

    private final SupplierPaymentModel supplierPaymentModel = new SupplierPaymentModel();
    private final SupplierModel supplierModel = new SupplierModel();

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        loadSupplierIds();
        cmbPayMethod.setItems((FXCollections.observableArrayList("Cash", "Card")));
        loadPaymentsToTable();
        generateNextId();
        ObservableList<String> months = FXCollections.observableArrayList(
                "January", "February", "March", "April", "May",
                "June", "July", "August", "September",
                "October", "November", "December"
        );

        cmbSelectMonth.setItems(months);

        cmbSupplierId.setOnAction(this::cmbSupplierIdOnAction);
    }

    private void loadSupplierIds() {
        try {
            ArrayList<String> ids = supplierModel.getAllSupplierIds();
            cmbSupplierId.setItems(FXCollections.observableArrayList(ids));
        } catch (SQLException | ClassNotFoundException e) {
            showAlert("Error", "Failed to load supplier IDs");
        }
    }


    private void loadPaymentsToTable() throws SQLException, ClassNotFoundException {
        colId.setCellValueFactory(new PropertyValueFactory<>("expensesId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colPayAmount.setCellValueFactory(new PropertyValueFactory<>("payAmount"));
        colPayDate.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colPayMethod.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        tblSalary.setItems(FXCollections.observableArrayList(supplierPaymentModel.getAllSupplierPayments()));


    }

    private String getSupplierName(String supplierId) {
        try {
            return supplierModel.findNameById(supplierId);
        } catch (SQLException | ClassNotFoundException e) {
            return "N/A";
        }
    }

    @FXML
    void btnSalarySaveOnAction(ActionEvent event) {
        SupplierPaymentDTO dto = getFormData();
        if (dto == null) return;

        try {
            if (supplierPaymentModel.saveSupplierPayment(dto)) {
                showAlert("Success", "Payment saved");
                resetForm();
                loadPaymentsToTable();
                generateNextId();
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert("Error", "Failed to save payment");
        }
    }

    private void generateNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(supplierPaymentModel.getNextExpensesId());
    }

    @FXML
    void btnSalaryUpdateOnAction(ActionEvent event) {
        SupplierPaymentDTO dto = getFormData();
        if (dto == null) return;

        try {
            if (supplierPaymentModel.updateSupplierPayment(dto)) {
                showAlert("Success", "Payment updated");
                resetForm();
                loadPaymentsToTable();
                generateNextId();
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert("Error", "Failed to update payment");
        }
    }

    @FXML
    void btnSalaryDeleteOnAction(ActionEvent event) {
        String id = lblId.getText();
        try {
            if (supplierPaymentModel.deleteSupplierPayment(id)) {
                showAlert("Success", "Payment deleted");
                resetForm();
                loadPaymentsToTable();
                generateNextId();
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert("Error", "Failed to delete payment");
        }
    }

    @FXML
    void btnSalaryResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        resetForm();
        generateNextId();
    }

    @FXML
    void cmbSupplierIdOnAction(ActionEvent event) {
        String id = cmbSupplierId.getValue();
        if (id != null) {
            lblSupplierName.setText(getSupplierName(id));
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        SupplierPaymentDTO selected = tblSalary.getSelectionModel().getSelectedItem();
        if (selected != null) {
            lblId.setText(selected.getExpensesId());
            cmbSupplierId.setValue(selected.getSupplierId());
            txtPayAmount.setText(String.valueOf(selected.getPayAmount()));
            cmbPayMethod.setValue(selected.getPaymentMethod());
            dpDate.setValue(selected.getPaymentDate());
            lblSupplierName.setText(getSupplierName(selected.getSupplierId()));
        }
    }

    @FXML
    void btnSalaryReportOnAction(ActionEvent event) {
        showAlert("Info", "Report generation not implemented yet.");
    }

    private SupplierPaymentDTO getFormData() {
        String id = lblId.getText();
        String supplierId = cmbSupplierId.getValue();
        String method = cmbPayMethod.getValue();
        LocalDate date = dpDate.getValue();

        if (supplierId == null || method == null || date == null || txtPayAmount.getText().isEmpty()) {
            showAlert("Validation Error", "Please complete all fields");
            return null;
        }

        double amount;
        try {
            amount = Double.parseDouble(txtPayAmount.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Pay amount must be a valid number");
            return null;
        }

        return new SupplierPaymentDTO(id, supplierId, amount, method, date);
    }

    private void resetForm() {
        cmbSupplierId.setValue(null);
        cmbPayMethod.setValue(null);
        dpDate.setValue(null);
        txtPayAmount.clear();
        lblSupplierName.setText("");
        tblSalary.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
      Alert alert=  new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
      alert.setTitle(title);
      alert.show();

    }

    public void cmbSelectMonthOnAction(ActionEvent actionEvent) {
    }

    public void cmbPayMethodOnAction(ActionEvent actionEvent) {

    }
}
