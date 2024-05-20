package com.Service.OrderingService.constants;

public class OrderConstants {
    private OrderConstants() {
        //to restrict instantiation
    }

    public static final String STATUS_201 = "201";
    public static final String MESSAGE_201 = "Order placed successfully";
    public static final String STATUS_202 = "202";
    public static final String MESSAGE_202 = "Items added successfully";
    public static final String STATUS_203 = "202";
    public static final String MESSAGE_203 = "Quantity updated successfully";
    public static final String STATUS_200 = "200";
    public static final String MESSAGE_200 = "Request processed successfully";
    public static final String STATUS_417 = "417";
    public static final String MESSAGE_417_UPDATE = "Update operation failed. Please try again or contact Dev team";
    public static final String MESSAGE_417_DELETE = "Delete operation failed. Please try again or contact Dev team";
    public static final String STATUS_500 = "500";
    public static final String MESSAGE_500 = "An error occurred. Please try again or contact Dev team";
}
