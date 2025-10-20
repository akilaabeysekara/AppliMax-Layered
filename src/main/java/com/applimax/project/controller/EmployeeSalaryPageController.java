package com.applimax.project.controller;

import com.applimax.project.dto.tm.SalaryTM;
import com.applimax.project.model.AttendanceModel;
import com.applimax.project.model.EmployeeModel;
import com.applimax.project.model.SalaryModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeSalaryPageController implements Initializable {
    private final SalaryModel salaryModel = new SalaryModel();
    private ObservableList<SalaryTM> obList = FXCollections.observableArrayList();

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
    private ComboBox<String> cmbAttendanceId;

    @FXML
    private ComboBox<String> cmbEmployeeId;

    @FXML
    private ComboBox<String> cmbSelectMonth;

    @FXML
    private TableColumn<SalaryTM,Double> colAllowances;

    @FXML
    private TableColumn<SalaryTM,String> colAttendanceId;

    @FXML
    private TableColumn<SalaryTM,Double> colBasic;

    @FXML
    private TableColumn<SalaryTM,String> colDate;

    @FXML
    private TableColumn<SalaryTM, String> colId;

    @FXML
    private TableColumn<SalaryTM, String> colName;

    @FXML
    private TableColumn<SalaryTM,Double> colTotalSalary;

    @FXML
    private DatePicker dpDate;

    @FXML
    private Label lblEmployeeName;

    @FXML
    private Label lblId;

    @FXML
    private TableView<SalaryTM> tblSalary;

    @FXML
    private TextField txtBasicSalary;

    @FXML
    private TextField txtTotalSalary;

    @FXML
    private TextField txtallowances;
    private final EmployeeModel employeeModel = new EmployeeModel();
    private final AttendanceModel attendanceModel = new AttendanceModel();

    @FXML
    void btnSalaryDeleteOnAction(ActionEvent event) {
        String id = lblId.getText();
        try {
            boolean isDeleted = salaryModel.deleteSalary(id);
            if (isDeleted) {
                loadTable();
                clearFields();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSalaryReportOnAction(ActionEvent event) {

    }

    @FXML
    void btnSalaryResetOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        generateNextId();
        lblEmployeeName.setText("");
        cmbAttendanceId.setValue(null);
        setDate();
        txtBasicSalary.clear();
        txtallowances.clear();
        txtTotalSalary.clear();
    }

    @FXML
    void btnSalarySaveOnAction(ActionEvent event) {
        String id = lblId.getText();
        String name = lblEmployeeName.getText();
        String attendanceId = cmbAttendanceId.getValue();
        LocalDate date = dpDate.getValue();
        double basicSalary = Double.parseDouble(txtBasicSalary.getText());
        double allowances = Double.parseDouble(txtallowances.getText());
        double totalSalary = Double.parseDouble(txtTotalSalary.getText());

        try {
            boolean isSaved = salaryModel.saveSalary(new SalaryTM(
                    id, name, attendanceId, java.sql.Date.valueOf(date),
                    basicSalary, allowances, totalSalary
            ));

            if (isSaved) {
                loadTable();
                clearFields();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSalaryUpdateOnAction(ActionEvent event) {
        String id = lblId.getText();
        String name = lblEmployeeName.getText();
        String attendanceId = cmbAttendanceId.getValue();
        LocalDate date = dpDate.getValue();
        double basicSalary = Double.parseDouble(txtBasicSalary.getText());
        double allowances = Double.parseDouble(txtallowances.getText());
        double totalSalary = Double.parseDouble(txtTotalSalary.getText());

        try {
            boolean isUpdated = salaryModel.updateSalary(new SalaryTM(
                    id, name, attendanceId, java.sql.Date.valueOf(date),
                    basicSalary, allowances, totalSalary
            ));

            if (isUpdated) {
                loadTable();
                clearFields();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void cmbAttendanceIdOnAction(ActionEvent event) {
        String attendanceId = cmbAttendanceId.getValue();
        try {
            SalaryTM salaryTM = salaryModel.findById(attendanceId);
            if (salaryTM != null) {
                lblEmployeeName.setText(salaryTM.getName());
                txtBasicSalary.setText(String.valueOf(salaryTM.getBasic_salary()));
                txtallowances.setText(String.valueOf(salaryTM.getAllowances()));
                txtTotalSalary.setText(String.valueOf(salaryTM.getTotal_salary()));
                dpDate.setValue(LocalDate.parse(salaryTM.getSalary_date().toLocaleString()));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void cmbEmployeeIdOnAction(ActionEvent event) {
        String id = cmbEmployeeId.getValue();
        try {
            SalaryTM salaryTM = salaryModel.findById(id);
            if (salaryTM != null) {
                lblEmployeeName.setText(salaryTM.getName());
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void cmbSelectMonthOnAction(ActionEvent event) {
        try {
            loadTable();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        if (event.getClickCount() == 1) {
            SalaryTM selectedItem = (SalaryTM) tblSalary.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                lblId.setText(selectedItem.getSalary_id());
                lblEmployeeName.setText(selectedItem.getName());
                cmbAttendanceId.setValue(selectedItem.getAttendance_id());
                dpDate.setValue(LocalDate.parse(selectedItem.getSalary_date().toLocaleString()));
                txtBasicSalary.setText(String.valueOf(selectedItem.getBasic_salary()));
                txtallowances.setText(String.valueOf(selectedItem.getAllowances()));
                txtTotalSalary.setText(String.valueOf(selectedItem.getTotal_salary()));
            }
        }
    }
    @FXML
    void setTotal(KeyEvent event) {
        if (!txtBasicSalary.getText().isEmpty() && !txtallowances.getText().isEmpty()) {
            double basic = Double.parseDouble(txtBasicSalary.getText());
            double allowances = Double.parseDouble(txtallowances.getText());
            txtTotalSalary.setText(String.valueOf(basic + allowances));
        }
    }

    public void loadTable() {
        try {
            obList.clear();
            ArrayList<SalaryTM> salaryList = salaryModel.getAllSalary();
            obList.addAll(salaryList);
            tblSalary.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellValueFactory();
        loadTable();
        generateNextId();
        setDate();

        try {
            ObservableList<String> months = FXCollections.observableArrayList(
                    "January", "February", "March", "April", "May",
                    "June", "July", "August", "September",
                    "October", "November", "December"
            );

            cmbSelectMonth.setItems(months);

            loadEmployeeIds();
            loadAttendanceIds();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void loadEmployeeIds() throws SQLException, ClassNotFoundException {
        List<String> employeeDTOS = employeeModel.getAllEmployeeIds();
        cmbEmployeeId.setItems(FXCollections.observableArrayList(employeeDTOS));

        cmbEmployeeId.setOnAction(event -> {
            try {
                setEmployeeName();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
    private void loadAttendanceIds() throws SQLException, ClassNotFoundException {
        List<String> attendanceIds = attendanceModel.getAllAttendanceIds();
        cmbAttendanceId.setItems(FXCollections.observableArrayList(attendanceIds));

    }

    private void setEmployeeName() throws SQLException, ClassNotFoundException {
        String employeeId = cmbEmployeeId.getValue();
        String employeeName = employeeModel.findNameById(employeeId);
        lblEmployeeName.setText(employeeName);
    }
    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("salary_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAttendanceId.setCellValueFactory(new PropertyValueFactory<>("attendance_id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("salary_date"));
        colBasic.setCellValueFactory(new PropertyValueFactory<>("basic_salary"));
        colAllowances.setCellValueFactory(new PropertyValueFactory<>("allowances"));
        colTotalSalary.setCellValueFactory(new PropertyValueFactory<>("total_salary"));
    }

    private void generateNextId() {
        try {
            lblId.setText(salaryModel.getNextId());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDate() {
        dpDate.setValue(LocalDate.now());
    }
}
