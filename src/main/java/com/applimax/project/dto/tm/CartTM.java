package com.applimax.project.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class CartTM {
    private String itemId;
    private String itemName;
    private Date orderDate;
    private Integer quantity;
    private Double unitPrice;
    private Double totalAmount;
    private Button removeBtn;

    public CartTM(String itemId, String itemName,LocalDate date,int cartQty, double itemUnitPrice, double total, Button removeBtn) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = cartQty;
        this.unitPrice = itemUnitPrice;
        this.totalAmount = total;
        this.removeBtn = removeBtn;
    }
}
