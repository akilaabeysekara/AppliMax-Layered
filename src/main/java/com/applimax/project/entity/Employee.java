package com.applimax.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Employee implements Serializable {
    private String employeeId;
    private String name;
    private String nic;
    private String email;
    private String phoneNo;
    private String address;
    private String role;
}