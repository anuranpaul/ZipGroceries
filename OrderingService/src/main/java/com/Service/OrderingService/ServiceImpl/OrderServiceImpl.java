package com.Service.OrderingService.ServiceImpl;

import com.Service.OrderingService.Service.OrderService;
import com.Service.OrderingService.Utils.OrderUtilities;
import com.Service.OrderingService.constants.DeliverySlot;
import com.Service.OrderingService.constants.OrderStatus;
import com.Service.OrderingService.dto.ItemDto;
import com.Service.OrderingService.dto.OrderDto;
import com.Service.OrderingService.dto.DeliveryRequestDto;
import com.Service.OrderingService.dto.DeliveryResponseDto;
import com.Service.OrderingService.entity.Item;
import com.Service.OrderingService.entity.Order;
import com.Service.OrderingService.entity.User;
import com.Service.OrderingService.exceptions.ResourceNotFoundException;
import com.Service.OrderingService.feignClient.OrderClient;
import com.Service.OrderingService.feignClient.UserClient;
import com.Service.OrderingService.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Scope("prototype")
public class OrderServiceImpl implements OrderService {

    private static final Logger logger= LoggerFactory.getLogger(OrderService.class);

    @Autowired
    public UserClient userClient;

    @Autowired
    public OrderClient orderClient;

    @Autowired
    OrderRepository orderRepository;

    @Override
    public void placeOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    public Order createOrder(List<Item> cart, User user) {
        logger.info("Creating order for user: {}", user.getEmail());
        Order order = new Order();

        String orderNumber = "ORD" + new Random().ints(7, 0, 10)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        order.setItems(cart);
        order.setOrderNumber(orderNumber);
        order.setOrderedDate(LocalDate.now());
        order.setTotal(OrderUtilities.countTotalPrice(cart));
        order.setStatus(OrderStatus.PENDING.toString());
        order.setUser(user);
        return order;
    }

    @Override
    public Order getOrderById(String orderNumber){
        logger.info("Searching for order with order number: {}", orderNumber);
        Optional<Order> orderOptional = orderRepository.findByOrderNumber(orderNumber);
        if (orderOptional.isEmpty()) {
            logger.warn("Order not found with order number: {}", orderNumber);
            throw new ResourceNotFoundException("Order", "orderNumber", orderNumber);
        }
        return orderOptional.get();
    }

    @Override
    public List<OrderDto> getOrdersByUser(String userEmail) {
        logger.info("Received request to get orders for user email: {}", userEmail);
        User user = userClient.getUser(userEmail);
        if (user == null) {
            logger.warn("User not found for email: {}", userEmail);
            return Collections.emptyList(); // Return empty list if user not found
        }
        List<Order> orders = orderRepository.findAllByUserEmail(user.getEmail());
        logger.info("Found {} orders for user email: {}", orders.size(), userEmail);
        return convertOrdersToDtos(orders);
    }

    @Override
    public ResponseEntity<DeliveryResponseDto> chooseDeliverySlot(String orderNumber, DeliverySlot chosenSlot) {
        logger.info("Choosing delivery slot for order: {}", orderNumber);
        Order order = getOrderById(orderNumber); // Retrieve order details

        ResponseEntity<List<DeliverySlot>> response = orderClient.getAvailableDeliverySlots(orderNumber);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to retrieve available delivery slots: " + response.getStatusCodeValue());
        }

        List<DeliverySlot> availableSlots = response.getBody();
        logger.info("Available delivery slots retrieved for order: {}", orderNumber);
        order.setStatus(OrderStatus.CONFIRMED.toString());  // Set order status to CONFIRMED

        orderRepository.save(order); // Save updated order

        DeliveryResponseDto responseDto = new DeliveryResponseDto(orderNumber, chosenSlot);
        return ResponseEntity.ok(responseDto);
    }

    private List<OrderDto> convertOrdersToDtos(List<Order> orders) {
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            // Convert each Order object to an OrderDto object
            OrderDto orderDto = new OrderDto(
                    order.getId(),
                    order.getUser().getEmail(), // Assuming you have a getter for userEmail in Order
                    order.getOrderedDate(),
                    order.getOrderNumber(),
                    order.getStatus(),
                    order.getTotal(),
                    convertItemsToDtos(order.getItems()) // Assuming you have items and a convertItemsToDtos method
            );
            orderDtos.add(orderDto);
        }
        return orderDtos;
    }

    // Assuming you have a separate method for converting Item objects to ItemDto objects
    private List<ItemDto> convertItemsToDtos(List<Item> items) {
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            itemDtos.add(new ItemDto(item.getProduct().getProductId(), item.getProduct().getProductName(), item.getQuantity(), item.getProduct().getPrice()));
        }
        return itemDtos;
    }

}
