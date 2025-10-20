package com.applimax.project.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class ItemDTO {
    private String itemId;
    private String itemName;
    private String itemCategory;
    private int qtyOnHand;
    private String itemBrand;
    private String itemPrice;
}
