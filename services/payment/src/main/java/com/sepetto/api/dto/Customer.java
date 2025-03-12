package com.sepetto.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record Customer(

        String id,

        @NotNull(message = "Firstname is required")
        String firstname,

        @NotNull(message = "lastname is required")
        String lastname,

        @Email(message = "The customer is not correctly formatted")
        @NotNull(message = "Email is required")
        String email
) {
}
