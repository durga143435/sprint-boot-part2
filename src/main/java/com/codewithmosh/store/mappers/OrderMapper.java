package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.OrdersResponse;
import com.codewithmosh.store.entities.Order;
import org.hibernate.annotations.Source;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source ="orderItems", target= "items")
    OrdersResponse toDto(Order order);
}
