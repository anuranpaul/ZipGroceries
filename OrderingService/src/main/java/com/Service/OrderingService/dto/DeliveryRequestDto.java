package com.Service.OrderingService.dto;

import com.Service.OrderingService.constants.DeliverySlot;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryRequestDto {
    private DeliverySlot chosenSlot;
}
