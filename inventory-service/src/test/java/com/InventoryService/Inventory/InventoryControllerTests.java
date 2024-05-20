package com.InventoryService.Inventory;

import com.InventoryService.Inventory.constants.InventoryConstants;
import com.InventoryService.Inventory.controller.InventoryController;
import com.InventoryService.Inventory.dto.InventoryDto;
import com.InventoryService.Inventory.dto.InventoryResponseDto;
import com.InventoryService.Inventory.dto.ResponseDto;
import com.InventoryService.Inventory.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class InventoryControllerTests {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProducts() {
        doNothing().when(inventoryService).addItems(anyList());

        List<InventoryDto> inventoryDtos = List.of(new InventoryDto());
        ResponseEntity<ResponseDto> response = inventoryController.addProducts(inventoryDtos);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(InventoryConstants.STATUS_201, response.getBody().getStatusCode());
        verify(inventoryService, times(1)).addItems(anyList());
    }

    @Test
    void testGetProductByProductId() {
        InventoryDto inventoryDto = new InventoryDto();
        when(inventoryService.getProductByProductCode(anyString())).thenReturn(inventoryDto);

        ResponseEntity<InventoryDto> response = inventoryController.getProductByProductId("ABC123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inventoryDto, response.getBody());
        verify(inventoryService, times(1)).getProductByProductCode("ABC123");
    }

    @Test
    void testGetAllProducts() {
        List<InventoryDto> inventoryDtos = List.of(new InventoryDto());
        when(inventoryService.getAllProducts()).thenReturn(inventoryDtos);

        ResponseEntity<List<InventoryDto>> response = inventoryController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inventoryDtos, response.getBody());
        verify(inventoryService, times(1)).getAllProducts();
    }

    @Test
    void testDeleteProducts() {
        when(inventoryService.deleteProduct(anyList())).thenReturn(true);

        ResponseEntity<ResponseDto> response = inventoryController.deleteProducts(List.of("ABC123", "XYZ789"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(InventoryConstants.STATUS_200, response.getBody().getStatusCode());
        verify(inventoryService, times(1)).deleteProduct(anyList());
    }

    @Test
    void testDeleteProducts_Failure() {
        when(inventoryService.deleteProduct(anyList())).thenReturn(false);

        ResponseEntity<ResponseDto> response = inventoryController.deleteProducts(List.of("ABC123", "XYZ789"));

        assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
        assertEquals(InventoryConstants.STATUS_417, response.getBody().getStatusCode());
        verify(inventoryService, times(1)).deleteProduct(anyList());
    }

    @Test
    void testIsInStock() {
        InventoryResponseDto inventoryResponseDto = new InventoryResponseDto();
        when(inventoryService.isInStock(anyString())).thenReturn(inventoryResponseDto);

        InventoryResponseDto response = inventoryController.isInStock("ABC123");

        assertEquals(inventoryResponseDto, response);
        verify(inventoryService, times(1)).isInStock("ABC123");
    }
}
