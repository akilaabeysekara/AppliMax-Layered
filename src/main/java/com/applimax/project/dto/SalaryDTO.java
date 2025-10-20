package com.applimax.project.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SalaryDTO {
    private String salary_id;
    private String employee_id;
    private String attendance_id;
    private Date salary_date;
    private double basic_salary;
    private double allowance;
    private double total_salary;

}
