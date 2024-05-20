package com.Service.OrderingService.Utils;

import com.Service.OrderingService.entity.Product;

public class CartUtilities {
    public static Double getSubTotalForItem(Product product, int quantity) {
        return (product.getPrice()) * (quantity);
    }
}
