package com.Service.OrderingService;

import com.Service.OrderingService.Service.CartService;
import com.Service.OrderingService.constants.OrderConstants;
import com.Service.OrderingService.controller.CartController;
import com.Service.OrderingService.dto.CartDto;
import com.Service.OrderingService.dto.ResponseDto;
import com.Service.OrderingService.entity.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CartControllerTests {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void viewCart_CartExists() {
        String cartId = "CART123";
        CartDto cartDto = new CartDto();

        when(cartService.getAllItemsFromCart(cartId)).thenReturn(cartDto);

        ResponseEntity<CartDto> response = cartController.viewCart(cartId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartDto, response.getBody());
    }

    @Test
    void viewCart_CartNotFound() {
        String cartId = "CART123";

        when(cartService.getAllItemsFromCart(cartId)).thenReturn(null);

        ResponseEntity<CartDto> response = cartController.viewCart(cartId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void addItemToCart_CartExists() {
        String cartId = "CART123";
        String productCode = "PROD001";
        int quantity = 1;
        List<Item> cart = List.of(new Item());

        when(cartService.getItems(cartId)).thenReturn(cart);

        ResponseEntity<ResponseDto> response = cartController.addItemToCart(productCode, quantity, cartId);

        verify(cartService).addToCart(cartId, productCode, quantity);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(OrderConstants.STATUS_202, response.getBody().getStatusCode());
        assertEquals(OrderConstants.MESSAGE_202, response.getBody().getStatusMsg());
    }

    @Test
    void addItemToCart_CartNotFound() {
        String cartId = "CART123";
        String productCode = "PROD001";
        int quantity = 1;

        when(cartService.getItems(cartId)).thenReturn(null);

        ResponseEntity<ResponseDto> response = cartController.addItemToCart(productCode, quantity, cartId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(OrderConstants.STATUS_500, response.getBody().getStatusCode());
        assertEquals(OrderConstants.MESSAGE_500, response.getBody().getStatusMsg());
    }

    @Test
    void modifyItemsInCart_CartExists() {
        String cartId = "CART123";
        String productCode = "PROD001";
        int quantity = 2;
        List<Item> cart = List.of(new Item());

        when(cartService.getItems(cartId)).thenReturn(cart);

        ResponseEntity<ResponseDto> response = cartController.modifyItemsInCart(productCode, quantity, cartId);

        verify(cartService).changeItemQuantity(cartId, productCode, quantity);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(OrderConstants.STATUS_203, response.getBody().getStatusCode());
        assertEquals(OrderConstants.MESSAGE_203, response.getBody().getStatusMsg());
    }

    @Test
    void modifyItemsInCart_CartNotFound() {
        String cartId = "CART123";
        String productCode = "PROD001";
        int quantity = 2;

        when(cartService.getItems(cartId)).thenReturn(List.of());

        ResponseEntity<ResponseDto> response = cartController.modifyItemsInCart(productCode, quantity, cartId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(OrderConstants.STATUS_500, response.getBody().getStatusCode());
        assertEquals(OrderConstants.MESSAGE_500, response.getBody().getStatusMsg());
    }

    @Test
    void removeItemFromCart_CartExists() {
        String cartId = "CART123";
        String productCode = "PROD001";
        List<Item> cart = List.of(new Item());

        when(cartService.getItems(cartId)).thenReturn(cart);

        ResponseEntity<ResponseDto> response = cartController.removeItemFromCart(productCode, cartId);

        verify(cartService).removeItemFromCart(cartId, productCode);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(OrderConstants.STATUS_200, response.getBody().getStatusCode());
        assertEquals(OrderConstants.MESSAGE_200, response.getBody().getStatusMsg());
    }

    @Test
    void removeItemFromCart_CartNotFound() {
        String cartId = "CART123";
        String productCode = "PROD001";

        when(cartService.getItems(cartId)).thenReturn(null);

        ResponseEntity<ResponseDto> response = cartController.removeItemFromCart(productCode, cartId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(OrderConstants.STATUS_417, response.getBody().getStatusCode());
        assertEquals(OrderConstants.MESSAGE_417_DELETE, response.getBody().getStatusMsg());
    }
}

