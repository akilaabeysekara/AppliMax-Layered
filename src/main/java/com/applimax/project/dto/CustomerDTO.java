package com.applimax.project.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class CustomerDTO {
    private String customerId;
    private String customerName;
    private String customerNic;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;


}
