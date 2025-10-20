package com.applimax.project.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class SupplierPaymentDTO {
    private String expensesId;
    private String supplierId;
    private double payAmount;
    private String paymentMethod;
    private LocalDate paymentDate;
}
