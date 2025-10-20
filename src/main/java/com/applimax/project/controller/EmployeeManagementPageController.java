package com.applimax.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;

import java.io.IOException;
import java.util.ResourceBundle;


public class EmployeeManagementPageController {

    @FXML
    private AnchorPane ancMainContainer;

    @FXML
    private Button btnEmployeeAttendance;

    @FXML
    private Button btnEmployeeRegistration;

    @FXML
    private Button btnEmployeeSalary;

    @FXML
    void btnEmployeeAttendanceOnAction(ActionEvent event) {
        navigateTo("/view/EmployeeAttendancePage.fxml");

    }

    @FXML
    void btnEmployeeRegistrationOnAction(ActionEvent event) {
        navigateTo("/view/EmployeeRegistrationPage.fxml");

    }

    @FXML
    void btnEmployeeSalaryOnAction(ActionEvent event) {
        navigateTo("/view/EmployeeSalaryPage.fxml");

    }

    private void navigateTo(String path) {
        try {
            ancMainContainer.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
            anchorPane.prefWidthProperty().bind(ancMainContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancMainContainer.heightProperty());
            ancMainContainer.getChildren().add(anchorPane);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

}

