package com.codewithmosh.store.dtos;


import com.codewithmosh.store.validation.LowerCase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
//    @LowerCase
    private String email;

    @NotBlank(message = "password is required")
    @Size(min=5, max= 25, message = "password should be in the range of 5 to 25 characters")
    private String password;
}
