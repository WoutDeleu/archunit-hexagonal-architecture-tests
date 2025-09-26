package com.example.ecommerce.core.order.usecase;

import com.example.ecommerce.core.order.model.Order;
import com.example.ecommerce.core.order.model.OrderItem;
import com.example.ecommerce.core.order.model.OrderStatus;
import com.example.ecommerce.core.order.port.OrderRepository;
import com.example.ecommerce.core.order.port.NotificationService;
import com.example.ecommerce.core.order.exceptions.OrderNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderService {
    private final OrderRepository orderRepository;
    private final NotificationService notificationService;

    public OrderService(OrderRepository orderRepository, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
    }

    public Order createOrder(UUID customerId, List<OrderItem> items) {
        BigDecimal totalAmount = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order(
                UUID.randomUUID(),
                customerId,
                items,
                totalAmount,
                OrderStatus.PENDING,
                LocalDateTime.now()
        );

        Order savedOrder = orderRepository.save(order);
        notificationService.sendOrderConfirmation(savedOrder);
        return savedOrder;
    }

    public Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
    }

    public Order updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = getOrder(orderId);
        Order updatedOrder = order.updateStatus(newStatus);
        Order savedOrder = orderRepository.save(updatedOrder);

        if (newStatus == OrderStatus.SHIPPED) {
            notificationService.sendShippingNotification(savedOrder);
        } else if (newStatus == OrderStatus.DELIVERED) {
            notificationService.sendDeliveryNotification(savedOrder);
        }

        return savedOrder;
    }

    public List<Order> getCustomerOrders(UUID customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
}