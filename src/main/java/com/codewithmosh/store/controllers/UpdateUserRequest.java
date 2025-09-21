package com.codewithmosh.store.controllers;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;
    private String email;
}

