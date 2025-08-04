package com.example.istio.order.entity;

import com.example.istio.order.model.OrderItem;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private String productName;
    private String productCategory;

    // Convert from domain model to entity
    public static OrderItemEntity fromOrderItem(OrderItem orderItem) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setProductId(orderItem.getProductId());
        entity.setQuantity(orderItem.getQuantity());
        entity.setUnitPrice(orderItem.getUnitPrice());
        entity.setSubtotal(orderItem.getSubtotal());
        entity.setProductName(orderItem.getProductName());
        entity.setProductCategory(orderItem.getProductCategory());
        return entity;
    }

    // Convert from entity to domain model
    public OrderItem toOrderItem() {
        return OrderItem.builder()
                .productId(this.productId)
                .quantity(this.quantity)
                .unitPrice(this.unitPrice)
                .subtotal(this.subtotal)
                .productName(this.productName)
                .productCategory(this.productCategory)
                .build();
    }
}