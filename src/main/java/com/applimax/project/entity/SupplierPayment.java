package com.applimax.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SupplierPayment implements Serializable {
    private String expensesId;
    private String supplierId;
    private double payAmount;
    private String paymentMethod;
    private LocalDate paymentDate;
}