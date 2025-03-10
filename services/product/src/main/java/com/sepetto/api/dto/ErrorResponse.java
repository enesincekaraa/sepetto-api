package com.sepetto.api.dto;

import java.util.Map;

public record ErrorResponse(
        Map<String,String> errors

) {
}
