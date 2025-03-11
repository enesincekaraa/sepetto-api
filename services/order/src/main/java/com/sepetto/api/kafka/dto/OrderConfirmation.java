package com.sepetto.api.kafka.dto;

import com.sepetto.api.customer.dto.CustomerResponse;
import com.sepetto.api.model.PaymentMethod;
import com.sepetto.api.product.dto.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
