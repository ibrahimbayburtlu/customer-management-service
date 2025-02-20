package com.getirCase.customer_management_service.service.handler;

import com.getirCase.customer_management_service.model.event.CustomerEvent;
import com.getirCase.customer_management_service.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class CustomerEventHandlerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerEventHandler customerEventHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleCustomerOrderCreated() {
        CustomerEvent event = new CustomerEvent();
        event.setCustomerId(1L);

        customerEventHandler.handleCustomerOrderCreated(event);
        verify(customerService, times(1)).createOrder(1L);
    }

    @Test
    void testHandleCustomerTierUpdated() {

        CustomerEvent event = new CustomerEvent();
        event.setCustomerId(1L);
        event.setOrderCount(5);


        customerEventHandler.handleCustomerTierUpdated(event);

        verify(customerService, times(1)).updateCustomerTier(1L, 5);
    }
}
