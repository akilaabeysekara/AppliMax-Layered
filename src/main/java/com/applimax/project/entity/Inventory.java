package com.applimax.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Inventory implements Serializable {
    private String inventoryId;
    private String itemName;
    private String supplierName;
    private Double unitPrice;
    private Integer quantity;
    private LocalDateTime receivedDate;
}