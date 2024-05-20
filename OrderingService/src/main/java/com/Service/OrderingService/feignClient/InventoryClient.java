package com.Service.OrderingService.feignClient;

import com.Service.OrderingService.dto.InventoryResponse;
import com.Service.OrderingService.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service", url = "http://localhost:8081/")
public interface InventoryClient {

    @GetMapping("/inventory/view/{productCode}")
    ProductResponse getProductByProductId(@PathVariable String productCode);

    @GetMapping("/inventory/stock")
    InventoryResponse inStock(@RequestParam String productCode);


}
