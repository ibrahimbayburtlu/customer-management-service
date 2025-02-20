package com.getirCase.customer_management_service.entity;

import com.getirCase.customer_management_service.enums.CustomerTier;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "surname", nullable = false, length = 100)
  private String surname;

  @Column(name = "email", nullable = false, unique = true, length = 150)
  private String email;

  @Column(name = "phone_number", nullable = false, unique = true, length = 20)
  private String phoneNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "tier", nullable = false)
  private CustomerTier tier = CustomerTier.REGULAR;

  @Column(name = "order_count", nullable = false)
  private int orderCount = 0;

  @Column(name = "address", columnDefinition = "TEXT")
  private String address;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "updated_at")
  private LocalDateTime updatedAt = LocalDateTime.now();

  @Column(name = "is_active", nullable = false)
  private boolean isActive = true;

  @PreUpdate
  public void setLastUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
