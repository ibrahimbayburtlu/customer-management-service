package com.getirCase.customer_management_service.repository;

import com.getirCase.customer_management_service.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  @Modifying
  @Transactional
  @Query("UPDATE Customer c SET c.isActive = :isActive WHERE c.id = :customerId")
  void updateIsActive(@Param("customerId") Long customerId, @Param("isActive") boolean isActive);
}
