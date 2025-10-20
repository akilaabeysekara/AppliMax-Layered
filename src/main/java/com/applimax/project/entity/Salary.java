package com.applimax.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Salary implements Serializable {
    private String salaryId;
    private String employeeId;
    private String attendanceId;
    private Date salaryDate;
    private double basicSalary;
    private double allowance;
    private double totalSalary;
}