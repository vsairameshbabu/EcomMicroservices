package com.app.ecom.service;


import com.app.ecom.dto.OrderItemDTO;
import com.app.ecom.dto.OrderResponse;
import com.app.ecom.model.*;
import com.app.ecom.repository.OrderRepository;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartItemService cartItemService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String userId) {
        //cartItem validate
        List<CartItem> cartItem = cartItemService.getCart(userId);
        if (cartItem.isEmpty()) {
            return Optional.empty();
        }

        //user Validate
        Optional<User> userOp = userRepository.findById(Long.parseLong(userId));
        if (userOp.isEmpty()) {
            return Optional.empty();
        }
        User user = userOp.get();

        //calculate total
        BigDecimal totalAmount = cartItem.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // create order
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.CONFIRMED);
        List<OrderItem> orderItems = cartItem.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order)).toList();
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // clear cart
        cartItemService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order savedOrder) {
        return new OrderResponse(
                savedOrder.getId().toString(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus().name(),
                savedOrder.getOrderItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId().toString(),
                                orderItem.getProduct().getId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))))
                        .toList(),
                savedOrder.getCreatedAt().toString());
    }
}
