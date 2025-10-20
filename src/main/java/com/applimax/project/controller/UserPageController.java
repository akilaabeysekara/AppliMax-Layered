package com.applimax.project.controller;

import com.applimax.project.dto.AppUserDTO;
import com.applimax.project.model.AppUserModel;
import com.applimax.project.model.EmployeeModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserPageController implements Initializable {

    @FXML
    private  TableColumn<AppUserDTO, String> colPassword;
    @FXML
    private  TableColumn<AppUserDTO, String> colEmployeeId;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbEmployeeId;

    @FXML
    private TableColumn<AppUserDTO, String> colEmail;

    @FXML
    private TableColumn<AppUserDTO, String> colId;

    @FXML
    private TableColumn<AppUserDTO, String> colName;

    @FXML
    private Label lblEmployeeName;

    @FXML
    private Label lblId;

    @FXML
    private Label lblUserEmail;

    @FXML
    private Label lblUserRole;

    @FXML
    private TableView<AppUserDTO> tblUser;

    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtUserPassword;

    private final AppUserModel appUserModel = new AppUserModel();
    private final EmployeeModel employeeModel = new EmployeeModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        try {
            loadEmployeeIds();
            setEmployeeName();
            resetPage();
        } catch (Exception e) {
            showError("Failed to load user data.");
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
        String employeeId=cmbEmployeeId.getValue();
        String employeeName=employeeModel.findNameById(employeeId);
        lblEmployeeName.setText(employeeName);
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
        txtUserName.clear();
        txtUserPassword.clear();
        cmbEmployeeId.setValue(null);
        lblEmployeeName.setText("");
        lblUserEmail.setText("");
        lblUserRole.setText("");
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(appUserModel.getNextId());
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        tblUser.setItems(FXCollections.observableArrayList(appUserModel.getAllUsers()));
    }

    @FXML
    void btnUserDeleteOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this user?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = appUserModel.deleteUser(lblId.getText());
                if (isDeleted) {
                    resetPage();
                    showInfo("User deleted successfully.");
                } else {
                    showError("Cannot delete user. It may be referenced in other records.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error occurred while deleting user.");
            }
        }
    }

    @FXML
    void btnUserResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        resetPage();
    }

    @FXML
    void btnUserSaveOnAction(ActionEvent event) {
        String userId = lblId.getText();
        String userName = txtUserName.getText();
        String userPassword = txtUserPassword.getText();
        String employeeId = cmbEmployeeId.getValue();
        String userEmail = lblUserEmail.getText();

        if (userId.isEmpty() || userName.isEmpty() || userPassword.isEmpty() || employeeId == null) {
            showError("Please fill in all required fields.");
            return;
        }

        AppUserDTO dto = new AppUserDTO(userId, employeeId, userName, userPassword,  userEmail);

        try {
            boolean isSaved = appUserModel.saveUser(dto);
            if (isSaved) {
                resetPage();
                showInfo("User saved successfully!");
            } else {
                showError("Failed to save user.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error occurred while saving user.");
        }
    }

    @FXML
    void btnUserUpdateOnAction(ActionEvent event) {
        String userId = lblId.getText();
        String userName = txtUserName.getText();
        String userPassword = txtUserPassword.getText();
        String employeeId = cmbEmployeeId.getValue();
        String userEmail = lblUserEmail.getText();
        String userRole = lblUserRole.getText();

        AppUserDTO dto = new AppUserDTO(userId, userName, userPassword, employeeId, userEmail);

        try {
            boolean isUpdated = appUserModel.updateUser(dto);
            if (isUpdated) {
                resetPage();
                showInfo("User updated successfully!");
            } else {
                showError("Failed to update user.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error occurred while updating user.");
        }
    }

    @FXML
    void cmbEmployeeOnAction(ActionEvent event) {

    }

    @FXML
    void onClickTable(MouseEvent event) {
        AppUserDTO selected = tblUser.getSelectionModel().getSelectedItem();

        if (selected != null) {
            lblId.setText(selected.getUserId());
            txtUserName.setText(selected.getUserName());
            txtUserPassword.setText(selected.getPassword());
            cmbEmployeeId.setValue(selected.getEmployeeId());
            lblUserEmail.setText(selected.getEmail());
            lblUserRole.setText(selected.getUserId());

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    @FXML
    void setTotal(KeyEvent event) {

    }

    private void showInfo(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).show();
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }

}