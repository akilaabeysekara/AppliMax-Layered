package com.applimax.project.model;

import com.applimax.project.util.CrudUtil;

import java.sql.SQLException;
import java.util.Random;

public class ForgotPasswordModel {

    public boolean updatePassword(String email, String newPassword) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE app_user SET password=? WHERE email=?",
                newPassword,
                email
        );
    }

    public String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}