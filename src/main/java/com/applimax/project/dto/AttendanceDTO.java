package com.applimax.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor

public class AttendanceDTO {
    private String attendanceId;
    private String employeeId;
    private Date attendanceDate;
    private Time attendanceInTime;
    private Time attendanceOutTime;
    private String status;
    private int workingHours;
}
