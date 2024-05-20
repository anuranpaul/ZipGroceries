package com.grocery.deliveryservice.serviceImpl;

import com.grocery.deliveryservice.entity.Delivery;
import com.grocery.deliveryservice.exception.ResourceNotFoundException;
import com.grocery.deliveryservice.feignClient.OrderClient;
import com.grocery.deliveryservice.repository.DeliveryRepository;
import com.grocery.deliveryservice.service.DeliveryService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private static final Logger logger= LoggerFactory.getLogger(DeliveryService.class);

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private OrderClient orderClient; // Assuming you have an OrderClient for communication

    @Getter
    public enum DeliverySlot {
        MORNING("Morning", 5, 10), // Time range (hours)
        MIDDAY("Midday", 10, 16),
        EVENING("Evening", 16, 23);

        private final String timeSlot;
        private final int startTime;
        private final int endTime;

        DeliverySlot(String timeSlot, int startTime, int endTime) {
            this.timeSlot = timeSlot;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public boolean isAvailable(int currentTime) {
            return currentTime < startTime || currentTime >= endTime;
        }
    }

    @Override
    public List<DeliverySlot> getAvailableDeliverySlots(String orderNumber) {
        // Retrieve order details from Order microservice
        ResponseEntity<String> orderResponse = orderClient.getOrderStatus(orderNumber);
        logger.info("Finding the available delivery slots for the ordernumber : {}",orderNumber);
        if (orderResponse.getStatusCode().is2xxSuccessful()) {
            String orderStatus = orderResponse.getBody();
            assert orderStatus != null;
            if (orderStatus.equals("PENDING")) {
                return generateAvailableDeliverySlots(LocalTime.now());
            } else {
                logger.warn("No order with status PENDING found.");
                return Collections.emptyList(); // No slots for non-pending orders
            }
        } else {
            logger.warn("Failed to retrieve order information from Order server : {}",orderNumber);
            throw new RuntimeException("Failed to retrieve order information from Order service: "
                    + orderResponse.getStatusCodeValue()); // Handle error
        }
    }
    private List<DeliverySlot> generateAvailableDeliverySlots(LocalTime currentTime) {
        LocalDate today = LocalDate.now();
        List<DeliverySlot> availableSlots = new ArrayList<>();
        for (DeliverySlot slot : DeliverySlot.values()) {
            if (slot.isAvailable(currentTime.getHour())) {
                availableSlots.add(slot); // Add existing slot object
            }
        }
        return availableSlots;
    }

    @Override
    public Delivery saveDelivery(String orderNumber, DeliverySlot chosenSlot) {
        logger.info("Attempting to retrieve order status for order: {}", orderNumber);

        ResponseEntity<String> orderResponse = orderClient.getOrderStatus(orderNumber);

        if (orderResponse.getStatusCode().is2xxSuccessful()) {
            String orderStatus = orderResponse.getBody(); // Assuming the response body contains the order status
            assert orderStatus != null;
            logger.info("Retrieved order status for order {}: {}", orderNumber, orderStatus);

            if (orderStatus.equals("CONFIRMED")) {
                Delivery delivery = new Delivery();
                delivery.setOrderNumber(orderNumber); // Assuming orderNumber is a String
                delivery.setDeliverySlot(chosenSlot);
                delivery.setStatus("CONFIRMED"); // Or set initial status as needed
                delivery.setCreatedAt(LocalDate.now()); // Set creation date

                logger.info("Delivery created for order {} with slot: {}", orderNumber, chosenSlot);
                return deliveryRepository.save(delivery);
            } else {

                logger.warn("Order {} not confirmed, cannot save delivery (Status: {})", orderNumber, orderStatus);
                throw new RuntimeException("Order is not confirmed for saving delivery (Status: " + orderStatus + ")");
            }
        } else {

            logger.error("Failed to retrieve order information from Order service for order {}: {}", orderNumber, orderResponse.getStatusCodeValue());
            throw new ResourceNotFoundException("Order", "orderNumber", orderNumber); // Handle error
        }
    }
}

