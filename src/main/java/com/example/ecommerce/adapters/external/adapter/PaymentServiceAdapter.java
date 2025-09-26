package com.example.ecommerce.adapters.external.adapter;

import com.example.ecommerce.adapters.external.entity.PaymentRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class PaymentServiceAdapter {

    public boolean processPayment(UUID orderId, UUID customerId, BigDecimal amount) {
        PaymentRequest request = new PaymentRequest(
                orderId,
                customerId,
                amount,
                "USD",
                "CREDIT_CARD"
        );

        return callExternalPaymentService(request);
    }

    private boolean callExternalPaymentService(PaymentRequest request) {
        // In a real implementation, this would call an external payment service
        System.out.println("Processing payment for order: " + request.getOrderId() +
                          " amount: " + request.getAmount());
        return true; // Simulate successful payment
    }
}