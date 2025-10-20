package com.applimax.project.dto.tm;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemTM {
    private String itemId;
    private String itemName;
    private String itemCategory;
    private String itemPrice;
}
