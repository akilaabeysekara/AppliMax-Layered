package com.applimax.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer implements Serializable {
    private String customerId;
    private String customerName;
    private String customerNic;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;
}