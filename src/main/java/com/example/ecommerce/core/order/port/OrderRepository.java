package com.example.ecommerce.core.order.port;

import com.example.ecommerce.core.order.model.Order;
import com.example.ecommerce.core.order.model.OrderStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Optional<Order> findById(UUID id);
    List<Order> findByCustomerId(UUID customerId);
    List<Order> findByStatus(OrderStatus status);
    Order save(Order order);
    void deleteById(UUID id);
}