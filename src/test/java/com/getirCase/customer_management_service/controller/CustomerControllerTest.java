package com.getirCase.customer_management_service.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getirCase.customer_management_service.enums.CustomerTier;
import com.getirCase.customer_management_service.exception.CustomerNotFoundException;
import com.getirCase.customer_management_service.model.CustomerRequest;
import com.getirCase.customer_management_service.model.CustomerResponse;
import com.getirCase.customer_management_service.service.CustomerService;
import com.getirCase.customer_management_service.constants.ApiEndpoints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private CustomerRequest request;
    private CustomerResponse response;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();


        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        request = new CustomerRequest(
                "ibrahim", "bayburtlu", "ibrahimbayburtlu5@gmail.com", "5061225291",
                "merkez mahallesi kavaklı sokak", CustomerTier.REGULAR
        );
        response = new CustomerResponse(
                "ibrahim", "bayburtlu", "ibrahimbayburtlu5@gmail.com", "5061225291",
                "merkez mahallesi kavaklı sokak", CustomerTier.REGULAR
        );
    }


    @Test
    @DisplayName("Get Customer")
    void testGetCustomer() throws Exception {
        when(customerService.getCustomer(1L)).thenReturn(response);
        mockMvc.perform(get(ApiEndpoints.CUSTOMER_BASE + ApiEndpoints.GET_CUSTOMER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.surname").value(request.getSurname()))
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(request.getPhoneNumber()))
                .andExpect(jsonPath("$.address").value(request.getAddress()))
                .andExpect(jsonPath("$.tier").value(request.getTier().toString()));
    }

    @Test
    @DisplayName("Get Customer Not Found")
    void testGetCustomerNotFound() throws Exception {
        when(customerService.getCustomer(99L)).thenThrow(new CustomerNotFoundException("Customer not found with id: 99"));

        mockMvc.perform(get(ApiEndpoints.CUSTOMER_BASE + ApiEndpoints.GET_CUSTOMER, 99L))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Create Customer")
    void testCreateCustomer() throws Exception {

        when(customerService.createCustomer(any(CustomerRequest.class))).thenReturn(response);


        mockMvc.perform(post(ApiEndpoints.CUSTOMER_BASE + ApiEndpoints.CREATE_CUSTOMER)
                        .contentType("application/json")
                        .content("{\"name\":\"ibrahim\",\"surname\":\"bayburtlu\",\"email\":\"ibrahimbayburtlu5@gmail.com\",\"phoneNumber\":\"5061225291\",\"address\":\"merkez mahallesi kavaklı sokak\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.surname").value(request.getSurname()))
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(request.getPhoneNumber()))
                .andExpect(jsonPath("$.address").value(request.getAddress()))
                .andExpect(jsonPath("$.tier").value(request.getTier().toString()));
    }

    @Test
    @DisplayName("Create Customer Bad Request")
    void testCreateCustomerBadRequest() throws Exception {
        request = new CustomerRequest( "", "", "", "",
                "merkez mahallesi kavaklı sokak", CustomerTier.REGULAR);

        String invalidCustomerJson = OBJECT_MAPPER.writeValueAsString(request);

        mockMvc.perform(post(ApiEndpoints.CUSTOMER_BASE + ApiEndpoints.CREATE_CUSTOMER)
                        .contentType("application/json")
                        .content(invalidCustomerJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete Customer")
    void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete(ApiEndpoints.CUSTOMER_BASE + ApiEndpoints.DELETE_CUSTOMER, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("Delete Customer Not Found")
    void testDeleteCustomerNotFound() throws Exception {
        Long customerId = 99L;

        doThrow(new CustomerNotFoundException("Customer not found")).when(customerService).deleteCustomer(customerId);

        mockMvc.perform(delete(ApiEndpoints.CUSTOMER_BASE + ApiEndpoints.DELETE_CUSTOMER, customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // 404
    }

    @Test
    @DisplayName("Delete Customer Bad Request")
    void testDeleteCustomerBadRequest() throws Exception {
        String invalidCustomerId = "invalid";

        mockMvc.perform(delete(ApiEndpoints.CUSTOMER_BASE + ApiEndpoints.DELETE_CUSTOMER, invalidCustomerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // 400
    }

    @Test
    @DisplayName("Update Customer Success")
    void testUpdateCustomerSuccess() throws Exception {
        Long customerId = 1L;
        when(customerService.updateCustomer(eq(customerId), any(CustomerRequest.class))).thenReturn(response);

        mockMvc.perform(put(ApiEndpoints.CUSTOMER_BASE + ApiEndpoints.UPDATE_CUSTOMER, customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isOk()) // 200
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.surname").value(response.getSurname()))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(response.getPhoneNumber()))
                .andExpect(jsonPath("$.address").value(response.getAddress()))
                .andExpect(jsonPath("$.tier").value(response.getTier().toString()));
    }

    @Test
    @DisplayName("Update Customer Not Found")
    void testUpdateCustomerNotFound() throws Exception {
        Long customerId = 99L;
        when(customerService.updateCustomer(eq(customerId), any(CustomerRequest.class)))
                .thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(put(ApiEndpoints.CUSTOMER_BASE + ApiEndpoints.UPDATE_CUSTOMER, customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(response)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update Customer Tier Success")
    void testUpdateCustomerTierSuccess() throws Exception {
        Long customerId = 1L;
        int orderCount = 10;

        when(customerService.updateCustomerTier(eq(customerId), eq(orderCount))).thenReturn(response);

        mockMvc.perform(patch(ApiEndpoints.CUSTOMER_BASE + ApiEndpoints.UPDATE_TIER, customerId)
                        .param("orderCount", String.valueOf(orderCount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.surname").value(request.getSurname()))
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(request.getPhoneNumber()))
                .andExpect(jsonPath("$.address").value(request.getAddress()))
                .andExpect(jsonPath("$.tier").value(request.getTier().toString()));
    }

    @Test
    @DisplayName("Update Customer Tier Not Found")
    void testUpdateCustomerTierNotFound() throws Exception {
        Long customerId = 99L;
        int orderCount = 5;

        when(customerService.updateCustomerTier(eq(customerId), eq(orderCount)))
                .thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(patch(ApiEndpoints.CUSTOMER_BASE + ApiEndpoints.UPDATE_TIER, customerId)
                        .param("orderCount", String.valueOf(orderCount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }



}
