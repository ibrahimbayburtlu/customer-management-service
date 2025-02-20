package com.getirCase.customer_management_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.getirCase.customer_management_service.entity.Customer;
import com.getirCase.customer_management_service.enums.CustomerTier;
import com.getirCase.customer_management_service.mapper.CustomerMapper;
import com.getirCase.customer_management_service.model.request.CustomerRequest;
import com.getirCase.customer_management_service.model.response.CustomerResponse;
import com.getirCase.customer_management_service.repository.CustomerRepository;
import com.getirCase.customer_management_service.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerRequest request;
    private CustomerResponse response;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("ibrahim");
        customer.setSurname("bayburtlu");
        customer.setEmail("ibrahimbayburtlu5@gmail.com");
        customer.setPhoneNumber("5061225291");
        customer.setAddress("merkez mahallesi kavaklı sokak");
        customer.setTier(CustomerTier.REGULAR);
        customer.setOrderCount(5);

        request = new CustomerRequest("ibrahim", "bayburtlu", "ibrahimbayburtlu5@gmail.com", "5061225291", "merkez mahallesi kavaklı sokak", CustomerTier.REGULAR);
        response = new CustomerResponse("ibrahim", "bayburtlu", "ibrahimbayburtlu5@gmail.com", "5061225291", "merkez mahallesi kavaklı sokak", CustomerTier.REGULAR, 100);
        lenient().when(customerMapper.toResponse(any(Customer.class))).thenReturn(response);
    }

    @Test
    void testGetCustomer_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerResponse result = customerService.getCustomer(1L);

        assertNotNull(result);
        assertEquals("ibrahim", result.getName());
    }


    @Test
    void testCreateCustomer_Success() {

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse result = customerService.createCustomer(request);
        assertNotNull(result);
        assertEquals("ibrahim", result.getName());

        verify(customerRepository).save(any(Customer.class));
    }


    @Test
    void testUpdateCustomer_Success() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Act
        CustomerResponse result = customerService.updateCustomer(1L, request);

        // Assert
        assertNotNull(result);
        assertEquals("ibrahim", result.getName());

        // Verify
        verify(customerRepository, times(1)).save(any(Customer.class));

    }




    @Test
    void testUpdateCustomerTier_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse result = customerService.updateCustomerTier(1L, 15);

        assertNotNull(result);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testUpdateOrderCount_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse result = customerService.updateOrderCount(1L, 5);

        assertNotNull(result);
        assertEquals(10, customer.getOrderCount());

        verify(customerRepository, times(1)).save(customer);
    }


    @Test
    void testCreateOrder_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse result = customerService.createOrder(1L);

        assertNotNull(result);
        verify(customerRepository, times(1)).save(customer);
    }
}
