package com.applimax.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Supplier implements Serializable {
    private String supplierId;
    private String name;
    private String nic;
    private String phoneNo;
    private String email;
    private String address;
}