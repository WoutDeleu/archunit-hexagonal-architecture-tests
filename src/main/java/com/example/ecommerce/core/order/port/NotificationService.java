package com.example.ecommerce.core.order.port;

import com.example.ecommerce.core.order.model.Order;

public interface NotificationService {
    void sendOrderConfirmation(Order order);
    void sendShippingNotification(Order order);
    void sendDeliveryNotification(Order order);
}