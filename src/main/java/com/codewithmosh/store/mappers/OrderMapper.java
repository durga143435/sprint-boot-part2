package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.OrdersResponse;
import com.codewithmosh.store.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source ="orderItems", target= "items")
    OrdersResponse toDto(Order order);
}
