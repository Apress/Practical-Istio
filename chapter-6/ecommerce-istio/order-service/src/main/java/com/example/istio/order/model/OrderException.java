package com.example.istio.order.model;

/**
 * Exception for order-related errors
 * Includes fields helpful for monitoring and debugging
 */
public class OrderException extends RuntimeException {
    private final String orderId;
    private final OrderStatus orderStatus;
    private final String errorCode;
    private final String traceId;

    public OrderException(String message, String orderId, OrderStatus orderStatus,
                          String errorCode, String traceId) {
        super(message);
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.errorCode = errorCode;
        this.traceId = traceId;
    }

    // Getters for all fields
}
