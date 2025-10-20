package com.applimax.project.dto.tm;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SalesTM {
    private String salesId;
    private String itemName;
    private Integer qtyLeftStock;
    private Integer qtySold;
    private Double unitPrice;
    private Double soldAmount;
    private String soldDate;
}