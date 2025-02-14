package com.getirCase.customer_management_service.service.impl;

import com.getirCase.customer_management_service.dto.CustomerDTO;
import com.getirCase.customer_management_service.entity.Customer;
import com.getirCase.customer_management_service.enums.CustomerTier;
import com.getirCase.customer_management_service.exception.CustomerNotFoundException;
import com.getirCase.customer_management_service.mapper.CustomerMapper;
import com.getirCase.customer_management_service.repository.CustomerRepository;
import com.getirCase.customer_management_service.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) {
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
        return customerMapper.toDTO(customer);
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        if (customerDTO == null) {
            throw new IllegalArgumentException("Customer data cannot be null");
        }

        Customer customer = customerMapper.toEntity(customerDTO);
        if (customer.getTier() == null) {
            customer.setTier(CustomerTier.REGULAR);
        }

        Customer savedCustomer = customerRepository.save(customer);

        logger.info("Customer created with ID: {} and Tier: {}", savedCustomer.getId(), savedCustomer.getTier());
        return customerMapper.toDTO(savedCustomer);
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
    public CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDTO) {
        if (customerId == null) {
            logger.error("Customer ID is null");
            throw new IllegalArgumentException("Customer ID cannot be null");
        }

        if (customerDTO == null) {
            logger.error("Customer data is null");
            throw new IllegalArgumentException("Customer data cannot be null");
        }

        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    logger.warn("Customer not found with ID: {}", customerId);
                    return new CustomerNotFoundException("Customer not found with id: " + customerId);
                });

        existingCustomer.setName(customerDTO.getName());
        existingCustomer.setSurname(customerDTO.getSurname());
        existingCustomer.setEmail(customerDTO.getEmail());
        existingCustomer.setPhoneNumber(customerDTO.getPhoneNumber());

        Customer savedCustomer = customerRepository.save(existingCustomer);

        logger.info("Customer with ID {} has been updated", customerId);
        return customerMapper.toDTO(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomerTier(Long customerId, int orderCount) {
        try {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));

            customer.setOrderCount(orderCount);

            if (orderCount >= 20) {
                customer.setTier(CustomerTier.PLATINUM);
            } else if (orderCount >= 10) {
                customer.setTier(CustomerTier.GOLD);
            } else {
                customer.setTier(CustomerTier.REGULAR);
            }

            customerRepository.save(customer);
            logger.info("Updated customer {} to tier {}", customerId, customer.getTier());
            return customerMapper.toDTO(customer);
        } catch (Exception e) {
            logger.error("Error updating customer tier for ID {}: {}", customerId, e.getMessage());
            throw new RuntimeException("Unexpected error occurred while updating customer tier", e);
        }
    }
}
