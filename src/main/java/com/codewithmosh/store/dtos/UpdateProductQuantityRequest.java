package com.codewithmosh.store.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateProductQuantityRequest {

    @NotNull
    @Min(value= 1, message ="Quantity should be greater than Zero")
    private  Integer quantity;

}
