package com.applimax.project.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class SupplierDTO {
    private String supplierId;
    private String name;
    private String nic;
    private String phoneNo;
    private String email;
    private String address;
}