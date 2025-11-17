package com.codewithmosh.store.dtos;

import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.mappers.ProductMapperImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

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
