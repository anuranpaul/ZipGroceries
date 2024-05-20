package com.InventoryService.Inventory;

import com.InventoryService.Inventory.dto.InventoryDto;
import com.InventoryService.Inventory.dto.InventoryResponseDto;
import com.InventoryService.Inventory.entity.Inventory;
import com.InventoryService.Inventory.exception.ResourceNotFoundException;
import com.InventoryService.Inventory.repository.InventoryRepository;
import com.InventoryService.Inventory.serviceImpl.InventoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTests {

    @Mock
    private InventoryRepository inventoryRepo;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private Inventory inventory;
    private InventoryDto inventoryDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventory = new Inventory();
        inventory.setProductCode("P12345");
        inventory.setQuantity(10);

        inventoryDto = new InventoryDto();
        inventoryDto.setProductCode("P12345");
        inventoryDto.setQuantity(10);
    }

    @Test
    void testAddItems() {
        when(inventoryRepo.saveAll(anyList())).thenReturn(null); // Use thenReturn(null) for methods returning void
        InventoryDto inventoryDto = new InventoryDto();
        inventoryService.addItems(List.of(inventoryDto));
        verify(inventoryRepo, times(1)).saveAll(anyList());
    }

    @Test
    void testGetProductByProductCode() {
        when(inventoryRepo.findByProductCode(anyString())).thenReturn(Optional.of(inventory));

        InventoryDto fetchedInventory = inventoryService.getProductByProductCode("P12345");

        assertEquals(inventoryDto.getProductCode(), fetchedInventory.getProductCode());
    }

    @Test
    void testGetProductByProductCodeThrowsException() {
        when(inventoryRepo.findByProductCode(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.getProductByProductCode("P67890"));
    }

    @Test
    void testGetAllProducts() {
        when(inventoryRepo.findAll()).thenReturn(List.of(inventory));

        List<InventoryDto> products = inventoryService.getAllProducts();

        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        assertEquals(inventoryDto.getProductCode(), products.get(0).getProductCode());
    }

    @Test
    void testDeleteProduct() {
        when(inventoryRepo.findByProductCodeIn(anyList())).thenReturn(Optional.of(inventory));

        boolean isDeleted = inventoryService.deleteProduct(List.of("P12345"));

        assertTrue(isDeleted);
        verify(inventoryRepo, times(1)).deleteByProductCodeIn(anyList());
    }

    @Test
    void testDeleteProductThrowsException() {
        when(inventoryRepo.findByProductCodeIn(anyList())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.deleteProduct(List.of("P67890")));
    }

    @Test
    void testIsInStock() {
        when(inventoryRepo.findByProductCode(anyString())).thenReturn(Optional.of(inventory));

        InventoryResponseDto responseDto = inventoryService.isInStock("P12345");

        assertTrue(responseDto.isInStock());
        assertEquals("P12345", responseDto.getProductCode());
    }

    @Test
    void testIsInStockThrowsException() {
        when(inventoryRepo.findByProductCode(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.isInStock("P67890"));
    }
}
