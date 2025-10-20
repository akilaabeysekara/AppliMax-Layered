package com.applimax.project.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class AllowanceDTO {
    private String allowanceId;
    private String salaryId;
    private Date date;
    private Double amount;
}
