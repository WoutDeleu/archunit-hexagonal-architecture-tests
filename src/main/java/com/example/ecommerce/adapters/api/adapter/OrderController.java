package com.example.ecommerce.adapters.api.adapter;

import com.example.ecommerce.adapters.api.entity.OrderDto;
import com.example.ecommerce.adapters.api.entity.OrderItemDto;
import com.example.ecommerce.core.order.model.Order;
import com.example.ecommerce.core.order.model.OrderItem;
import com.example.ecommerce.core.order.model.OrderStatus;
import com.example.ecommerce.core.order.usecase.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderDto createOrder(@RequestBody OrderDto orderDto) {
        List<OrderItem> items = orderDto.getItems().stream()
                .map(dto -> new OrderItem(dto.getProductId(), dto.getProductName(),
                                        dto.getQuantity(), dto.getUnitPrice()))
                .collect(Collectors.toList());

        Order order = orderService.createOrder(orderDto.getCustomerId(), items);
        return toDto(order);
    }

    @GetMapping("/{id}")
    public OrderDto getOrder(@PathVariable UUID id) {
        Order order = orderService.getOrder(id);
        return toDto(order);
    }

    @GetMapping("/customer/{customerId}")
    public List<OrderDto> getCustomerOrders(@PathVariable UUID customerId) {
        return orderService.getCustomerOrders(customerId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/status")
    public OrderDto updateOrderStatus(@PathVariable UUID id, @RequestParam String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        Order order = orderService.updateOrderStatus(id, orderStatus);
        return toDto(order);
    }

    private OrderDto toDto(Order order) {
        List<OrderItemDto> itemDtos = order.getItems().stream()
                .map(item -> new OrderItemDto(item.getProductId(), item.getProductName(),
                                            item.getQuantity(), item.getUnitPrice()))
                .collect(Collectors.toList());

        return new OrderDto(
                order.getId(),
                order.getCustomerId(),
                itemDtos,
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCreatedAt()
        );
    }
}