package com.getirCase.customer_management_service.service.impl;

import com.getirCase.customer_management_service.model.CustomerRequest;
import com.getirCase.customer_management_service.model.CustomerResponse;
import com.getirCase.customer_management_service.entity.Customer;
import com.getirCase.customer_management_service.enums.CustomerTier;
import com.getirCase.customer_management_service.exception.CustomerNotFoundException;
import com.getirCase.customer_management_service.mapper.CustomerMapper;
import com.getirCase.customer_management_service.repository.CustomerRepository;
import com.getirCase.customer_management_service.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponse getCustomer(Long customerId) {
        if (customerId == null) {
            logger.error("Customer ID is null");
            throw new IllegalArgumentException("Customer ID cannot be null");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    logger.warn("Customer not found with ID: {}", customerId);
                    return new CustomerNotFoundException("Customer not found with id: " + customerId);
                });

        logger.info("Customer found: {}", customer);
        return customerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Customer data cannot be null");
        }

        Customer customer = customerMapper.toEntity(request);
        if (customer.getTier() == null) {
            customer.setTier(CustomerTier.REGULAR);
        }

        Customer savedCustomer = customerRepository.save(customer);

        logger.info("Customer created with ID: {} and Tier: {}", savedCustomer.getId(), savedCustomer.getTier());
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        if (customerId == null) {
            logger.error("Customer ID is null");
            throw new IllegalArgumentException("Customer ID cannot be null");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    logger.warn("Customer not found with ID: {}", customerId);
                    return new CustomerNotFoundException("Customer not found with id: " + customerId);
                });

        customerRepository.updateIsActive(customerId,false);
        customerRepository.save(customer);
        logger.info("Customer with ID {} has been deleted", customerId);
    }

    @Override
    public CustomerResponse updateCustomer(Long customerId, CustomerRequest request) {
        if (customerId == null) {
            logger.error("Customer ID is null");
            throw new IllegalArgumentException("Customer ID cannot be null");
        }

        if (request == null) {
            logger.error("Customer data is null");
            throw new IllegalArgumentException("Customer data cannot be null");
        }

        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    logger.warn("Customer not found with ID: {}", customerId);
                    return new CustomerNotFoundException("Customer not found with id: " + customerId);
                });

        existingCustomer.setName(request.getName());
        existingCustomer.setSurname(request.getSurname());
        existingCustomer.setEmail(request.getEmail());
        existingCustomer.setPhoneNumber(request.getPhoneNumber());

        Customer savedCustomer = customerRepository.save(existingCustomer);

        logger.info("Customer with ID {} has been updated", customerId);
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse updateCustomerTier(Long customerId, int orderCount) {
        try {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));

            customer.setOrderCount(orderCount);

            customer.setTier(determineCustomerTier(orderCount));
            customerRepository.save(customer);
            logger.info("Updated customer {} to tier {}", customerId, customer.getTier());
            return customerMapper.toResponse(customer);
        } catch (Exception e) {
            logger.error("Error updating customer tier for ID {}: {}", customerId, e.getMessage());
            throw new RuntimeException("Unexpected error occurred while updating customer tier", e);
        }
    }

    @Override
    public CustomerResponse updateOrderCount(Long customerId, int orderCount) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + customerId + " not found"));

        int customerOrderCount = customer.getOrderCount();

        customer.setOrderCount(customerOrderCount + orderCount);

        CustomerTier tier = determineCustomerTier(customer.getOrderCount());
        customer.setTier(tier);

        logger.info("update_order_count {} to order count {}", customerId, orderCount);
        customerRepository.save(customer);

        return CustomerResponse.builder()
                .name(customer.getName())
                .surname(customer.getSurname())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .orderCount(customer.getOrderCount())
                .tier(tier)
                .build();
    }

    private CustomerTier determineCustomerTier(int orderCount) {
        if (orderCount >= 20) {
            return CustomerTier.PLATINUM;
        } else if (orderCount >= 10) {
            return CustomerTier.GOLD;
        } else {
            return CustomerTier.REGULAR;
        }
    }

}
