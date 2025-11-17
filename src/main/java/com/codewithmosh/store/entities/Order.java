package com.codewithmosh.store.entities;

import com.codewithmosh.store.dtos.OrderItemDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<OrderItem> orderItems = new HashSet<>();


    public static Order createOrderFromCart(Cart cart, User user){
        Order order = Order.builder()
                .customer(user)
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
                    order.orderItems.add(orderItem);
                }
        );
        return order;
    }

    public boolean isPlacedBy(User customer){
        return this.customer.getId().equals(customer.getId());
    }



}
