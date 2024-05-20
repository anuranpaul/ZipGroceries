package com.Service.OrderingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponse {
    private String productCode;
    private String productName;
    private int quantity;
    private double price;
}
