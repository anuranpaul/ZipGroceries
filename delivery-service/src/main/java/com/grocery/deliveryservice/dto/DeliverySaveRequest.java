package com.grocery.deliveryservice.dto;

import com.grocery.deliveryservice.serviceImpl.DeliveryServiceImpl.DeliverySlot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliverySaveRequest {
    private String orderNumber;
    private DeliverySlot chosenSlot;
}
