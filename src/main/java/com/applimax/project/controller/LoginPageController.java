package com.applimax.project.controller;

import com.applimax.project.model.AppUserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;

public class LoginPageController {

    @FXML
    private AnchorPane ancMainContainer;

    @FXML
    private Label lblSup1;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    void forgotPasswordOnAction(MouseEvent event) {
        navigateTo("/view/ForgotPassword.fxml");

    }

    public void onLogin(ActionEvent actionEvent) throws IOException {
        singIn();
    }

    public void onNext(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            try {
                passwordField.requestFocus();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onLog(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            try {
                singIn();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void singIn(){
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Username and password are required", ButtonType.OK).show();
        }
        try {
            AppUserModel userModel = new AppUserModel();
            boolean isValid = userModel.checkUser(username,password);
            if (isValid) {
                navigateTo("/view/Dashboard.fxml");
            }else {
                new Alert(Alert.AlertType.ERROR, "Invalid credentials!", ButtonType.OK).show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
        }


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