package com.sepetto.api.service;

import com.sepetto.api.customer.client.CustomerClient;
import com.sepetto.api.dto.OrderMapper;
import com.sepetto.api.dto.OrderResponse;
import com.sepetto.api.kafka.OrderProducer;
import com.sepetto.api.kafka.dto.OrderConfirmation;
import com.sepetto.api.orderline.dto.OrderLineRequest;
import com.sepetto.api.orderline.service.OrderLineService;
import com.sepetto.api.product.ProductClient;
import com.sepetto.api.dto.OrderRequest;
import com.sepetto.api.exception.BusinessException;
import com.sepetto.api.product.dto.PurchaseRequest;
import com.sepetto.api.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    public OrderService(OrderRepository repository, CustomerClient customerClient, ProductClient productClient, OrderMapper mapper, OrderLineService orderLineService, OrderProducer orderProducer) {
        this.repository = repository;
        this.customerClient = customerClient;
        this.productClient = productClient;
        this.mapper = mapper;
        this.orderLineService = orderLineService;
        this.orderProducer = orderProducer;
    }


    public Integer createOrder(OrderRequest request) {

        var customer = this.customerClient.findCustomerById(request.customerId()).orElseThrow(
                ()-> new BusinessException("Cannot create order :: No customer exist with the provided ID" + request.customerId()));

        var purchaseProducts = this.productClient.purchaseProducts(request.products());

        var order = this.repository.save(mapper.toOrder(request));

        for (PurchaseRequest purchaseRequest : request.products()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

       // start payment process

        orderProducer.sendOrderConfirmation(new OrderConfirmation(
                request.reference(),
                request.amount(),
                request.paymentMethod(),
                customer,
                purchaseProducts
        ));
        return order.getId();

    }



    public List<OrderResponse> findAll() {
        return repository.findAll().stream().map(mapper::fromOrder).collect(Collectors.toList());
    }


    public OrderResponse findById(Integer orderId) {
        return  repository.findById(orderId).map(mapper::fromOrder).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Order with ID %d not found", orderId)));
    }


}
