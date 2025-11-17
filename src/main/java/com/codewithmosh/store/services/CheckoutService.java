package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.CheckoutResponse;
import com.codewithmosh.store.entities.*;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.PaymentException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CheckoutService {

    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;


    @Transactional
    public CheckoutResponse checkOut(UUID cartId) {

        Cart cart = cartRepository.findById(cartId).orElse(null);

        if (cart == null)
            throw new CartNotFoundException();

        if (cart.isEmpty())
            throw new CartEmptyException();

        //my approach
        /*Order order = Order.builder()
                .customer(authService.getCurrentUser())
                .status(OrderStatus.PENDING)
                .totalPrice(cart.getTotalPrice())
                .build();
        Set<OrderItem> orderItems = cart.getItems().stream().map(item->{
           return OrderItem.builder()
                    .unitPrice(item.getProduct().getPrice())
                    .product(item.getProduct())
                    .quantity(item.getQuantity())
                    .totalPrice(item.getTotalPrice())
                   .order(order)
                    .build();
        }).collect(Collectors.toUnmodifiableSet());

        order.setOrderItems(orderItems);*/



        /*Order order = Order.builder()
                .customer(authService.getCurrentUser())
                .status(OrderStatus.PENDING)
                .totalPrice(cart.getTotalPrice())
                .build();

        cart.getItems().forEach(item -> {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(item.getProduct())
                    .unitPrice(item.getProduct().getPrice())
                    .quantity(item.getQuantity())
                    .totalPrice(item.getTotalPrice())
                    .build();
            order.getOrderItems().add(orderItem);
                }
        );*/

        Order order = Order.createOrderFromCart(cart, authService.getCurrentUser());
        order = orderRepository.save(order);
        try {
            var session = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cart.getId());
            return new CheckoutResponse(order.getId(), session.getCheckoutUrl());
        } catch (PaymentException e) {
            orderRepository.delete(order);
            throw e;
        }
    }

    public void handleWebhookEvent(WebhookRequest request) {
        paymentGateway.parseWebhookRequest(request).ifPresent((result) -> {
            Order order = orderRepository.findById(result.getOrderId()).orElseThrow();
            order.setStatus(result.getStatus());
            orderRepository.save(order);
        });
    }

}
