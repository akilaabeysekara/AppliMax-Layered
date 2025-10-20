package com.applimax.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetail implements Serializable {
    private String orderId;
    private String itemId;
    private int quantity;
    private double unitPrice;
}