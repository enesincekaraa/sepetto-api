package com.sepetto.api.payment;

import com.sepetto.api.customer.dto.CustomerResponse;
import com.sepetto.api.model.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
