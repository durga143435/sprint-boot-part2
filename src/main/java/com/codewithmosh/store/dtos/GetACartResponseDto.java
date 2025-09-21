package com.codewithmosh.store.dtos;

import com.codewithmosh.store.entities.CartItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class GetACartResponseDto {

    private UUID id;
    private Set<CartItem> items = new LinkedHashSet();
    private BigDecimal totalPrice = BigDecimal.ZERO;


}
