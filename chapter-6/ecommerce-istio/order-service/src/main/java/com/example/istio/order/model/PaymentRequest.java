package com.example.istio.order.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request object for initiating a payment transaction
 * Contains all necessary information for payment processing
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    @NotNull(message = "Order ID is required")
    private String orderId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    // Optional fields for payment processing
    private String customerId;
    private String currency;
    private String paymentMethod;

    // Fields for distributed tracing
    private String traceId;
    private String spanId;

    // Constructor for basic payment request
    public PaymentRequest(String orderId, BigDecimal amount) {
        this.orderId = orderId;
        this.amount = amount;
        this.currency = "USD"; // Default currency
    }
}
