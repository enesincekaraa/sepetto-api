package com.sepetto.api.customer.dto;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email
) {
}
