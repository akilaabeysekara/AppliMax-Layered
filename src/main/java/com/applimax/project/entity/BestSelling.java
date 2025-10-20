package com.applimax.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BestSelling implements Serializable {
    private String name;
    private String category;
    private String brand;
    private String price;
    private String quantity;
}