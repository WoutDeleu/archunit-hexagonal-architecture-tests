package com.example.ecommerce.adapters.external.entity;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentRequest {
    private UUID orderId;
    private UUID customerId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;

    public PaymentRequest() {}

    public PaymentRequest(UUID orderId, UUID customerId, BigDecimal amount,
                         String currency, String paymentMethod) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
    }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}