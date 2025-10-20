package com.applimax.project.controller;

import com.applimax.project.dto.AttendanceDTO;
import com.applimax.project.dto.tm.AttendanceTM;
import com.applimax.project.model.AttendanceModel;
import com.applimax.project.model.EmployeeModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeAttendancePageController implements Initializable {

    public ComboBox<String> cmbInHours;
    public ComboBox<String> cmbInMinutes;
    public Label lblInTime;
    public ComboBox<String> cmbOutHours;
    public ComboBox<String> cmbOutMinutes;
    public Label lblOutTime;
    @FXML
    private ComboBox<String> cmbSelectMonth;
    @FXML
    private ComboBox<String> cmbStatus;
    @FXML
    private Button btnDeleteAttendance;

    @FXML
    private Button btnMarkAttendance;

    @FXML
    private Button btnReportAttendance;

    @FXML
    private Button btnResetAttendance;

    @FXML
    private Button btnUpdateAttendance;

    @FXML
    private ComboBox<String> cmbEmployeeId;

    @FXML
    private TableColumn<AttendanceTM, String> colDate;

    @FXML
    private TableColumn<AttendanceTM, String> colId;

    @FXML
    private TableColumn<AttendanceTM, String> colInTime;

    @FXML
    private TableColumn<AttendanceTM, String> colName;

    @FXML
    private TableColumn<AttendanceTM, String> colOutTime;

    @FXML
    private TableColumn<AttendanceTM, String> colStatus;

    @FXML
    private TableColumn<AttendanceTM, Integer> colWorkHours;

    @FXML
    private DatePicker dpDate;

    @FXML
    private Label lblEmployeeName;

    @FXML
    private Label lblId;

    @FXML
    private TableView<AttendanceTM> tblAttendance;

    @FXML
    private TextField txtInTime;

    @FXML
    private TextField txtOutTime;

    @FXML
    private TextField txtWorkHours;

    private final AttendanceModel attendanceModel = new AttendanceModel();
    private final EmployeeModel employeeModel = new EmployeeModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("attendanceId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("attendanceDate"));
        colInTime.setCellValueFactory(new PropertyValueFactory<>("attendanceInTime"));
        colOutTime.setCellValueFactory(new PropertyValueFactory<>("attendanceOutTime"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colWorkHours.setCellValueFactory(new PropertyValueFactory<>("workingHours"));

        for (int i = 0; i <= 24; i++) {
            cmbInHours.getItems().add(String.format("%02d", i));
            cmbOutHours.getItems().add(String.format("%02d", i));
        }
        for (int i = 0; i < 60; i++) {
            cmbInMinutes.getItems().add(String.format("%02d", i));
            cmbOutMinutes.getItems().add(String.format("%02d", i));
        }
        ObservableList<String> months = FXCollections.observableArrayList(
                "January", "February", "March", "April", "May",
                "June", "July", "August", "September",
                "October", "November", "December"
        );

        cmbSelectMonth.setItems(months);
        try {
            loadEmployeeIds();
            dpDate.setValue(LocalDate.now());
            cmbStatus.setItems((FXCollections.observableArrayList("Present", "Absent", "Half Day", "Short Leave")));
            resetPage();
        } catch (Exception e) {
           // new Alert(Alert.AlertType.ERROR, "Failed to load attendance data").show();
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

    private void setEmployeeName() throws SQLException, ClassNotFoundException {
        String employeeId = cmbEmployeeId.getValue();
        String employeeName = employeeModel.findNameById(employeeId);
        lblEmployeeName.setText(employeeName);
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
        loadTableData();
        clearFields();

        btnDeleteAttendance.setDisable(true);
        btnUpdateAttendance.setDisable(true);
        btnMarkAttendance.setDisable(false);
    }

    private void clearFields() {
        dpDate.setValue(LocalDate.now());
        cmbStatus.setValue(null);
        txtWorkHours.clear();
        txtInTime.clear();
        txtOutTime.clear();
        cmbEmployeeId.setValue(null);
        lblEmployeeName.setText("");
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(attendanceModel.getNextId());
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        tblAttendance.setItems(FXCollections.observableArrayList(attendanceModel.getAllAttendance()));
    }

    @FXML
    void btnDeleteAttendanceOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this attendance record?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = attendanceModel.deleteAttendance(lblId.getText());
                if (isDeleted) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Attendance deleted successfully").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error deleting attendance").show();
            }
        }
    }

    @FXML
    void btnMarkAttendanceOnAction(ActionEvent event) {
        try {
            AttendanceDTO dto = new AttendanceDTO(
                    lblId.getText(),
                    cmbEmployeeId.getValue(),
                    java.sql.Date.valueOf(dpDate.getValue()),
                    Time.valueOf(txtInTime.getText()),
                    Time.valueOf(txtOutTime.getText()),
                    cmbStatus.getValue(),
                    Integer.parseInt(txtWorkHours.getText())
            );

            if (attendanceModel.saveAttendance(dto)) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Attendance marked successfully").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error marking attendance").show();
        }
    }

    @FXML
    void btnReportAttendanceOnAction(ActionEvent event) {
    }

    @FXML
    void btnResetAttendanceOnAction(ActionEvent event) {
        try {
            resetPage();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error resetting page").show();
        }
    }

    @FXML
    void btnUpdateAttendanceOnAction(ActionEvent event) {
        try {
            AttendanceDTO dto = new AttendanceDTO(
                    lblId.getText(),
                    cmbEmployeeId.getValue(),
                    java.sql.Date.valueOf(dpDate.getValue()),
                    Time.valueOf(txtInTime.getText()),
                    Time.valueOf(txtOutTime.getText()),
                    cmbStatus.getValue(),
                    Integer.parseInt(txtWorkHours.getText())
            );

            if (attendanceModel.updateAttendance(dto)) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Attendance updated successfully").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error updating attendance").show();
        }
    }

    @FXML
    void cmbEmployeeOnAction(ActionEvent event) {
        cmbEmployeeId.getItems().clear();
        try {
            ArrayList<String> employeeIds = attendanceModel.getAllAttendanceIds();
            cmbEmployeeId.getItems().addAll(employeeIds);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading employee IDs").show();
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        AttendanceTM selected = tblAttendance.getSelectionModel().getSelectedItem();

        if (selected != null) {
            lblId.setText(selected.getAttendanceId());
            cmbEmployeeId.setValue(selected.getEmployeeId());
            dpDate.setValue(LocalDate.parse(selected.getAttendanceDate()));
            txtInTime.setText(selected.getAttendanceInTime());
            txtOutTime.setText(selected.getAttendanceOutTime());
            cmbStatus.setValue(selected.getStatus());
            txtWorkHours.setText(String.valueOf(selected.getWorkingHours()));

            btnMarkAttendance.setDisable(true);
            btnUpdateAttendance.setDisable(false);
            btnDeleteAttendance.setDisable(false);
        }
    }

    @FXML
    void setTotal(KeyEvent event) {
        try {
            String inTime = txtInTime.getText();
            String outTime = txtOutTime.getText();

            if (inTime != null && !inTime.isEmpty() && outTime != null && !outTime.isEmpty()) {
                Time in = Time.valueOf(inTime);
                Time out = Time.valueOf(outTime);

                long diff = out.getTime() - in.getTime();
                int hours = (int) (diff / (60 * 60 * 1000));

                txtWorkHours.setText(String.valueOf(hours));
            }
        } catch (Exception e) {
        }
    }

    public void cmbInHoursOnAction(ActionEvent actionEvent) {
        cmbInMinutes.requestFocus();

    }

    public void cmbInMinutesOnAction(ActionEvent actionEvent) {
        String hours = cmbInHours.getValue();
        String minutes = cmbInMinutes.getValue();
        lblInTime.setText(hours + ":" + minutes);
    }

    public void cmbOutHoursOnAction(ActionEvent actionEvent) {
        cmbOutMinutes.requestFocus();
    }

    public void cmbOutMinutesOnAction(ActionEvent actionEvent) {
        String hours = cmbOutHours.getValue();
        String minutes = cmbOutMinutes.getValue();
        lblOutTime.setText(hours + ":" + minutes);
    }

    @FXML
    void cmbSelectMonthOnAction(ActionEvent event) {


    }
}