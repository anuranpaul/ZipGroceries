package com.Service.OrderingService;

import com.Service.OrderingService.ServiceImpl.OrderServiceImpl;
import com.Service.OrderingService.constants.DeliverySlot;
import com.Service.OrderingService.constants.OrderStatus;
import com.Service.OrderingService.dto.DeliveryResponseDto;
import com.Service.OrderingService.dto.OrderDto;
import com.Service.OrderingService.entity.Item;
import com.Service.OrderingService.entity.Order;
import com.Service.OrderingService.entity.User;
import com.Service.OrderingService.exceptions.ResourceNotFoundException;
import com.Service.OrderingService.feignClient.OrderClient;
import com.Service.OrderingService.feignClient.UserClient;
import com.Service.OrderingService.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTests {

    @Mock
    private UserClient userClient;

    @Mock
    private OrderClient orderClient;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void placeOrder() {
        Order order = new Order();
        orderService.placeOrder(order);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void createOrder() {
        List<Item> cart = Collections.emptyList();
        User user = new User();
        user.setEmail("test@example.com");

        Order order = orderService.createOrder(cart, user);

        assertNotNull(order);
        assertEquals(LocalDate.now(), order.getOrderedDate());
        assertNotNull(order.getOrderNumber());
        assertEquals(OrderStatus.PENDING.toString(), order.getStatus());
        assertEquals(user, order.getUser());
        assertEquals(cart, order.getItems());
    }

    @Test
    void getOrderById_OrderExists() {
        String orderNumber = "ORD123";
        Order order = new Order();
        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(orderNumber);

        assertNotNull(result);
        assertEquals(order, result);
    }

    @Test
    void getOrderById_OrderNotExists() {
        String orderNumber = "ORD123";
        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(orderNumber));
    }

    @Test
    void getOrdersByUser_UserExists() {
        String userEmail = "test@example.com";
        User user = new User();
        user.setEmail(userEmail);
        when(userClient.getUser(userEmail)).thenReturn(user);

        List<OrderDto> result = orderService.getOrdersByUser(userEmail);

        assertNotNull(result);
        assertEquals(0, result.size()); // Assuming no orders for this user
    }

    @Test
    void getOrdersByUser_UserNotExists() {
        String userEmail = "test@example.com";
        when(userClient.getUser(userEmail)).thenReturn(null);

        List<OrderDto> result = orderService.getOrdersByUser(userEmail);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void chooseDeliverySlot_Successful() {
        // Mock order data
        String orderNumber = "ORD123";
        Order order = new Order();
        order.setOrderNumber(orderNumber);


        List<DeliverySlot> availableSlots = new ArrayList<>();
        ResponseEntity<List<DeliverySlot>> responseEntity = new ResponseEntity<>(availableSlots, HttpStatus.OK);
        when(orderClient.getAvailableDeliverySlots(orderNumber)).thenReturn(responseEntity);

        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(order));
        Mockito.doReturn(null).when(orderRepository).save(order);

        DeliverySlot chosenSlot = DeliverySlot.MORNING;
        ResponseEntity<DeliveryResponseDto> response = orderService.chooseDeliverySlot(orderNumber, chosenSlot);

        Mockito.verify(orderRepository).save(order);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderNumber, response.getBody().getOrderNumber());
        assertEquals(chosenSlot, response.getBody().getChosenSlot());
    }

    @Test
    void chooseDeliverySlot_FailedToRetrieveSlots() {
        // Mock order data
        String orderNumber = "ORD123";
        Order order = new Order();
        order.setOrderNumber(orderNumber);

        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(order));

        ResponseEntity<List<DeliverySlot>> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(orderClient.getAvailableDeliverySlots(orderNumber)).thenReturn(responseEntity);

        DeliverySlot chosenSlot = DeliverySlot.MORNING;
        assertThrows(RuntimeException.class, () -> orderService.chooseDeliverySlot(orderNumber, chosenSlot));
    }
}
