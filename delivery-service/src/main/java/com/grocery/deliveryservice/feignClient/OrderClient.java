package com.grocery.deliveryservice.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "delivery-service", url = "http://localhost:8083/")
public interface OrderClient {

    @GetMapping("/order/{orderNumber}/order-status")
    ResponseEntity<String> getOrderStatus(@PathVariable("orderNumber") String orderNumber);
}

