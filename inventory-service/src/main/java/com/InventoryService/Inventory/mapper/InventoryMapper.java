package com.InventoryService.Inventory.mapper;

import com.InventoryService.Inventory.dto.InventoryDto;
import com.InventoryService.Inventory.entity.Inventory;

public class InventoryMapper {
    public static InventoryDto mapToInventoryDto(Inventory inventory) {
        //userDto.setId(user.getId());
        InventoryDto inventoryDto= new InventoryDto();
        //inventoryDto.setId(inventory.getId());
        inventoryDto.setProductCode(inventory.getProductCode());
        inventoryDto.setProductName(inventory.getProductName());
        inventoryDto.setQuantity(inventory.getQuantity());
        inventoryDto.setPrice(inventory.getPrice());
        return inventoryDto;
    }

    public static Inventory mapToInventory(InventoryDto inventoryDto) {
        Inventory inventory = new Inventory();
        //inventory.setId(inventoryDto.getId());
        inventory.setProductCode(inventoryDto.getProductCode());
        inventory.setProductName(inventoryDto.getProductName());
        inventory.setQuantity(inventoryDto.getQuantity());
        inventory.setPrice(inventoryDto.getPrice());
        return inventory;
    }
}
