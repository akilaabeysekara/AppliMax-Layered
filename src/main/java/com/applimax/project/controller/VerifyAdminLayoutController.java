package com.applimax.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class VerifyAdminLayoutController {

    public AnchorPane ancVerifyAdmin;
    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    private static final String ADMIN_PASSWORD = "akila123";

    @FXML
    void onLog(KeyEvent event) {
        switch (event.getCode()) {
            case ENTER -> onLogin(null);
        }
    }

    @FXML
    void onLogin(ActionEvent event) {
        String enteredPassword = passwordField.getText();

        if (enteredPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Failed", "Password cannot be empty");
            return;
        }

        if (enteredPassword.equals(ADMIN_PASSWORD)) {
            navigateTo("/view/UserPage.fxml");
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect password. Please try again.");
            passwordField.clear();
        }
    }

    private void navigateTo(String path) {
        try {
            ancVerifyAdmin.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
            anchorPane.prefWidthProperty().bind(ancVerifyAdmin.widthProperty());
            anchorPane.prefHeightProperty().bind(ancVerifyAdmin.heightProperty());
            ancVerifyAdmin.getChildren().add(anchorPane);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}