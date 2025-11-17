package com.codewithmosh.store.entities;

import com.codewithmosh.store.dtos.OrderItemDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@Entity
@Table(name="order_items")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;


    @Column(name ="unit_price")
    private BigDecimal unitPrice;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="total_price")
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    /*public static List<OrderItemDto> getOrderItemDto(List<OrderItem> orderItemList){
       return orderItemList.stream().map(item -> {
            return new OrderItemDto(item.product, item.quantity, item.totalPrice);
        }).toList();

    }*/

}
