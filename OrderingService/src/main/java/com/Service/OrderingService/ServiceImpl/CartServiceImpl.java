package com.Service.OrderingService.ServiceImpl;

import com.Service.OrderingService.Service.CartService;
import com.Service.OrderingService.Utils.CartUtilities;
import com.Service.OrderingService.dto.CartDto;
import com.Service.OrderingService.dto.CartDto.CartItemDto;
import com.Service.OrderingService.dto.InventoryResponse;
import com.Service.OrderingService.dto.ProductResponse;
import com.Service.OrderingService.entity.Item;
import com.Service.OrderingService.entity.Product;
import com.Service.OrderingService.exceptions.ResourceNotFoundException;
import com.Service.OrderingService.feignClient.InventoryClient;
import com.Service.OrderingService.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Scope("prototype")
public class CartServiceImpl implements CartService {

    private static final Logger logger= LoggerFactory.getLogger(CartService.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private InventoryClient inventoryClient;

    @Override
    public void addToCart(String cartId, String productCode, Integer quantity) {
        logger.info("Adding item (productCode: {}, quantity: {}) to cart ID: {}",
                productCode, quantity, cartId);
        if (!checkIfItemIsExist(productCode)) {
            throw new ResourceNotFoundException("Product", "productCode", productCode + " is not in stock.");
        }
        ProductResponse response = inventoryClient.getProductByProductId(productCode);
        if (response == null) {
            throw new ResourceNotFoundException("Product", "productCode", productCode + " information not available.");
        }
        Product prod = new Product();
        prod.setProductName(response.getProductName());
        prod.setProductCode(response.getProductCode());
        prod.setPrice(response.getPrice());
        prod.setQuantity(quantity);
        Item item = new Item(quantity, prod, CartUtilities.getSubTotalForItem(prod, quantity));
        item.setCartId(cartId);
        cartRepository.save(item);

    }


    @Transactional
    @Override
    public void changeItemQuantity(String cartId, String productCode, Integer quantity) {
        logger.info("Updating quantity of item (productCode: {}) to {} in cart ID: {}",
                productCode, quantity, cartId);
        List<Item> cart = cartRepository.findByCartId(cartId);
        if (cart.isEmpty()) {
            throw new ResourceNotFoundException("Products", "productCodes", String.join(",", productCode));
        }
        for (Item item : cart) {
            if ((item.getProduct().getProductCode()).equals(productCode)) {
                item.setQuantity(quantity);
                item.setSubTotal(CartUtilities.getSubTotalForItem(item.getProduct(), quantity));
                cartRepository.save(item); // Update the item directly
            }
        }
    }

    @Transactional
    @Override
    public void removeItemFromCart(String cartId, String productCode) {
        List<Item> cart = cartRepository.findByCartId(cartId);
        if (cart.isEmpty()) {
            logger.warn("No products found with the given product codes: {}", productCode);
            throw new ResourceNotFoundException("Products", "productCodes", String.join(",", productCode));
        }
        for (Item item : cart) {
            if ((item.getProduct().getProductCode()).equals(productCode)) {
                cartRepository.deleteByCartIdAndId(cartId, item.getId());
            }
        }
    }
    @Override
    public List<Item> getItems(String cartId){
        logger.info("Fetching items for cart ID: {}", cartId);
        return cartRepository.findAll();
    }

    /**
     *
     * @param productCode - to check whether the product code given exists in the inventory or not
     * @return - true or false, depending on response from the server
     */
    private boolean checkIfItemIsExist(String productCode) {
        logger.info("Checking if product with code: {} exists in inventory", productCode);
        InventoryResponse response = inventoryClient.inStock(productCode);
        if (response == null) {
            throw new ResourceNotFoundException("Product", "productCode", String.join(",", productCode));
        }
        return true;
    }

    /**
     *
     * @param cartId - cartID as the required parameter
     * @return - all the Items that have been added to cart
     */
    @Override
    public CartDto getAllItemsFromCart(String cartId) {
        logger.info("Fetching items for cart ID : {} with response", cartId);
        List<Item> items = cartRepository.findByCartId(cartId);

        if (items == null || items.isEmpty()) {
            throw new ResourceNotFoundException("Items", "cartId", cartId);
        }

        List<CartItemDto> cartItemDtos = convertCartItemsToDtos(items);
        Double subTotal = calculateEachSubTotal(items);

        return new CartDto(items.get(0).getCartId(), cartItemDtos, subTotal);
    }

    /**
     *
     * @param cartId - clears the cart
     */
    @Override
    @Transactional
    public void deleteCart(String cartId) {
        logger.info("Deleting items from cart ID: {}", cartId);
        cartRepository.deleteAll();
    }

    private List<CartItemDto> convertCartItemsToDtos(List<Item> items) {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for (Item item : items) {
            cartItemDtos.add(new CartItemDto(item.getProduct().getProductName(),
                    item.getProduct().getPrice(),
                    item.getQuantity()));
        }
        return cartItemDtos;
    }

    /**
     * Assuming a method to calculate subTotal based on items in the cart
     * @param items - Gets a list of items
     * @return - using the Cart utils subTotal method, get the subtotal for each item.
     */
    private Double calculateEachSubTotal(List<Item> items) {
        double total = 0.0;
        for (Item item : items) {
            Double itemSubTotal = CartUtilities.getSubTotalForItem(item.getProduct(), item.getQuantity()); // Use CartUtilities method
            if (itemSubTotal != null) { // Handle potential null subtotals
                total += itemSubTotal;
            }
        }
        return total;
    }
}
