package com.Service.OrderingService.feignClient;

import com.Service.OrderingService.entity.User;
import org.springframework.cloud.openfeign.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="user-service", url="http://localhost:8080/")
public interface UserClient {

    @GetMapping("/users/fetch")
    User getUser(@RequestParam(required = false) String email);
}
