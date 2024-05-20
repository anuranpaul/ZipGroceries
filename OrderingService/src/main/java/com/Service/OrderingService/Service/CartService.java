package com.Service.OrderingService.Service;

import com.Service.OrderingService.dto.CartDto;
import com.Service.OrderingService.entity.Item;

import java.util.List;

public interface CartService {
    public void addToCart(String cartId, String productCode, Integer quantity);

    public void changeItemQuantity(String cartId, String productCode, Integer quantity);
    public void removeItemFromCart(String cartId, String productCode);

    List<Item> getItems(String cartId);

    public CartDto getAllItemsFromCart(String cartId);
    public void deleteCart(String cartId);
}
