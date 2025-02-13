package com.getirCase.customer_management_service.mapper;

import com.getirCase.customer_management_service.dto.CustomerDTO;
import com.getirCase.customer_management_service.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class); // Manuel singleton

    CustomerDTO toDTO(Customer customer);
    Customer toEntity(CustomerDTO customerDTO);
}
