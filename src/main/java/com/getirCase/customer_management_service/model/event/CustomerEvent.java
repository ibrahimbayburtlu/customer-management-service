package com.getirCase.customer_management_service.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CustomerEvent {

  @JsonProperty("eventType")
  private String eventType;

  @JsonProperty("customerId")
  private Long customerId;

  @JsonProperty("orderCount")
  private Integer orderCount;

  @JsonProperty("timestamp")
  private LocalDateTime timestamp;

  @JsonProperty("data")
  private Object data;
}
