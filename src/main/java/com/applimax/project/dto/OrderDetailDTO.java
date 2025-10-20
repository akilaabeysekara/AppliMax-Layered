package com.applimax.project.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class OrderDetailDTO {
    private String orderId;
    private String itemId;
    private int quantity;
    private double unitPrice;
}
