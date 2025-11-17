package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.OrdersResponse;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.OrderItem;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.exceptions.NoOrdersFoundException;
import com.codewithmosh.store.exceptions.NoOrdersFoundForCurrentUserException;
import com.codewithmosh.store.mappers.OrderItemMapper;
import com.codewithmosh.store.mappers.OrderMapper;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.OrderItemRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final AuthService authService;

    public List<OrdersResponse> getOrders(User customer){
        List<Order> orders = orderRepository.findAllByCustomer(customer);

        if(orders.isEmpty())
            throw new NoOrdersFoundException();

        return orders.stream().map(order -> orderMapper.toDto(order)).toList();
        //my implementation to map Dto manually
      /*  List<OrdersResponse> ordersResponses= orders.stream().map(order -> {
           return new OrdersResponse().builder()
                    .id(order.getId())
                    .status(order.getStatus().toString())
                    .createdAt(order.getCreatedAt())
                    .items(OrderItem.getOrderItemDto(orderItemRepository.findAllByOrder(order)))
                    .totalPrice(order.getTotalPrice())
                    .build();
        }).toList();
        return ordersResponses;*/

    }

    public OrdersResponse getOrderById(Long orderId){
       /* Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null)
            throw new NoOrdersFoundException();*/

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NoOrdersFoundException());

        User customer = authService.getCurrentUser();

        if(!order.isPlacedBy(customer))
            throw new AccessDeniedException("you don't have the access to this order");
        return orderMapper.toDto(order);
    }
}
