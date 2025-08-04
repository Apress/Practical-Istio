package com.example.istio.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response object for payment processing results
 * Includes detailed status and transaction information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String paymentId;
    private String orderId;
    private PaymentStatus status;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime processedAt;
    private String transactionId;
    private String errorMessage;

    // Tracing fields for observability
    private String traceId;
    private String spanId;

    // Factory methods for common responses
    public static PaymentResponse success(String orderId, String paymentId, BigDecimal amount) {
        return PaymentResponse.builder()
                .paymentId(paymentId)
                .orderId(orderId)
                .status(PaymentStatus.SUCCESSFUL)
                .amount(amount)
                .processedAt(LocalDateTime.now())
                .build();
    }

    public static PaymentResponse failure(String orderId, String errorMessage) {
        return PaymentResponse.builder()
                .orderId(orderId)
                .status(PaymentStatus.FAILED)
                .errorMessage(errorMessage)
                .processedAt(LocalDateTime.now())
                .build();
    }

    public boolean isSuccess() {
        return PaymentStatus.SUCCESSFUL.equals(this.status);
    }
}
