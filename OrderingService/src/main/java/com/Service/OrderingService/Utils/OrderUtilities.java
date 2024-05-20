package com.Service.OrderingService.Utils;

import com.Service.OrderingService.entity.Item;

import java.util.List;

public class OrderUtilities {
    public static double countTotalPrice(List<Item> cart) {
        double total = 0.0;
        for (Item item : cart) {
            total += item.getSubTotal();
        }
        return total;
    }
}
