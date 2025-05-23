package com.springlooks.order_service.service;

import com.springlooks.order_service.client.InventoryClient;
import com.springlooks.order_service.dto.OrderRequest;
import com.springlooks.order_service.model.Order;
import com.springlooks.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest) {
        boolean inStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (!inStock)
            throw new RuntimeException("Product: '" + orderRequest.skuCode() + "' is not in stock for quantity: " + orderRequest.quantity());

        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .price(orderRequest.price())
                .skuCode(orderRequest.skuCode())
                .quantity(orderRequest.quantity())
                .build();
        orderRepository.save(order);
    }
}
