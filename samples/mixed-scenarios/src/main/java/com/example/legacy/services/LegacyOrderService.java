package com.example.legacy.services;

import org.springframework.stereotype.Service;

/**
 * ❌ BAD (Legacy): @Service annotation NOT in adapters package
 * ❌ Fails: services_should_be_in_adapters_only
 *
 * ❌ BAD (Legacy): Business logic in wrong package structure
 * ❌ Fails: infrastructure_should_not_contain_business_logic
 *
 * 🧊 FREEZE CANDIDATE: Use freeze to allow this while preventing new violations
 */
@Service
public class LegacyOrderService {

    public String processOrder(String orderData) {
        // ❌ Business logic in wrong place - should be in core
        if (orderData == null || orderData.trim().isEmpty()) {
            throw new IllegalArgumentException("Order data cannot be empty");
        }

        // Simulate order processing
        String orderId = "ORDER-" + System.currentTimeMillis();
        return "Created order: " + orderId + " with data: " + orderData;
    }

    public String findOrder(String id) {
        // ❌ More business logic in wrong place
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be empty");
        }

        return "Order found: " + id;
    }
}