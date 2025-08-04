package com.example.istio.order.entity;

import com.example.istio.order.model.Order;
import com.example.istio.order.model.OrderItem;
import com.example.istio.order.model.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class OrderEntity {
    @Id
    private String orderId;

    private String customerId;
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String paymentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String lastModifiedBy;
    private String traceId;
    private String spanId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItemEntity> items = new ArrayList<>();

    // Convert from domain model to entity
    public static OrderEntity fromOrder(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setOrderId(order.getOrderId());
        entity.setCustomerId(order.getCustomerId());
        entity.setTotalAmount(order.getTotalAmount());
        entity.setStatus(order.getStatus());
        entity.setPaymentId(order.getPaymentId());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());
        entity.setCreatedBy(order.getCreatedBy());
        entity.setLastModifiedBy(order.getLastModifiedBy());
        entity.setTraceId(order.getTraceId());
        entity.setSpanId(order.getSpanId());

        if (order.getItems() != null) {
            entity.setItems(order.getItems().stream()
                    .map(OrderItemEntity::fromOrderItem)
                    .toList());
        }

        return entity;
    }

    // Convert from entity to domain model
    public Order toOrder() {
        Order order = new Order();
        order.setOrderId(this.orderId);
        order.setCustomerId(this.customerId);
        order.setTotalAmount(this.totalAmount);
        order.setStatus(this.status);
        order.setPaymentId(this.paymentId);
        order.setCreatedAt(this.createdAt);
        order.setUpdatedAt(this.updatedAt);
        order.setCreatedBy(this.createdBy);
        order.setLastModifiedBy(this.lastModifiedBy);
        order.setTraceId(this.traceId);
        order.setSpanId(this.spanId);

        if (this.items != null) {
            order.setItems(this.items.stream()
                    .map(OrderItemEntity::toOrderItem)
                    .toList());
        }

        return order;
    }
}