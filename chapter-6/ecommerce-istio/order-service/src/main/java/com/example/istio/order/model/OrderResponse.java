package com.example.istio.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for order responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String orderId;
    private OrderStatus status;
    private String message;
    private List<OrderItem> items;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    // Tracking fields for observability
    private String traceId;
    private String requestId;

    // Factory methods for common responses
    public static OrderResponse success(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus())
                .message("Order processed successfully")
                .items(order.getItems())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .build();
    }

    public static OrderResponse failure(String message) {
        return OrderResponse.builder()
                .status(OrderStatus.FAILED)
                .message(message)
                .build();
    }
}
