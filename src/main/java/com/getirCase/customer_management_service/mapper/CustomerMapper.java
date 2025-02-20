package com.getirCase.customer_management_service.mapper;

import com.getirCase.customer_management_service.entity.Customer;
import com.getirCase.customer_management_service.model.request.CustomerRequest;
import com.getirCase.customer_management_service.model.response.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
  CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class); // Manuel singleton

  Customer toEntity(CustomerRequest request);

  CustomerResponse toResponse(Customer customer);
}
