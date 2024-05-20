package com.grocery.deliveryservice.entity;

import com.grocery.deliveryservice.serviceImpl.DeliveryServiceImpl.DeliverySlot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@Document(collection = "deliveries")
@Data @AllArgsConstructor @NoArgsConstructor
public class Delivery {
    @Id
    private String id;
    private String orderNumber;
    private DeliverySlot deliverySlot;
    private String status;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
