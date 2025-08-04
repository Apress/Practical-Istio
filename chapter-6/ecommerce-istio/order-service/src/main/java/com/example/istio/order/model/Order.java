package com.example.istio.order.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Main Order entity representing a customer order
 * Includes audit fields and status tracking for observability
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @NotNull
    private String orderId;

    @NotNull
    private String customerId;

    @NotNull
    private List<OrderItem> items;

    @NotNull
    private BigDecimal totalAmount;

    @NotNull
    private OrderStatus status;

    public Order(String customerId) {
        this.orderId = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.status = OrderStatus.CREATED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    private String paymentId;

    // Audit fields for tracking and observability
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String lastModifiedBy;

    // Trace fields for debugging and monitoring
    private String traceId;
    private String spanId;

    public void calculateTotalAmount() {
        this.totalAmount = items.stream()
                .map(item -> item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
