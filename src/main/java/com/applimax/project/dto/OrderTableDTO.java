package com.applimax.project.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderTableDTO {
    private String orderId;
    private String customerId;
    private LocalDate orderDate;
    private Integer Quantity;
    private Double unitPrice;
    private Double totalAmount;
    private ArrayList<OrderDetailDTO> cartList;

    public OrderTableDTO(String orderId, String customerId, LocalDate orderDate, ArrayList<OrderDetailDTO> cartList) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.cartList = cartList;
    }

}
