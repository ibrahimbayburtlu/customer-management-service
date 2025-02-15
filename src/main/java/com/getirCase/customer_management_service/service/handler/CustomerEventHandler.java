package com.getirCase.customer_management_service.service.handler;

import com.getirCase.customer_management_service.model.CustomerEvent;
import com.getirCase.customer_management_service.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerEventHandler {

    private final CustomerService customerService;

    public CustomerEventHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void handleCustomerCreated(CustomerEvent event) {
        log.info("Processing CUSTOMER_CREATED event for customer ID: {}", event.getCustomerId());
        // Burada müşteri oluşturma işlemleri yapılır
    }

    public void handleCustomerUpdated(CustomerEvent event) {
        log.info("Processing CUSTOMER_UPDATED event for customer ID: {}", event.getCustomerId());
        // Burada müşteri güncelleme işlemleri yapılır
    }

    public void handleCustomerTierUpdated(CustomerEvent event) {
        log.info("Processing CUSTOMER_TIER_UPDATED event for customer ID: {}", event.getCustomerId());
        customerService.updateCustomerTier(event.getCustomerId(), event.getOrderCount());
    }

    public void handleCustomerOrderPlaced(CustomerEvent event) {
        log.info("Processing CUSTOMER_ORDER_PLACED event for customer ID: {}", event.getCustomerId());
        // Burada sipariş işlemleri yapılır
    }
}
