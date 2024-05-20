package com.grocery.deliveryservice;

import com.grocery.deliveryservice.controller.DeliveryController;
import com.grocery.deliveryservice.dto.DeliverySaveRequest;
import com.grocery.deliveryservice.entity.Delivery;
import com.grocery.deliveryservice.service.DeliveryService;
import com.grocery.deliveryservice.serviceImpl.DeliveryServiceImpl.DeliverySlot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeliveryControllerTests {

    @Mock
    private DeliveryService deliveryService;

    @InjectMocks
    private DeliveryController deliveryController;

    @Test
    void testGetAvailableDeliverySlots() {
        // Prepare test data
        String orderNumber = "ORD123";
        List<DeliverySlot> availableSlots = Arrays.asList(DeliverySlot.MORNING, DeliverySlot.MIDDAY);

        // Mock the behavior of deliveryService.getAvailableDeliverySlots
        when(deliveryService.getAvailableDeliverySlots(orderNumber)).thenReturn(availableSlots);

        // Call the method to be tested
        ResponseEntity<List<DeliverySlot>> response = deliveryController.getAvailableDeliverySlots(orderNumber);

        // Verify that deliveryService.getAvailableDeliverySlots was called with the correct argument
        verify(deliveryService, times(1)).getAvailableDeliverySlots(orderNumber);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(availableSlots, response.getBody());
    }

    @Test
    void testSaveDelivery() {
        // Prepare test data
        DeliverySaveRequest request = new DeliverySaveRequest("ORD123", DeliverySlot.EVENING);
        Delivery savedDelivery = new Delivery(); // You should replace this with an actual saved delivery

        // Mock the behavior of deliveryService.saveDelivery
        when(deliveryService.saveDelivery(request.getOrderNumber(), request.getChosenSlot())).thenReturn(savedDelivery);

        // Call the method to be tested
        ResponseEntity<Delivery> response = deliveryController.saveDelivery(request);

        // Verify that deliveryService.saveDelivery was called with the correct arguments
        verify(deliveryService, times(1)).saveDelivery(request.getOrderNumber(), request.getChosenSlot());

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedDelivery, response.getBody());
    }
}
