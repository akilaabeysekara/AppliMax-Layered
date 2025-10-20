package com.applimax.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Item implements Serializable {
    private String itemId;
    private String itemName;
    private String itemCategory;
    private int qtyOnHand;
    private String itemBrand;
    private String itemPrice;
}