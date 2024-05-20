package com.InventoryService.Inventory.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "inventory_items")
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

    @Id
    private String id;
    private String productCode;
    private String productName;
    private int quantity;
    private double price;
}

