package com.applimax.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppUser implements Serializable {
    private String userId;
    private String employeeId;
    private String userName;
    private String password;
    private String email;
}