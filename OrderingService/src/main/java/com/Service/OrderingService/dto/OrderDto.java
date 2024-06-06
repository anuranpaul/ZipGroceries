package com.Service.OrderingService.dto;

import com.Service.OrderingService.constants.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;
    private String userEmail;
    private LocalDate orderedDate;
    private String orderNumber;
    private String status;
    private double total;
    private List<ItemDto> items; // List of items in the order


}

