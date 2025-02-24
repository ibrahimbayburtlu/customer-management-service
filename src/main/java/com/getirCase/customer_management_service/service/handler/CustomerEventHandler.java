package com.getirCase.customer_management_service.service.handler;

import com.getirCase.customer_management_service.model.event.CustomerEvent;
import com.getirCase.customer_management_service.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class CustomerEventHandler {

  private final CustomerService customerService;

  public CustomerEventHandler(CustomerService customerService) {
    this.customerService = Objects.requireNonNull(customerService, "CustomerService cannot be null");
  }

  public void handleCustomerOrderCreated(CustomerEvent event) {
    log.info("Processing CUSTOMER_CREATED event for customer ID: {}", event.getCustomerId());
    customerService.createOrder(event.getCustomerId());
  }

  public void handleCustomerTierUpdated(CustomerEvent event) {
    log.info("Processing CUSTOMER_TIER_UPDATED event for customer ID: {}", event.getCustomerId());
    customerService.updateCustomerTier(event.getCustomerId(), event.getOrderCount());
  }
}
