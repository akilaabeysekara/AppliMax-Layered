package com.applimax.project.dto.tm;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class AttendanceTM {
    private String attendanceId;
    private String employeeId;
    private String attendanceDate;
    private String attendanceInTime;
    private String attendanceOutTime;
    private String status;
    private int workingHours;
}
