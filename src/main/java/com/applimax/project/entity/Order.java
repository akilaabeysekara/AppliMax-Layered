package com.applimax.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order implements Serializable {
    private String orderId;
    private String customerId;
    private LocalDate orderDate;
    private Integer Quantity;
    private Double unitPrice;
    private Double totalAmount;
    private ArrayList<OrderDetail> cartList;
}