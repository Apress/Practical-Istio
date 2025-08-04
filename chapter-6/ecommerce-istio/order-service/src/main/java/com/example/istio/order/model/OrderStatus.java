package com.example.istio.order.model;

import lombok.Getter;

/**
 * Order status enum with detailed states for tracking
 */
@Getter
public enum OrderStatus {
    CREATED("Order has been created"),
    VALIDATED("Order has been validated"),
    INVENTORY_CHECKING("Checking inventory availability"),
    INVENTORY_CONFIRMED("Inventory has been confirmed"),
    PAYMENT_PENDING("Awaiting payment processing"),
    PAYMENT_PROCESSED("Payment has been processed"),
    PAYMENT_FAILED("Payment processing failed"),
    COMPLETED("Order has been completed successfully"),
    CANCELLED("Order has been cancelled"),
    FAILED("Order processing failed");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

}
