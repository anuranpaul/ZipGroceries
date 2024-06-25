package com.Service.OrderingService.controller;

import com.Service.OrderingService.Service.CartService;
import com.Service.OrderingService.constants.OrderConstants;
import com.Service.OrderingService.dto.CartDto;
import com.Service.OrderingService.dto.ResponseDto;
import com.Service.OrderingService.entity.Item;
import com.Service.OrderingService.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/cart", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CartController {

    private static final Logger logger= LoggerFactory.getLogger(CartController.class);

    @Autowired
    CartService cartService;

    @GetMapping("/view")
    public ResponseEntity<CartDto> viewCart(@RequestHeader(value = "Cookie") String cartId) {
        logger.info("Fetching cart details for cart ID: {}", cartId);
        CartDto cartDto = cartService.getAllItemsFromCart(cartId);
        if (cartDto == null) {
            logger.warn("Cart details not found for cart ID: {}",cartId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartDto);
    }

    @PostMapping(value = "/add", params = {"productCode", "quantity"})
    public ResponseEntity<ResponseDto> addItemToCart(@RequestParam String productCode,
                                                     @RequestParam("quantity") Integer quantity,
                                                     @RequestHeader(value = "Cookie") String cartId) {
        logger.info("Adding item (productCode: {}, quantity: {}) to cart ID: {}", productCode, quantity, cartId);
        List<Item> cart = cartService.getItems(cartId);
        if (cart == null) {
            logger.warn("Cart does not exist for cart ID: {}",cartId);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto(OrderConstants.STATUS_500, OrderConstants.MESSAGE_500));
        }
        cartService.addToCart(cartId, productCode, quantity);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(OrderConstants.STATUS_202, OrderConstants.MESSAGE_202));

    }

    @PutMapping(value = "/modify", params = {"productCode", "quantity"})
    public ResponseEntity<ResponseDto> modifyItemsInCart(@RequestParam String productCode,
                                                         @RequestParam("quantity") Integer quantity,
                                                         @RequestHeader(value = "Cookie") String cartId) {
        logger.info("Modifying items in cart with cart ID : {}",cartId);
        List<Item> cart = cartService.getItems(cartId);
        if (!cart.isEmpty()) {
            cartService.changeItemQuantity(cartId, productCode, quantity);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseDto(OrderConstants.STATUS_203, OrderConstants.MESSAGE_203));
        }
        logger.warn("Cart not found for modification (cartId: {})", cartId);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto(OrderConstants.STATUS_500, OrderConstants.MESSAGE_500));
    }

    @DeleteMapping(value = "/delete", params = "productCode")
    public ResponseEntity<ResponseDto> removeItemFromCart(
            @RequestParam String productCode,
            @RequestHeader(value = "Cookie") String cartId) {
        logger.info("Removing item (productCode: {}) from cart ID: {}", productCode, cartId);
        List<Item> cart = cartService.getItems(cartId);
        if (cart != null) {
            cartService.removeItemFromCart(cartId, productCode);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(OrderConstants.STATUS_200, OrderConstants.MESSAGE_200));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseDto(OrderConstants.STATUS_417, OrderConstants.MESSAGE_417_DELETE));

    }
    @DeleteMapping("/delete/{cartId}")
    public void deleteCart(@PathVariable String cartId) {
        logger.info("Received request to delete cart with ID: {}", cartId);
        cartService.deleteCart(cartId);
    }
}
