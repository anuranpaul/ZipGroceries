package com.Service.OrderingService.constants;

import lombok.Data;
import lombok.Getter;

@Getter
public enum DeliverySlot {
    MORNING("Morning", 5, 10), // Time range (hours)
    MIDDAY("Midday", 10, 16),
    EVENING("Evening", 16, 23);

    private final String timeSlot;
    private final int startTime;
    private final int endTime;

    DeliverySlot(String timeSlot, int startTime, int endTime) {
        this.timeSlot = timeSlot;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean isAvailable(int currentTime) {
        return currentTime < startTime || currentTime >= endTime;
    }
}
