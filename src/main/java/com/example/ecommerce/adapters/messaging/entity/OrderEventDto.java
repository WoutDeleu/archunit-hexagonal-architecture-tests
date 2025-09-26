package com.example.ecommerce.adapters.messaging.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderEventDto {
    private UUID orderId;
    private UUID customerId;
    private BigDecimal totalAmount;
    private String status;
    private String eventType;
    private LocalDateTime timestamp;

    public OrderEventDto() {}

    public OrderEventDto(UUID orderId, UUID customerId, BigDecimal totalAmount,
                        String status, String eventType, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.eventType = eventType;
        this.timestamp = timestamp;
    }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}