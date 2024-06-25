package com.Service.OrderingService.controller;

import com.Service.OrderingService.Service.CartService;
import com.Service.OrderingService.Service.OrderService;
import com.Service.OrderingService.constants.DeliverySlot;
import com.Service.OrderingService.constants.OrderConstants;
import com.Service.OrderingService.dto.DeliveryResponseDto;
import com.Service.OrderingService.dto.OrderDto;
import com.Service.OrderingService.dto.ResponseDto;
import com.Service.OrderingService.entity.Item;
import com.Service.OrderingService.entity.Order;
import com.Service.OrderingService.entity.User;
import com.Service.OrderingService.exceptions.ResourceNotFoundException;
import com.Service.OrderingService.feignClient.UserClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(path = "/order", produces = {MediaType.APPLICATION_JSON_VALUE})
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private UserClient userClient;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @PostMapping(value = "/user/{userEmail}")
    public ResponseEntity<ResponseDto> saveOrder(
            @PathVariable String userEmail,
            @RequestHeader(value = "Cookie") String cartId) {
        logger.info("Saving order for user: {}", userEmail);

        List<Item> cart = cartService.getItems(cartId);
        User user = userClient.getUser(userEmail);

        logger.info("Retrieved user details for email: {}", userEmail);

        if (cart != null && user != null) {
            Order order = orderService.createOrder(cart, user);
            try {
                orderService.placeOrder(order);
                //cartService.deleteCart(cartId);
                return status(HttpStatus.CREATED)
                        .body(new ResponseDto(OrderConstants.STATUS_201, OrderConstants.MESSAGE_201));
            } catch (Exception ex) {
                logger.error("Error placing order: ", ex);
                return status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseDto(OrderConstants.STATUS_500, OrderConstants.MESSAGE_500));
            }
        }
        logger.warn("Cart not found or user details not retrieved. Order cannot be saved.");
        return status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto(OrderConstants.STATUS_500, OrderConstants.MESSAGE_500));
    }

    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable("userEmail") String userEmail) {
        logger.info("Fetching order details for user: {}", userEmail);
        List<OrderDto> orderDtos = orderService.getOrdersByUser(userEmail);
        if (orderDtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDtos);
    }

    @GetMapping("/{orderNumber}/order-status")
    public ResponseEntity<String> getOrderStatus(@PathVariable String orderNumber) {
        try {
            Order order = orderService.getOrderById(orderNumber); // Use improved getOrderById with exception handling
            String deliveryStatus = order.getStatus();
            return ResponseEntity.ok(deliveryStatus);
        } catch (ResourceNotFoundException e) {
            logger.error("Order not found for ID: {}", orderNumber, e);
            return ResponseEntity.notFound().build(); // Return 404 Not Found if order not found
        } catch (Exception e) { // Catch any other unexpected exceptions
            logger.error("Error retrieving delivery status for order ID: {}", orderNumber, e);
            return ResponseEntity.internalServerError().build(); // Return 500 Internal Server Error
        }
    }

    @PostMapping("/{orderNumber}/choose-slot")
    public ResponseEntity<DeliveryResponseDto> chooseDeliverySlot(
            @PathVariable String orderNumber,
            @RequestBody(required = false) DeliverySlot chosenSlot) {
        logger.info("Choosing delivery slot for order: {}", orderNumber);
        return orderService.chooseDeliverySlot(orderNumber, chosenSlot);
    }
}