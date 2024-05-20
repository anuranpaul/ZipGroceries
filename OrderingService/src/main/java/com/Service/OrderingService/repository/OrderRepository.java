package com.Service.OrderingService.repository;

import com.Service.OrderingService.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,String> {
    List<Order> findAllByUserEmail(String email);
    Optional<Order> findByOrderNumber(String orderNumber);
}
