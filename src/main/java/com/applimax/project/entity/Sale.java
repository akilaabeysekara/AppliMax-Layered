package com.applimax.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale implements Serializable {

    private String orderId;
    private String orderDate;
    private Item item;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
}