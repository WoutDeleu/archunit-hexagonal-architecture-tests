package com.example.legacy.controllers;

import com.example.legacy.services.LegacyOrderService;
import org.springframework.web.bind.annotation.*;

/**
 * ❌ BAD (Legacy): Controller NOT in adapters.api package
 * ❌ Fails: controllers_should_be_in_api_adapters
 *
 * 🧊 FREEZE CANDIDATE: Use freeze to allow this while preventing new violations
 */
@RestController
@RequestMapping("/legacy/orders")
public class LegacyOrderController {
    private final LegacyOrderService orderService;

    public LegacyOrderController(LegacyOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public String createOrder(@RequestBody String orderData) {
        return orderService.processOrder(orderData);
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable String id) {
        return orderService.findOrder(id);
    }
}