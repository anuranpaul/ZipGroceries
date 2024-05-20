package com.Service.OrderingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long productId; // Use product ID instead of entire product object
    private String productName;
    private int quantity;
    private double subTotal;
}

