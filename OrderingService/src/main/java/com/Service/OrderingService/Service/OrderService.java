package com.Service.OrderingService.Service;

import com.Service.OrderingService.constants.DeliverySlot;
import com.Service.OrderingService.dto.DeliveryResponseDto;
import com.Service.OrderingService.dto.OrderDto;
import com.Service.OrderingService.entity.Item;
import com.Service.OrderingService.entity.Order;
import com.Service.OrderingService.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    void placeOrder(Order order);

    Order createOrder(List<Item> cart, User user);

    List<OrderDto> getOrdersByUser(String userEmail);

    Order getOrderById(String orderNumber);

    ResponseEntity<DeliveryResponseDto> chooseDeliverySlot(String orderNumber, DeliverySlot chosenSlot);
}
