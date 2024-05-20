package com.grocery.deliveryservice;

import com.grocery.deliveryservice.entity.Delivery;
import com.grocery.deliveryservice.exception.ResourceNotFoundException;
import com.grocery.deliveryservice.feignClient.OrderClient;
import com.grocery.deliveryservice.repository.DeliveryRepository;
import com.grocery.deliveryservice.serviceImpl.DeliveryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTests {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private OrderClient orderClient;

    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    private Delivery delivery;

    @BeforeEach
    void setUp() {
        delivery = new Delivery();
        delivery.setOrderNumber("ORD123");
        delivery.setDeliverySlot(DeliveryServiceImpl.DeliverySlot.MORNING);
        delivery.setStatus("CONFIRMED");
    }

    @Test
    void testGetAvailableDeliverySlots_Success() {
        String orderNumber = "ORDER123";
        when(orderClient.getOrderStatus(orderNumber))
                .thenReturn(new ResponseEntity<>("PENDING", HttpStatus.OK));

        List<DeliveryServiceImpl.DeliverySlot> slots = deliveryService.getAvailableDeliverySlots(orderNumber);

        assertNotNull(slots);
        assertFalse(slots.isEmpty());
        verify(orderClient, times(1)).getOrderStatus(orderNumber);
    }

    @Test
    void testGetAvailableDeliverySlots_OrderNotPending() {
        String orderNumber = "ORD123";
        when(orderClient.getOrderStatus(orderNumber))
                .thenReturn(new ResponseEntity<>("COMPLETED", HttpStatus.OK));

        List<DeliveryServiceImpl.DeliverySlot> slots = deliveryService.getAvailableDeliverySlots(orderNumber);

        assertTrue(slots.isEmpty());
        verify(orderClient, times(1)).getOrderStatus(orderNumber);
    }

    @Test
    void testGetAvailableDeliverySlots_OrderServiceError() {
        String orderNumber = "ORD123";
        when(orderClient.getOrderStatus(orderNumber))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> deliveryService.getAvailableDeliverySlots(orderNumber));

        assertTrue(exception.getMessage().contains("Failed to retrieve order information from Order service"));
        verify(orderClient, times(1)).getOrderStatus(orderNumber);
    }

    @Test
    void testSaveDelivery_Success() {
        String orderNumber = "ORD123";
        DeliveryServiceImpl.DeliverySlot chosenSlot = DeliveryServiceImpl.DeliverySlot.MORNING;
        when(orderClient.getOrderStatus(orderNumber))
                .thenReturn(new ResponseEntity<>("CONFIRMED", HttpStatus.OK));
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

        Delivery savedDelivery = deliveryService.saveDelivery(orderNumber, chosenSlot);

        assertNotNull(savedDelivery);
        assertEquals(orderNumber, savedDelivery.getOrderNumber());
        assertEquals("CONFIRMED", savedDelivery.getStatus());
        verify(orderClient, times(1)).getOrderStatus(orderNumber);
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    void testSaveDelivery_OrderNotConfirmed() {
        String orderNumber = "ORD123";
        DeliveryServiceImpl.DeliverySlot chosenSlot = DeliveryServiceImpl.DeliverySlot.MORNING;
        when(orderClient.getOrderStatus(orderNumber))
                .thenReturn(new ResponseEntity<>("PENDING", HttpStatus.OK));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> deliveryService.saveDelivery(orderNumber, chosenSlot));

        assertTrue(exception.getMessage().contains("Order is not confirmed for saving delivery"));
        verify(orderClient, times(1)).getOrderStatus(orderNumber);
        verify(deliveryRepository, times(0)).save(any(Delivery.class));
    }

    @Test
    void testSaveDelivery_OrderServiceError() {
        String orderNumber = "ORD123";
        DeliveryServiceImpl.DeliverySlot chosenSlot = DeliveryServiceImpl.DeliverySlot.MORNING;
        when(orderClient.getOrderStatus(orderNumber))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> deliveryService.saveDelivery(orderNumber, chosenSlot));

        assertTrue(exception.getMessage().contains("Order"));
        verify(orderClient, times(1)).getOrderStatus(orderNumber);
        verify(deliveryRepository, times(0)).save(any(Delivery.class));
    }
}
