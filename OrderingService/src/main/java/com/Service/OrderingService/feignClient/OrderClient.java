package com.Service.OrderingService.feignClient;

import com.Service.OrderingService.constants.DeliverySlot;
import com.Service.OrderingService.dto.DeliveryRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "delivery-service", url = "http://localhost:8084/")
public interface OrderClient {
    @GetMapping("/deliveries/{orderNumber}/available-slots")
    ResponseEntity<List<DeliverySlot>> getAvailableDeliverySlots
            (@PathVariable String orderNumber);
}
