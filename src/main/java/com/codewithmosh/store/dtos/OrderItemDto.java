package com.codewithmosh.store.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private OrderProductDto product;
    private Integer quantity;
    private BigDecimal totalPrice;

  /*  public OrderItemDto(Product product, Integer quantity, BigDecimal totalPrice){
        this.product = productMapper.toDto(product);
        this.quantity = quantity;
        this.totalPrice = totalPrice;

    }*/

}
