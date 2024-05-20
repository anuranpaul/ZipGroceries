package com.Service.OrderingService;

import com.Service.OrderingService.Service.CartService;
import com.Service.OrderingService.Service.OrderService;
import com.Service.OrderingService.constants.OrderConstants;
import com.Service.OrderingService.controller.OrderController;
import com.Service.OrderingService.dto.OrderDto;
import com.Service.OrderingService.dto.ResponseDto;
import com.Service.OrderingService.entity.Item;
import com.Service.OrderingService.entity.Order;
import com.Service.OrderingService.entity.User;
import com.Service.OrderingService.exceptions.ResourceNotFoundException;
import com.Service.OrderingService.feignClient.UserClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderControllerTests {

    @Mock
    private UserClient userClient;

    @Mock
    private OrderService orderService;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveOrder_Successful() {
        String userEmail = "test@example.com";
        String cartId = "cart123";

        User user = new User();
        List<Item> cart = Collections.singletonList(new Item());

        when(cartService.getItems(cartId)).thenReturn(cart);
        when(userClient.getUser(userEmail)).thenReturn(user);

        Order order = new Order();
        when(orderService.createOrder(cart, user)).thenReturn(order);
        doNothing().when(orderService).placeOrder(order);
        doNothing().when(cartService).deleteCart(cartId);

        ResponseEntity<ResponseDto> response = orderController.saveOrder(userEmail, cartId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(OrderConstants.STATUS_201, response.getBody().getStatusCode());
        verify(orderService).placeOrder(order);
        verify(cartService).deleteCart(cartId);
    }

    @Test
    void saveOrder_CartOrUserNotFound() {
        String userEmail = "test@example.com";
        String cartId = "cart123";

        when(cartService.getItems(cartId)).thenReturn(null);

        ResponseEntity<ResponseDto> response = orderController.saveOrder(userEmail, cartId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(OrderConstants.STATUS_500, response.getBody().getStatusCode());

        verify(orderService, never()).placeOrder(any(Order.class));
        verify(cartService, never()).deleteCart(cartId);
    }

    @Test
    void saveOrder_InternalServerError() {
        String userEmail = "test@example.com";
        String cartId = "cart123";

        User user = new User();
        List<Item> cart = Collections.singletonList(new Item());

        when(cartService.getItems(cartId)).thenReturn(cart);
        when(userClient.getUser(userEmail)).thenReturn(user);

        Order order = new Order();
        when(orderService.createOrder(cart, user)).thenReturn(order);
        doThrow(new RuntimeException("Error placing order")).when(orderService).placeOrder(order);

        ResponseEntity<ResponseDto> response = orderController.saveOrder(userEmail, cartId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(OrderConstants.STATUS_500, response.getBody().getStatusCode());

        verify(orderService).placeOrder(order);
        verify(cartService, never()).deleteCart(cartId);
    }

    @Test
    void getOrdersByUser_UserHasOrders() {
        String userEmail = "test@example.com";
        List<OrderDto> orderDtos = List.of(new OrderDto());

        when(orderService.getOrdersByUser(userEmail)).thenReturn(orderDtos);

        ResponseEntity<List<OrderDto>> response = orderController.getOrdersByUser(userEmail);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDtos, response.getBody());
    }

    @Test
    void getOrdersByUser_UserHasNoOrders() {
        String userEmail = "test@example.com";

        when(orderService.getOrdersByUser(userEmail)).thenReturn(Collections.emptyList());

        ResponseEntity<List<OrderDto>> response = orderController.getOrdersByUser(userEmail);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getOrderStatus_OrderExists() {
        String orderNumber = "ORD123";
        Order order = new Order();
        order.setStatus("CONFIRMED");

        when(orderService.getOrderById(orderNumber)).thenReturn(order);

        ResponseEntity<String> response = orderController.getOrderStatus(orderNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order.getStatus(), response.getBody());
    }

    @Test
    void getOrderStatus_OrderNotFound() {
        String orderNumber = "ORD123";

        when(orderService.getOrderById(orderNumber)).thenThrow(new ResourceNotFoundException("Order", "orderNumber", orderNumber));

        ResponseEntity<String> response = orderController.getOrderStatus(orderNumber);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getOrderStatus_InternalServerError() {
        String orderNumber = "ORD123";

        when(orderService.getOrderById(orderNumber)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<String> response = orderController.getOrderStatus(orderNumber);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}

