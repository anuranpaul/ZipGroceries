package com.InventoryService.Inventory.serviceImpl;

import com.InventoryService.Inventory.dto.InventoryDto;
import com.InventoryService.Inventory.dto.InventoryResponseDto;
import com.InventoryService.Inventory.entity.Inventory;
import com.InventoryService.Inventory.exception.ResourceNotFoundException;
import com.InventoryService.Inventory.mapper.InventoryMapper;
import com.InventoryService.Inventory.repository.InventoryRepository;
import com.InventoryService.Inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {
    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    InventoryRepository inventoryRepo;

    /**
     * @param items - list of products to be added
     */
    @Override
    public void addItems(List<InventoryDto> items) {
        logger.info("Adding products: {}", items);
        List<Inventory> inventoryItems = items.stream()
                .map(InventoryMapper::mapToInventory)
                .collect(Collectors.toList());
        inventoryRepo.saveAll(inventoryItems);
    }

    /**
     * @param productCode
     * @return
     */
    @Override
    public InventoryDto getProductByProductCode(String productCode) {
        //return inventoryRepo.findByProductCode(productCode);
        Optional<Inventory> inventoryOptional = inventoryRepo.findByProductCode(productCode);
        Inventory inventory = inventoryOptional.orElseThrow(
                () -> new ResourceNotFoundException("Product", "productCode", productCode));
        return InventoryMapper.mapToInventoryDto(inventory);

    }

    /**
     * @return - show all the products that are in the inventory
     */
    @Override
    public List<InventoryDto> getAllProducts() {
        logger.info("Fetching all products from inventory");
        List<Inventory> inventoryItems = inventoryRepo.findAll();
        return convertToDtoList(inventoryItems);
    }

    /**
     * @param productCodes - list of all the product codes that are to be removed from the inventory
     * @return - whether it has been deleted or not
     */
    @Override
    public boolean deleteProduct(List<String> productCodes) {
        Optional<Inventory> inventories = inventoryRepo.findByProductCodeIn(productCodes);
        if (inventories.isEmpty()) {
            logger.warn("No products found with the given product codes: {}", productCodes);
            throw new ResourceNotFoundException("Products", "productCodes", String.join(",", productCodes));
        }

        inventoryRepo.deleteByProductCodeIn(productCodes);
        logger.info("Deleting products with product codes: {}", productCodes);
        return true;
    }

    /**
     * @param productCodes - list of all the items to check whether they're in stock or not
     * @return - returns whether they're in stock or not
     */
    @Override
    public InventoryResponseDto isInStock(String productCode) {
        logger.info("Checking stock for product code: {}", productCode);
        Optional<Inventory> inventoryList = inventoryRepo.findByProductCode(productCode);

        if (inventoryList.isEmpty()) {
            logger.error("Product not found in inventory: {}", productCode);
            throw new ResourceNotFoundException("Product", "productCode", productCode);
        }
        boolean isInStock = inventoryList.get().getQuantity() > 0;
        // Create an InventoryResponseDto for the current productCode
        return InventoryResponseDto.builder()
                .productCode(productCode)
                .isInStock(isInStock)
                .build();
    }

    /**
     *
     * @param inventoryItems
     * @return converts to the DTO object which is used to send as reponse to the HTTP request
     */
    private List<InventoryDto> convertToDtoList(List<Inventory> inventoryItems) {
        return inventoryItems.stream()
                .map(InventoryMapper::mapToInventoryDto)
                .collect(Collectors.toList());
    }

}
