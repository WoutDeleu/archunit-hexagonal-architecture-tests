package com.example.ecommerce.adapters.messaging.adapter;

import com.example.ecommerce.adapters.messaging.entity.OrderEventDto;
import com.example.ecommerce.core.order.model.Order;
import com.example.ecommerce.core.order.port.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationServiceAdapter implements NotificationService {

    @Override
    public void sendOrderConfirmation(Order order) {
        OrderEventDto event = new OrderEventDto(
                order.getId(),
                order.getCustomerId(),
                order.getTotalAmount(),
                order.getStatus().name(),
                "ORDER_CONFIRMED",
                LocalDateTime.now()
        );
        publishEvent(event);
    }

    @Override
    public void sendShippingNotification(Order order) {
        OrderEventDto event = new OrderEventDto(
                order.getId(),
                order.getCustomerId(),
                order.getTotalAmount(),
                order.getStatus().name(),
                "ORDER_SHIPPED",
                LocalDateTime.now()
        );
        publishEvent(event);
    }

    @Override
    public void sendDeliveryNotification(Order order) {
        OrderEventDto event = new OrderEventDto(
                order.getId(),
                order.getCustomerId(),
                order.getTotalAmount(),
                order.getStatus().name(),
                "ORDER_DELIVERED",
                LocalDateTime.now()
        );
        publishEvent(event);
    }

    private void publishEvent(OrderEventDto event) {
        // In a real implementation, this would publish to a message broker
        System.out.println("Publishing event: " + event.getEventType() + " for order: " + event.getOrderId());
    }
}