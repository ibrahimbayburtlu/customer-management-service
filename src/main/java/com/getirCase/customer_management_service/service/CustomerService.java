package com.getirCase.customer_management_service.service;

import com.getirCase.customer_management_service.model.CustomerRequest;
import com.getirCase.customer_management_service.model.CustomerResponse;
import jakarta.validation.Valid;

import java.math.BigDecimal;

public interface CustomerService {
    CustomerResponse getCustomer(Long customerId);
    CustomerResponse createCustomer(@Valid CustomerRequest request);
    void deleteCustomer(Long customerId);
    CustomerResponse updateCustomer(Long customerId,CustomerRequest request);
    CustomerResponse updateCustomerTier(Long customerId, int orderCount);
    CustomerResponse updateOrderCount(Long customerId, int orderCount);
}
