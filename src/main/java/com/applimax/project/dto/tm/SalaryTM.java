package com.applimax.project.dto.tm;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class SalaryTM {
    private String salary_id;
    private String name;
    private String attendance_id;
    private Date salary_date;
    private double basic_salary;
    private double allowances;
    private double total_salary;
}
