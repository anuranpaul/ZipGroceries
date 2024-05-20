package com.InventoryService.Inventory.service;

import com.InventoryService.Inventory.dto.InventoryDto;
import com.InventoryService.Inventory.dto.InventoryResponseDto;
import com.InventoryService.Inventory.entity.Inventory;

import java.util.List;
import java.util.Optional;

//@FeignClient
public interface InventoryService {
    /**
     * @param items - list of products to be added
     */
    void addItems(List<InventoryDto> items);

    /**
     * @param productCode
     * @return
     */
    InventoryDto getProductByProductCode(String productCode);

    /**
     * @return - show all the products that are in the inventory
     */
    List<InventoryDto> getAllProducts();

    /**
     * @param productCodes - list of all the product codes that are to be removed from the inventory
     * @return - whether it has been deleted or not
     */
    boolean deleteProduct(List<String> productCodes);

    /**
     * @param productCode - list of all the items to check whether they're in stock or not
     * @return - returns whether they're in stock or not
     */
    InventoryResponseDto isInStock(String productCode);

}
