package com.getirCase.customer_management_service.service;

import com.getirCase.customer_management_service.dto.CustomerDTO;
import jakarta.validation.Valid;

public interface CustomerService {
    CustomerDTO getCustomer(Long customerId);
    CustomerDTO createCustomer(@Valid CustomerDTO customerDTO);
    void deleteCustomer(Long customerId);
    CustomerDTO updateCustomer(Long customerId,CustomerDTO customerDTO);
    CustomerDTO updateCustomerTier(Long customerId, int orderCount);
}
