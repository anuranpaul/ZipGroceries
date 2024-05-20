package com.InventoryService.Inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDto {

    //private String id;
    private String productCode;
    private String productName;
    private int quantity;
    private double price;
}
