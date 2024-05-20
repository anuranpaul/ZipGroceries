package com.InventoryService.Inventory.controller;

import com.InventoryService.Inventory.constants.InventoryConstants;
import com.InventoryService.Inventory.dto.InventoryDto;
import com.InventoryService.Inventory.dto.InventoryResponseDto;
import com.InventoryService.Inventory.dto.ResponseDto;
import com.InventoryService.Inventory.service.InventoryService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/inventory", produces = {MediaType.APPLICATION_JSON_VALUE})
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class InventoryController {
    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addProducts(@RequestBody List<InventoryDto> inventoryDtos) {
        logger.info("Adding products : ");
        inventoryService.addItems(inventoryDtos);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(InventoryConstants.STATUS_201, InventoryConstants.MESSAGE_201));
    }

    @GetMapping("/view/{productCode}")
    public ResponseEntity<InventoryDto> getProductByProductId(@PathVariable String productCode) {
        InventoryDto inventoryDto = inventoryService.getProductByProductCode(productCode);
        return ResponseEntity.ok(inventoryDto);
    }

    @GetMapping("/view")
    public ResponseEntity<List<InventoryDto>> getAllProducts() {
        logger.info("Fetching all products from inventory");
        List<InventoryDto> products = inventoryService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteProducts(@RequestParam List<String> productCodes) {
        logger.info("Deleting products with product codes: {}", productCodes);
        boolean isDeleted = inventoryService.deleteProduct(productCodes);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(InventoryConstants.STATUS_200, InventoryConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(InventoryConstants.STATUS_417, InventoryConstants.MESSAGE_417_DELETE));
        }
    }

    //    eg: http://localhost:8081/inventory/stock?productCode=ABC123&productCode=XYZ789
    @GetMapping("/stock")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponseDto isInStock(@RequestParam String productCode) {
        logger.info("Checking stock for product code: {}", productCode);
        return inventoryService.isInStock(productCode);
    }
}
