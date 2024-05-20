package com.Service.OrderingService.dto;

import com.Service.OrderingService.constants.DeliverySlot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class DeliveryResponseDto {
    private String orderNumber;
    private DeliverySlot chosenSlot;
}
