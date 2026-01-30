package com.project.enums;

public enum OrderStatus {

    CREATED,            // Order created (before payment)
    PAID,               // Payment successful
    PREPARING,          // Restaurant is preparing food
    OUT_FOR_DELIVERY,   // Delivery partner picked up
    DELIVERED,          // Order delivered
    CANCELLED  ,
    REFUNDED// Order cancelled
}