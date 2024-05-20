package com.grocery.deliveryservice.service;

import com.grocery.deliveryservice.entity.Delivery;
import com.grocery.deliveryservice.serviceImpl.DeliveryServiceImpl;

import java.util.List;

public interface DeliveryService {
    List<DeliveryServiceImpl.DeliverySlot> getAvailableDeliverySlots(String orderNumber);

    Delivery saveDelivery(String orderNumber, DeliveryServiceImpl.DeliverySlot chosenSlot);
}
