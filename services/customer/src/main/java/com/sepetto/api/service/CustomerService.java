package com.sepetto.api.service;

import com.sepetto.api.dto.CustomerMapper;
import com.sepetto.api.dto.CustomerRequest;
import com.sepetto.api.dto.CustomerResponse;
import com.sepetto.api.exception.CustomerNotFoundException;
import com.sepetto.api.model.Customer;
import com.sepetto.api.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerService(CustomerRepository repository, CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public String createCustomer(CustomerRequest request) {
        var customer = repository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(@Valid CustomerRequest request) {
        var customer= repository.findById(request.id())
                .orElseThrow(()-> new CustomerNotFoundException(
                        format("Cannot update customer:: No customer found with the provided Id:: %s", request.id())
                ));
        mergeCustomer(customer,request);
        repository.save(customer);
    }


    public List<CustomerResponse> findAllCustomer() {
       return repository.findAll().stream()
                .map(
                mapper::fromCustomer)
                .collect(Collectors.toList());
    }

    public CustomerResponse findById(String customerId) {
        var customer = repository.findById(customerId).orElseThrow(
                ()-> new CustomerNotFoundException(
                        format(" No customer found with the provided Id:: %s", customerId)));
                return mapper.fromCustomer(customer);
    }


    public Boolean existsById(String customerId) {
        return repository.findById(customerId).isPresent();
    }


    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if (StringUtils.isNotBlank(request.firstName())){
            customer.setFirstName(request.firstName());
        }
        if (StringUtils.isNotBlank(request.lastName())){
            customer.setLastName(request.lastName());
        }
        if (StringUtils.isNotBlank(request.email())){
            customer.setEmail(request.email());
        }
        if (request.address() != null){
            customer.setAddress(request.address());
        }
    }


    public void deleteCustomer(String customerId) {
        repository.deleteById(customerId);
    }
}
