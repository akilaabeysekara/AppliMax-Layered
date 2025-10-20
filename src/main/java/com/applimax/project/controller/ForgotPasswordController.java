package com.applimax.project.controller;

import com.applimax.project.model.ForgotPasswordModel;
import com.applimax.project.util.CrudUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class ForgotPasswordController {

    @FXML
    private AnchorPane ancMainContainer;

    @FXML
    private Button btnRecoverPassword;

    @FXML
    private ImageView iconBack;

    @FXML
    private TextField txtEmail;


    @FXML
    void btnRecoverPasswordOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String email = txtEmail.getText();
        if (email.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter your email address.").show();
            return;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid email address.").show();
            return;
        }

        ForgotPasswordModel model = new ForgotPasswordModel();
        String newPassword = model.generateRandomPassword();

        try {
            ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM app_user WHERE email=?", email);
            resultSet.next();
            boolean exists = resultSet.getInt(1) > 0;
            if (!exists) {
                new Alert(Alert.AlertType.ERROR, "Email address not found in system.").show();
                return;
            }

            String from = "akilaabeysekara99@gmail.com";
            String password = "jhep kzuj eqwf xdui";

            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Your New Password for AppliMax");
            message.setText("As requested, we have generated a new password for your account.\n"+
                    "\n"+
                    "Your new password is: " + newPassword +"\n"+
                            "\n"+
                    "For security reasons, we recommend that you log in immediately\n " +
                    "If you did not request this password reset or believe this was done in error,\n " +
                    "please contact our support team right away.\n"+
                            "\n"+
                    "Thank you,\n" +
                    "AppliMax Support Team");

            Transport.send(message);

            model.updatePassword(email, newPassword);

            navigateTo("/view/LoginPage.fxml");
            new Alert(Alert.AlertType.INFORMATION, "Password reset instructions sent to your email.").show();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to send email: " + e.getMessage()).show();
        }
    }

    @FXML
    void onClickedBack(MouseEvent event) {
        navigateTo("/view/LoginPage.fxml");
    }

    @FXML
    void onNext(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            btnRecoverPassword.fire();
        }
    }

    void sendEmail(ActionEvent event) {

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