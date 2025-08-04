package com.example.istio.order.service;

import com.example.istio.order.entity.OrderEntity;
import com.example.istio.order.model.*;
import com.example.istio.order.repository.OrderRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class OrderService {

    private final RestTemplate restTemplate;
    private final MeterRegistry meterRegistry;
    private final Tracer tracer;
    private final OrderRepository orderRepository;

    @Value("${service.inventory.url}")
    private String inventoryServiceUrl;

    @Value("${service.payment.url}")
    private String paymentServiceUrl;

    public OrderService(RestTemplate restTemplate, MeterRegistry meterRegistry,
                        Tracer tracer, OrderRepository orderRepository) {
        this.restTemplate = restTemplate;
        this.meterRegistry = meterRegistry;
        this.tracer = tracer;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse processOrder(OrderRequest orderRequest) {
        Timer.Sample timer = Timer.start(meterRegistry);

        // Create a span using OpenTelemetry's span builder
        Span span = tracer.spanBuilder("processOrder")
                .setAttribute("customerId", orderRequest.getCustomerId())
                .startSpan();

        // Use try-with-resources for proper context management
        try (Scope scope = span.makeCurrent()) {
            log.info("Processing order for customer: {}", orderRequest.getCustomerId());

            Order order = orderRequest.toOrder();
            order.setCreatedAt(LocalDateTime.now());
            order.setStatus(OrderStatus.CREATED);

            // Save initial order state
            OrderEntity orderEntity = OrderEntity.fromOrder(order);
            orderRepository.save(orderEntity);

            // Record metrics
            meterRegistry.counter("order.created",
                    "customer_id", order.getCustomerId()).increment();

            // Check inventory with current context
            if (!checkInventory(order)) {
                OrderEntity failedEntity = OrderEntity.fromOrder(order);
                failedEntity.setStatus(OrderStatus.FAILED);
                orderRepository.save(failedEntity);
                return handleOrderFailure(order, "Insufficient inventory");
            }

            order.setStatus(OrderStatus.INVENTORY_CONFIRMED);
            orderRepository.save(OrderEntity.fromOrder(order));

            order.calculateTotalAmount();

            // Process payment with current context
            PaymentResponse paymentResult = processPayment(order);
            if (!PaymentStatus.SUCCESSFUL.equals(paymentResult.getStatus())) {
                OrderEntity failedEntity = OrderEntity.fromOrder(order);
                failedEntity.setStatus(OrderStatus.PAYMENT_FAILED);
                orderRepository.save(failedEntity);
                return handleOrderFailure(order, "Payment processing failed");
            }

            order.setStatus(OrderStatus.COMPLETED);
            order.setPaymentId(paymentResult.getPaymentId());
            order.setUpdatedAt(LocalDateTime.now());

            orderRepository.save(OrderEntity.fromOrder(order));

            timer.stop(meterRegistry.timer("order.processing.time",
                    "status", "success",
                    "customer_id", order.getCustomerId()));

            // Set success status on span
            span.setStatus(StatusCode.OK);
            return OrderResponse.success(order);

        } catch (Exception e) {
            log.error("Error processing order", e);
            // Record error details using OpenTelemetry conventions
            span.setStatus(StatusCode.ERROR, e.getMessage());
            span.recordException(e);

            meterRegistry.counter("order.errors",
                    "error_type", e.getClass().getSimpleName()).increment();

            return OrderResponse.failure("Order processing failed: " + e.getMessage());
        } finally {
            span.end(); // End the span in finally block
        }
    }

    private boolean checkInventory(Order order) {
        // Create child span with current context as parent
        Span span = tracer.spanBuilder("checkInventory")
                .setAttribute(SemanticAttributes.CODE_FUNCTION, "checkInventory")
                .setAttribute("orderId", order.getOrderId())
                .startSpan();

        try (Scope scope = span.makeCurrent()) {
            Timer.Sample timer = Timer.start(meterRegistry);

            for (OrderItem item : order.getItems()) {
                String url = inventoryServiceUrl + "/check/" + item.getProductId()
                        + "?quantity=" + item.getQuantity();

                // Add span attributes for the inventory check
                span.setAttribute("product.id", item.getProductId());
                span.setAttribute("product.quantity", item.getQuantity());

                InventoryCheckResult result = restTemplate.getForObject(
                        url, InventoryCheckResult.class);

                if (result == null || !result.isAvailable()) {
                    span.setStatus(StatusCode.ERROR, "Insufficient inventory");
                    span.setAttribute("inventory.available", false);
                    return false;
                }
            }

            timer.stop(meterRegistry.timer("inventory.check.time"));
            span.setStatus(StatusCode.OK);
            return true;
        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR, e.getMessage());
            span.recordException(e);
            throw new OrderException("Error checking inventory",
                    order.getOrderId(),
                    OrderStatus.INVENTORY_CHECKING,
                    "INVENTORY_ERROR",
                    span.getSpanContext().getTraceId());
        } finally {
            span.end();
        }
    }

    private PaymentResponse processPayment(Order order) {
        // Create child span with current context as parent
        Span span = tracer.spanBuilder("processPayment")
                .setAttribute(SemanticAttributes.CODE_FUNCTION, "processPayment")
                .setAttribute("orderId", order.getOrderId())
                .setAttribute("amount", order.getTotalAmount().toString())
                .startSpan();

        try (Scope scope = span.makeCurrent()) {
            Timer.Sample timer = Timer.start(meterRegistry);

            PaymentResponse result = restTemplate.postForObject(
                    paymentServiceUrl + "/process",
                    new PaymentRequest(order.getOrderId(), order.getTotalAmount()),
                    PaymentResponse.class
            );

            timer.stop(meterRegistry.timer("payment.processing.time",
                    "status", result != null ? result.getStatus().toString() : "ERROR"));

            span.setStatus(StatusCode.OK);
            return result;
        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR, e.getMessage());
            span.recordException(e);
            throw new OrderException("Error processing payment",
                    order.getOrderId(),
                    OrderStatus.PAYMENT_PENDING,
                    "PAYMENT_ERROR",
                    span.getSpanContext().getTraceId());
        } finally {
            span.end();
        }
    }

    private OrderResponse handleOrderFailure(Order order, String reason) {
        Span currentSpan = Span.current();
        currentSpan.setAttribute("failure.reason", reason);

        order.setStatus(OrderStatus.FAILED);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(OrderEntity.fromOrder(order));

        meterRegistry.counter("order.failed",
                "reason", reason,
                "customer_id", order.getCustomerId()).increment();

        return OrderResponse.failure(reason);
    }

    public Optional<Order> getOrder(String orderId) {
        Span span = tracer.spanBuilder("getOrder")
                .setAttribute("orderId", orderId)
                .setAttribute(SemanticAttributes.CODE_FUNCTION, "getOrder")
                .startSpan();

        try (Scope scope = span.makeCurrent()) {
            Optional<Order> response =  orderRepository.findById(orderId)
                    .map(OrderEntity::toOrder);
            span.setStatus(response.isPresent() ? StatusCode.OK : StatusCode.ERROR);
            return response;

        } finally {
            span.end();
        }
    }
}