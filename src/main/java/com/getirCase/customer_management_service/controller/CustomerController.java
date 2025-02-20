package com.getirCase.customer_management_service.controller;

import com.getirCase.customer_management_service.constants.ApiEndpoints;
import com.getirCase.customer_management_service.exception.InvalidRequestException;
import com.getirCase.customer_management_service.model.request.CustomerRequest;
import com.getirCase.customer_management_service.model.request.OrderCountRequest;
import com.getirCase.customer_management_service.model.response.CustomerResponse;
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
import org.springframework.validation.BindingResult;
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
  @Operation(
      summary = "Retrieve a customer by ID",
      description = "Fetches customer details using the provided ID.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Customer found successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid customer ID"),
    @ApiResponse(responseCode = "404", description = "Customer not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<CustomerResponse> getCustomer(
      @Parameter(description = "ID of the customer to retrieve", required = true) @PathVariable
          Long id) {

    logger.info("Retrieving customer with ID: {}", id);
    CustomerResponse response = customerService.getCustomer(id);
    return ResponseEntity.ok(response);
  }

  @PostMapping(ApiEndpoints.CREATE_CUSTOMER)
  @Operation(
      summary = "Create a new customer",
      description = "Creates a new customer. Default tier is Regular.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Customer created successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request data"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<CustomerResponse> createCustomer(
      @Parameter(description = "Customer data including name and email", required = true)
          @Valid
          @RequestBody
          CustomerRequest request,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      throw new InvalidRequestException("Invalid customer data");
    }

    logger.info("Creating new customer: {}", request);
    CustomerResponse response = customerService.createCustomer(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @DeleteMapping(ApiEndpoints.DELETE_CUSTOMER)
  @Operation(
      summary = "Delete a customer by ID",
      description = "Deletes the customer with the specified ID.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid customer ID"),
    @ApiResponse(responseCode = "404", description = "Customer not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<Void> deleteCustomer(
      @Parameter(description = "ID of the customer to delete", required = true) @PathVariable
          Long id) {

    logger.info("Deleting customer with ID: {}", id);
    customerService.deleteCustomer(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping(ApiEndpoints.UPDATE_CUSTOMER)
  @Operation(
      summary = "Update an existing customer",
      description = "Updates the customer information for the specified ID.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request data"),
    @ApiResponse(responseCode = "404", description = "Customer not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<CustomerResponse> updateCustomer(
      @Parameter(description = "ID of the customer to update", required = true) @PathVariable
          Long id,
      @Parameter(description = "Updated customer data", required = true) @Valid @RequestBody
          CustomerRequest request) {

    logger.info("Updating customer with ID: {}", id);
    CustomerResponse response = customerService.updateCustomer(id, request);
    return ResponseEntity.ok(response);
  }

  @PatchMapping(ApiEndpoints.UPDATE_TIER)
  @Operation(
      summary = "Update customer tier",
      description = "Called by Order-Service to update customer tier based on order count.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Customer tier updated successfully"),
    @ApiResponse(responseCode = "404", description = "Customer not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<CustomerResponse> updateCustomerTier(
      @Parameter(description = "Customer ID", required = true) @PathVariable Long id,
      @Parameter(description = "New Order Count", required = true) @RequestParam int orderCount) {

    logger.info("Updating customer tier for customer ID: {} based on {} orders", id, orderCount);
    CustomerResponse response = customerService.updateCustomerTier(id, orderCount);
    return ResponseEntity.ok(response);
  }

  @PutMapping(ApiEndpoints.UPDATE_ORDER_COUNT)
  @Operation(
      summary = "Update customer order count",
      description = "Updates the order count for a customer.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Order count updated successfully"),
    @ApiResponse(responseCode = "404", description = "Customer not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<CustomerResponse> updateOrderCount(
      @Parameter(description = "Customer ID", required = true) @PathVariable Long id,
      @Parameter(description = "New Order Count", required = true) @RequestBody
          OrderCountRequest request) {

    logger.info("Updating order count for customer ID: {} to {}", id, request.getNewOrderCount());

    CustomerResponse response = customerService.updateOrderCount(id, request.getNewOrderCount());
    return ResponseEntity.ok(response);
  }
}
