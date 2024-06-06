package com.Service.OrderingService.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private String cartId;
    private List<CartItemDto> items;

    @Data
    @AllArgsConstructor @NoArgsConstructor// Includes getters and setters for CartDto fields
    public static class CartItemDto {
        private String productCode;
        private String productName;
        private Double price;
        private Integer quantity; // Changed type to Integer
    }

    private Double subTotal;
}

