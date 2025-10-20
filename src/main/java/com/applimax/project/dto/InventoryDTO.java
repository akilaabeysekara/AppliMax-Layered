package com.applimax.project.dto;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {
    private String inventoryId;
    private String itemName;
    private String supplierName;
    private Double unitPrice;
    private Integer quantity;
    private LocalDateTime receivedDate;

    public InventoryDTO(String inventoryId, String itemName, String supplierName, LocalDateTime receivedDate) {
        this.inventoryId = inventoryId;
        this.itemName = itemName;
        this.supplierName = supplierName;
        this.receivedDate = receivedDate;
    }
}