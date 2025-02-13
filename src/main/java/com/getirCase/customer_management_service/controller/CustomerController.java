package com.getirCase.customer_management_service.controller;

import com.getirCase.customer_management_service.constants.ApiEndpoints;
import com.getirCase.customer_management_service.dto.CustomerDTO;
import com.getirCase.customer_management_service.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoints.CUSTOMER_BASE)
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(ApiEndpoints.GET_CUSTOMER)
    @Operation(summary = "Retrieve a customer by ID", description = "Fetches customer details using the provided ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer found successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid customer ID"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CustomerDTO> getCustomer(
            @Parameter(description = "ID of the customer to retrieve", required = true)
            @PathVariable Long id) {
        logger.info("Fetching customer with ID: {}", id);
        CustomerDTO customerDTO = customerService.getCustomer(id);
        return ResponseEntity.ok(customerDTO);
    }

    @PostMapping(ApiEndpoints.CREATE_CUSTOMER)
    @Operation(
            summary = "Create a new customer with a specific tier",
            description = "Creates a new customer with the specified tier (Regular, Gold, Platinum). Default is Regular."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CustomerDTO> createCustomer(
            @Parameter(description = "Customer data including tier (Regular, Gold, Platinum)", required = true)
            @Valid @RequestBody CustomerDTO customerDTO) {

        logger.info("Creating new customer: {}", customerDTO);
        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }


    @DeleteMapping(ApiEndpoints.DELETE_CUSTOMER)
    @Operation(
            summary = "Delete a customer by ID",
            description = "Deletes the customer with the specified ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid customer ID"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "ID of the customer to delete", required = true)
            @PathVariable Long id) {

        logger.info("Deleting customer with ID: {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(ApiEndpoints.UPDATE_CUSTOMER)
    @Operation(
            summary = "Update an existing customer",
            description = "Updates the customer information for the specified ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CustomerDTO> updateCustomer(
            @Parameter(description = "ID of the customer to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated customer data", required = true)
            @Valid @RequestBody CustomerDTO customerDTO) {

        logger.info("Updating customer with ID: {}", id);
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @PutMapping(ApiEndpoints.UPDATE_TIER)
    @Operation(
            summary = "Update customer tier based on order count",
            description = "Called by Order-Service to update customer tier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer tier updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CustomerDTO> updateCustomerTier(
            @Parameter(description = "Customer ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "New Order Count", required = true)
            @RequestParam int orderCount) {

        logger.info("Updating customer tier for customer ID: {}", id);
        CustomerDTO updatedCustomer = customerService.updateCustomerTier(id, orderCount);
        return ResponseEntity.ok(updatedCustomer);
    }

}
