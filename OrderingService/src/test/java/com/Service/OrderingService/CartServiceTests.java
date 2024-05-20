package com.Service.OrderingService;

import com.Service.OrderingService.ServiceImpl.CartServiceImpl;
import com.Service.OrderingService.dto.CartDto;
import com.Service.OrderingService.dto.InventoryResponse;
import com.Service.OrderingService.dto.ProductResponse;
import com.Service.OrderingService.entity.Item;
import com.Service.OrderingService.entity.Product;
import com.Service.OrderingService.exceptions.ResourceNotFoundException;
import com.Service.OrderingService.feignClient.InventoryClient;
import com.Service.OrderingService.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTests {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private InventoryClient inventoryClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addToCart_Success() {
        String cartId = "123";
        String productCode = "P123";
        Integer quantity = 2;

        ProductResponse productResponse = new ProductResponse("P123", "Product1", 100, 10);
        when(inventoryClient.getProductByProductId(productCode)).thenReturn(productResponse);
        when(inventoryClient.inStock(productCode)).thenReturn(new InventoryResponse(productCode,true));
        when(cartRepository.save(any(Item.class))).thenReturn(new Item());

        cartService.addToCart(cartId, productCode, quantity);

        verify(cartRepository, times(1)).save(any(Item.class));
    }

    @Test
    void addToCart_ProductNotInStock() {
        String cartId = "123";
        String productCode = "P123";
        Integer quantity = 2;
        assertThrows(ResourceNotFoundException.class, () -> cartService.addToCart(cartId, productCode, quantity));
        verify(cartRepository, never()).save(any(Item.class));
    }

    @Test
    void changeItemQuantity_Success() {
        String cartId = "123";
        String productCode = "P123";
        Integer quantity = 5;

        Product product = new Product(null, null, productCode, "Product1", 100.0, 10, null);
        Item item = new Item(quantity, product, 200.0);
        item.setCartId(cartId);
        List<Item> items = Arrays.asList(item);

        when(cartRepository.findByCartId(cartId)).thenReturn(items);

        cartService.changeItemQuantity(cartId, productCode, quantity);

        verify(cartRepository, times(1)).save(any(Item.class));
    }

    @Test
    void changeItemQuantity_CartIsEmpty() {
        String cartId = "123";
        String productCode = "P123";
        Integer quantity = 5;

        when(cartRepository.findByCartId(cartId)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> cartService.changeItemQuantity(cartId, productCode, quantity));

        verify(cartRepository, never()).save(any(Item.class));
    }

    @Test
    void removeItemFromCart_Success() {
        String cartId = "123";
        String productCode = "P123";

        Product product = new Product(null, null, productCode, "Product1", 100.0, 10, null);
        Item item = new Item(2, product, 200.0);
        item.setCartId(cartId);
        List<Item> items = Arrays.asList(item);

        when(cartRepository.findByCartId(cartId)).thenReturn(items);

        cartService.removeItemFromCart(cartId, productCode);

        verify(cartRepository, times(1)).deleteByCartIdAndId(cartId, item.getId());
    }

    @Test
    void removeItemFromCart_CartIsEmpty() {
        String cartId = "123";
        String productCode = "P123";

        when(cartRepository.findByCartId(cartId)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> cartService.removeItemFromCart(cartId, productCode));

        verify(cartRepository, never()).deleteByCartIdAndId(anyString(), anyLong());
    }

    @Test
    void getAllItemsFromCart_Success() {
        String cartId = "123";

        Product product = new Product(null, null, "P123", "Product1", 100.0, 10, null);
        Item item = new Item(2, product, 200.0);
        item.setCartId(cartId);
        List<Item> items = Arrays.asList(item);

        when(cartRepository.findByCartId(cartId)).thenReturn(items);

        CartDto cartDto = cartService.getAllItemsFromCart(cartId);

        assertNotNull(cartDto);
        assertEquals(cartId, cartDto.getCartId());
        assertEquals(1, cartDto.getItems().size());
    }

    @Test
    void getAllItemsFromCart_CartIsEmpty() {
        String cartId = "123";

        when(cartRepository.findByCartId(cartId)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> cartService.getAllItemsFromCart(cartId));
    }

    @Test
    void deleteCart_Success() {
        String cartId = "123";

        cartService.deleteCart(cartId);

        verify(cartRepository, times(1)).deleteAll();
    }
}
