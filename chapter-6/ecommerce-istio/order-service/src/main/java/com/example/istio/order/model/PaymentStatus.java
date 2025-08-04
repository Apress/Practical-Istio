package com.example.istio.order.model;

/**
 * Enumeration of possible payment statuses
 * Each status includes a description for logging and monitoring
 */
public enum PaymentStatus {
    PENDING("Payment is pending processing"),
    PROCESSING("Payment is being processed"),
    SUCCESSFUL("Payment was successful"),
    FAILED("Payment failed"),
    CANCELLED("Payment was cancelled"),
    REFUNDED("Payment was refunded");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
