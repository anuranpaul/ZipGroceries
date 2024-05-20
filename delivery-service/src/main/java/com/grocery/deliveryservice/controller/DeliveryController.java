package com.grocery.deliveryservice.controller;

import com.grocery.deliveryservice.dto.DeliverySaveRequest;
import com.grocery.deliveryservice.entity.Delivery;
import com.grocery.deliveryservice.service.DeliveryService;
import com.grocery.deliveryservice.serviceImpl.DeliveryServiceImpl.DeliverySlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/deliveries", produces = {MediaType.APPLICATION_JSON_VALUE})
@CrossOrigin(origins = "*")
public class DeliveryController {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryController.class);

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping("/{orderNumber}/available-slots")
    public ResponseEntity<List<DeliverySlot>> getAvailableDeliverySlots
            (@PathVariable String orderNumber) {

        logger.info("Getting the available delivery slots for the specified order number: {}", orderNumber);
        List<DeliverySlot> availableSlots = deliveryService.getAvailableDeliverySlots(orderNumber);
        return ResponseEntity.ok(availableSlots);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Delivery> saveDelivery(@RequestBody DeliverySaveRequest request) {
        Delivery savedDelivery = deliveryService.saveDelivery(request.getOrderNumber()
                , request.getChosenSlot());
        logger.info("Saving the order number {} and the delivery slot {} to the DB"
                , request.getOrderNumber(), request.getChosenSlot());
        return ResponseEntity.ok(savedDelivery);
    }
}