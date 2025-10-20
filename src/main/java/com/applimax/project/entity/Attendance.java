package com.applimax.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Attendance implements Serializable {
    private String attendanceId;
    private String employeeId;
    private Date attendanceDate;
    private Time attendanceInTime;
    private Time attendanceOutTime;
    private String status;
    private int workingHours;
}